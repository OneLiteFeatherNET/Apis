package net.theevilreaper.apis.api.generator;

import net.hollowcube.util.schem.Rotation;
import net.hollowcube.util.schem.SchematicReader;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.chunk.ChunkUtils;
import net.theevilreaper.apis.api.BaseGenerator;
import net.theevilreaper.apis.api.data.RoomDTO;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.data.RoomType;
import net.theevilreaper.apis.api.loader.RoomSchematicLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public class DungeonGeneratorImpl extends BaseGenerator {

    private final RoomSchematicLoader roomSchematicLoader;
    private final List<RoomDTO> dtos;

    private final List<GenerationUnit> units;

    public DungeonGeneratorImpl(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath, RoomSchematicLoader roomSchematicLoader) {
        super(name, instance, filePath);
        this.roomSchematicLoader = roomSchematicLoader;
        this.dtos = new ArrayList<>();
        generatorLogger = LoggerFactory.getLogger(DungeonGeneratorImpl.class);
        this.units = new ArrayList<>();
    }

    public DungeonGeneratorImpl(@NotNull String name, @NotNull Path filePath, RoomSchematicLoader roomSchematicLoader) {
        super(name, filePath);
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
        var startRoom = dtos.stream().filter(roomDTO -> roomDTO.getRoomData().type() == RoomType.START_ROOM).findFirst().get();
        this.loadChunks(startPos, startRoom.getRoomData(), startRoom);
        for (RoomDTO dto : dtos) {
            this.loadChunks(startPos, startRoom.getRoomData(), dto);
        }


        this.units.forEach(generationUnit -> {
            for (Map.Entry<Vec, Chunk> posChunks : generationUnit.getChunks().entrySet()) {
                if (!ChunkUtils.isLoaded(posChunks.getValue())) {
                    instance.loadChunk(posChunks.getKey());
                }
            }
        });

        if (this.units.isEmpty()) {
            throw new IllegalArgumentException("NOPÜE");
        }

        for (GenerationUnit unit : units) {
            buildRoom(unit.getOriginPoint(), unit.getSchematicPath());
        }
    }

    private void loadChunks(@NotNull Point startPos, @NotNull RoomData startRoom, @NotNull RoomDTO currentRoom) {
        System.out.println("");
        System.out.println("CurrentRoom is " + currentRoom.getRoomData());
        int oldStartRoomX = startPos.blockX();
        int oldStartRoomZ = startPos.blockZ();

        var roomUnit = new GenerationUnit();

        Vec roomVec;

        if (startRoom.x() == currentRoom.getRoomData().x()) {
            // Only update z?
            int newStartZ = oldStartRoomZ + ((startRoom.z() - currentRoom.getRoomData().z()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
            roomVec = new Vec(startPos.blockX(), startPos.blockY(), newStartZ);
            System.out.println("[X Equal] New position is " + roomVec);
            //buildRoom(new Vec(startPos.blockX(), startPos.blockY(), newStartZ), currentRoom.getSchematicPath());
        } else {
            // The newStartX calculation must be positive otherwise the dungeon is mirrored
            int newStartX = oldStartRoomX + ((startRoom.x() - currentRoom.getRoomData().x()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
            int newStartZ = oldStartRoomZ + ((startRoom.z() - currentRoom.getRoomData().z()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
            roomVec = new Vec(newStartX, startPos.blockY(), newStartZ);
            System.out.println("[X Not] New position is " + roomVec);
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
            var block = switch (currentRoom.getRoomData().type()) {
                case SHOP_ROOM -> Block.LIME_WOOL;
                case NORMAL_ROOM -> Block.PURPLE_WOOL;
                case START_ROOM -> Block.ORANGE_WOOL;
                case DEAD_END -> Block.BLACK_WOOL;
                case BOSS_ROOM -> Block.RED_WOOL;
                case ITEM_ROOM -> Block.YELLOW_WOOL;
                case TELEPORT_ROOM -> Block.BLUE_WOOL;
            };
            instance.setBlock(verticalStartVec.add(0, 2, 0), block);
        }

        for (Vec horizontalStartVec : horizontalStartVecs) {
            roomUnit.addChunk(horizontalStartVec, instance.getChunkAt(horizontalStartVec));
            instance.setBlock(horizontalStartVec.add(0, 2, 0), Block.RED_WOOL);
        }

        roomUnit.setSchematicPath(currentRoom.getSchematicPath());

        this.units.add(roomUnit);
        System.out.println("");
    }

    private void buildRoom(@NotNull Point position, @NotNull Path schematicPath) {
        var schematic = SchematicReader.read(schematicPath);
        schematic.build(Rotation.NONE, null).apply(instance, position, () -> {
            generatorLogger.info("Schematic successfully placed");
        });

    }

    /**
     * Implementation of the save method to save a dungeon into the world folder.
     */
    @Override
    public void save() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
