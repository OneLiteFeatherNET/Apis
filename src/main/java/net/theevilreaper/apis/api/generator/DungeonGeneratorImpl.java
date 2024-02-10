package net.theevilreaper.apis.api.generator;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.utils.validate.Check;
import net.theevilreaper.apis.api.BaseGenerator;
import net.theevilreaper.apis.api.data.RoomDTO;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.data.RoomType;
import net.theevilreaper.apis.api.event.GeneratorFinishEvent;
import net.theevilreaper.apis.api.generator.exception.GeneratorGenerationException;
import net.theevilreaper.apis.api.generator.unit.RoomUnit;
import net.theevilreaper.apis.api.loader.RoomSchematicLoader;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public class DungeonGeneratorImpl extends BaseGenerator {

    private final RoomSchematicLoader roomSchematicLoader;
    private final List<RoomDTO> dtos;
    private final List<RoomUnit> units;

    public DungeonGeneratorImpl(@NotNull Path filePath, RoomSchematicLoader roomSchematicLoader) {
        super("Isaac", filePath);
        this.roomSchematicLoader = roomSchematicLoader;
        this.dtos = new ArrayList<>();
        generatorLogger = LoggerFactory.getLogger(DungeonGeneratorImpl.class);
        this.units = new ArrayList<>();
    }

    @Override
    public void loadData() {
        super.loadData();
        this.dtos.clear();
        var regions = this.roomSchematicLoader.findRegions();
        if (regions.isEmpty()) {
            throw new IllegalArgumentException("Found a floor which does not contains any schematics");
        }
        var mapped = this.roomSchematicLoader.mapSchematicsByRegionsFiles(regions);
        for (RoomData roomDat : roomData) {
            try {
                dtos.add(this.roomSchematicLoader.findByRoomData(roomDat, mapped));
            } catch (IOException exception) {
                generatorLogger.warn("Unable to add schematic into the dto list", exception);
            }
        }
    }

    @Override
    public void generate(@NotNull Point startPos) {
        Check.argCondition(instance == null, "The instance can't be null");
        var chunkPos = verifyStartPosIntegrity(startPos);

        if (startPos.blockX() != chunkPos.blockX() || startPos.blockZ() != chunkPos.blockZ()) {
            startPos = chunkPos;
        }

        if (this.units.isEmpty()) {
            var startRoomOptional = dtos.stream().filter(roomDTO -> roomDTO.getRoomData().type() == RoomType.START_ROOM).findFirst();

            if (startRoomOptional.isEmpty()) {
                throw new GeneratorGenerationException("The floor plan contains no start room!");
            }

            var startRoom = startRoomOptional.get();

            this.loadChunks(startPos, startRoom.getRoomData(), startRoom);
            for (RoomDTO dto : dtos) {
                this.loadChunks(startPos, startRoom.getRoomData(), dto);
            }

            if (this.units.isEmpty()) {
                throw new GeneratorGenerationException("Something wen't wrong during the chunk scanning!");
            }
            this.units.forEach(roomUnit -> this.chunkHandling.handleChunks(instance, roomUnit.getChunks()));
        }

        new SchematicPlacementTask(this.instance, this.units, roomPlacement, () -> {
            var finishEvent = new GeneratorFinishEvent(this.getName(), this.units);
            MinecraftServer.getGlobalEventHandler().call(finishEvent);
        });
    }

    /**
     * Determines the southwest position of a chunk.
     * @param position the position to use
     * @return the determined southwest position
     */
    @Contract(value = "_ -> new", pure = true)
    private @NotNull Vec verifyStartPosIntegrity(@NotNull Point position) {
        int startChunkX = position.chunkX();
        int startChunkZ = position.chunkZ();

        int southChunkX = (startChunkX << 4) + 15;
        int southChunkZ = startChunkZ << 4;

        return new Vec(southChunkX, position.blockY(), southChunkZ);
    }

    /**
     * The method loads all chunks for a room when some of them are not loaded.
     * @param startPos the position to start
     * @param startRoom the {@link RoomData} reference from the start room
     * @param currentRoom the {@link RoomDTO} from the current room
     */
    private void loadChunks(@NotNull Point startPos, @NotNull RoomData startRoom, @NotNull RoomDTO currentRoom) {
        int oldStartRoomX = startPos.blockX();
        int oldStartRoomZ = startPos.blockZ();

        var roomUnit = RoomUnit.builder();

        Vec roomVec;

        if (startRoom.x() == currentRoom.getRoomData().x()) {
            // Only update z?
            int newStartZ = this.zPartCalculation.calculatePointPart(oldStartRoomZ, startRoom.z(), currentRoom.getRoomData().z(), roomScale);
            roomVec = new Vec(startPos.blockX(), startPos.blockY(), newStartZ);
        } else {
            // The newStartX calculation must be positive otherwise the dungeon is mirrored
            int newStartX = this.xPartCalculation.calculatePointPart(oldStartRoomX, startRoom.x(), currentRoom.getRoomData().x(), roomScale);
            int newStartZ = this.zPartCalculation.calculatePointPart(oldStartRoomZ, startRoom.z(), currentRoom.getRoomData().z(), roomScale);
            roomVec = new Vec(newStartX, startPos.blockY(), newStartZ);
        }

        roomUnit.setOriginPoint(roomVec);

        Vec[] horizontalStartVecs = new Vec[roomScale];
        horizontalStartVecs[0] = roomVec;
        for (int i = 1; i < roomScale; i++) {
            horizontalStartVecs[i] = roomVec.sub((i * Chunk.CHUNK_SECTION_SIZE), 0, 0);
        }

        Vec[] verticalStartVecs = new Vec[((roomScale - 1) * (roomScale))];
        int index = 0; // Represents the index counter for the verticalStartVecs array

        for (int horiIndex = 0; horiIndex < horizontalStartVecs.length; horiIndex++) {
            var currentStartVec = horizontalStartVecs[horiIndex];
            for (int vertiIndex = 1; vertiIndex < roomScale; vertiIndex++) {
                verticalStartVecs[index++] = currentStartVec.add(0, 0, (vertiIndex * Chunk.CHUNK_SIZE_Z));
            }
        }

        for (Vec verticalStartVec : verticalStartVecs) {
            roomUnit.addChunk(verticalStartVec, instance.getChunkAt(verticalStartVec));
        }

        for (Vec horizontalStartVec : horizontalStartVecs) {
            roomUnit.addChunk(horizontalStartVec, instance.getChunkAt(horizontalStartVec));
        }

        roomUnit.setSchematicPath(currentRoom.getSchematicPath());

        this.units.add(roomUnit.build());
    }
}
