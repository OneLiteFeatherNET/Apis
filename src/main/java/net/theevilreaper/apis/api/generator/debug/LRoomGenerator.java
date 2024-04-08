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
 * @since
 **/

public final class LRoomGenerator extends BaseGenerator {

    private final RoomSchematicLoader roomSchematicLoader;
    private final List<RoomDTO> dtos;

    public LRoomGenerator(@NotNull Path filePath, @NotNull RoomSchematicLoader roomSchematicLoader) {
        super("LGen", filePath);
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
        var startRoom = dtos.stream().filter(roomDTO -> roomDTO.roomData().type() == RoomType.START).findFirst().get();
        this.dtos.remove(startRoom);
        generatorLogger.info("After remove from start {}", this.dtos.size());
        this.roomPlacement.place(instance, startPos, startRoom.schematicPath());

        int oldStartRoomX = startPos.blockX();
        int oldStartRoomZ = startPos.blockZ();


        // SOUT x -> neagtive z ins Positive
        for (int i = 0; i < this.dtos.size(); i++) {
            var currentRoom = this.dtos.get(i);
            generatorLogger.debug("Current room in queue is {}",currentRoom.roomData().type());
            // Trivial case
            if (startRoom.roomData().x() == currentRoom.roomData().x()) {
                // Only update z?
                int newStartZ = oldStartRoomZ + ((startRoom.roomData().z() - currentRoom.roomData().z()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
                var position = new Vec(startPos.blockX(), startPos.blockY(), newStartZ);
                generatorLogger.debug("New position is {}", position);
                this.roomPlacement.place(instance, position, currentRoom.schematicPath());
                continue;
            }

            int newStartX = oldStartRoomX - ((startRoom.roomData().x() - currentRoom.roomData().x()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
            int newStartZ = oldStartRoomZ + ((startRoom.roomData().z() - currentRoom.roomData().z()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));

            var newPos = new Vec(newStartX, startPos.blockY(), newStartZ);

            generatorLogger.debug("[1] New position is {}", newPos);

            this.roomPlacement.place(instance, newPos, currentRoom.schematicPath());
        }
    }
}
