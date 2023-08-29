package net.theevilreaper.apis.api.generator;

import net.hollowcube.util.schem.Rotation;
import net.hollowcube.util.schem.SchematicReader;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.utils.validate.Check;
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

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class HorizontalLineGenerator extends BaseGenerator {

    private final RoomSchematicLoader roomSchematicLoader;
    private final List<RoomDTO> dtos;

    public HorizontalLineGenerator(@NotNull Path filePath, @NotNull RoomSchematicLoader roomSchematicLoader) {
        super("Line",  filePath);
        this.roomSchematicLoader = roomSchematicLoader;
        this.dtos = new ArrayList<>();
        generatorLogger = LoggerFactory.getLogger(DungeonGeneratorImpl.class);
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
        System.out.println("Room size is " + this.dtos.size());
        System.out.println("Start position is " + startPos);
        //Aktuelle Richtung für die Generation South und links unten hinstellen
        var startRoom = dtos.stream().filter(roomDTO -> roomDTO.getRoomData().type() == RoomType.START_ROOM).findFirst().get();
        this.dtos.remove(startRoom);
        System.out.println("After remove from start "+ this.dtos.size());

        buildRoom(startPos, startRoom.getSchematicPath());

        int oldStartRoomX = startPos.blockX();
        int oldStartRoomZ = startPos.blockZ();


        // SOUT x -> neagtive z ins Positive
        for (int i = 0; i < this.dtos.size(); i++) {
            var currentRoom = this.dtos.get(i);
            System.out.println("Current room in queue is " + currentRoom.getRoomData().type());
            if (startRoom.getRoomData().x() == currentRoom.getRoomData().x()) {
                // Only update z?
                int newStartZ = oldStartRoomZ + ((startRoom.getRoomData().z() - currentRoom.getRoomData().z()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
                System.out.println("New position is " + new Vec(startPos.blockX(), startPos.blockY(), newStartZ));
                buildRoom(new Vec(startPos.blockX(), startPos.blockY(), newStartZ), currentRoom.getSchematicPath());
                continue;
            }

            int newStartX = oldStartRoomX + ((startRoom.getRoomData().x() - currentRoom.getRoomData().x()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
            System.out.println("New position is " + new Vec(newStartX, startPos.y(), startPos.z()));
            buildRoom(new Vec(newStartX, startPos.y(), startPos.z()), currentRoom.getSchematicPath());
        }

    }

    private void buildRoom(@NotNull Point position, @NotNull Path schematicPath) {
        var schematic = SchematicReader.read(schematicPath);
        if (instance.getChunkAt(position) == null) {
            instance.loadChunk(position.chunkX(), position.chunkZ());
        }

        schematic.build(Rotation.NONE, null).apply(instance, position, () -> {
            generatorLogger.info("Schematic successfully placed");
        });
    }
}
