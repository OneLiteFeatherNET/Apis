package net.theevilreaper.apis.api.generator.debug;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.utils.validate.Check;
import net.theevilreaper.apis.api.BaseGenerator;
import net.theevilreaper.apis.api.data.RoomDTO;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.data.RoomType;
import net.theevilreaper.apis.api.generator.DungeonGeneratorImpl;
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
 * @since 1.0.0
 **/

public final class HorizontalLineGenerator extends BaseGenerator {

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
        //Aktuelle Richtung für die Generation South und links unten hinstellen
        var startRoom = dtos.stream().filter(roomDTO -> roomDTO.getRoomData().type() == RoomType.START_ROOM).findFirst().get();

        this.dtos.remove(startRoom);
        this.roomPlacement.place(instance, startPos, startRoom.getSchematicPath());

        int oldStartRoomX = startPos.blockX();
        int oldStartRoomZ = startPos.blockZ();


        // SOUT x -> neagtive z ins Positive
        for (int i = 0; i < this.dtos.size(); i++) {
            var currentRoom = this.dtos.get(i);
            generatorLogger.debug("Current room in queue is {}", currentRoom.getRoomData().type());
            if (startRoom.getRoomData().x() == currentRoom.getRoomData().x()) {
                // Only update z?
                int newStartZ = oldStartRoomZ + ((startRoom.getRoomData().z() - currentRoom.getRoomData().z()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
                var roomPosition = new Vec(startPos.blockX(), startPos.blockY(), newStartZ);
                generatorLogger.debug("New position is {}", roomPosition);
                this.roomPlacement.place(instance, roomPosition, currentRoom.getSchematicPath());
                continue;
            }

            int newStartX = oldStartRoomX + ((startRoom.getRoomData().x() - currentRoom.getRoomData().x()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
            var position = new Vec(newStartX, startPos.y(), startPos.z());
            generatorLogger.debug("New position is {}", position);
            this.roomPlacement.place(instance, position, currentRoom.getSchematicPath());
        }
    }
}
