package net.theevilreaper.apis.api.generator.debug;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.theevilreaper.apis.api.BaseGenerator;
import net.theevilreaper.apis.api.data.RoomDTO;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.data.RoomType;
import net.theevilreaper.apis.api.generator.DungeonGeneratorImpl;
import net.theevilreaper.apis.api.loader.RoomSchematicLoader;
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

public final class VerticalLineGenerator extends AbstractDebugGenerator {

    public VerticalLineGenerator(Path filePath, RoomSchematicLoader roomSchematicLoader) {
        super("Line", filePath, roomSchematicLoader);
        generatorLogger = LoggerFactory.getLogger(VerticalLineGenerator.class);
    }


    @Override
    public void generate(Point startPos) {
        // Current generation direction is south, starting from the bottom-left
        var startRoom = dtos.stream().filter(roomDTO -> roomDTO.roomData().type() == RoomType.START).findFirst().get();
        this.dtos.remove(startRoom);
        roomPlacement.place(instance, startPos, startRoom.schematicPath());

        int oldStartRoomX = startPos.blockX();

        // South: x -> negative z to positive
        for (int i = 0; i < this.dtos.size(); i++) {
            var currentRoom = this.dtos.get(i);
            generatorLogger.debug("Current room in queue is {}", currentRoom.roomData().type());
            if (startRoom.roomData().x() != currentRoom.roomData().x()) {
                // Only update z?

                int newStartX = oldStartRoomX - ((startRoom.roomData().x() - currentRoom.roomData().x()) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
                var position = new Vec(newStartX, startPos.y(), startPos.z());
                generatorLogger.debug("New position is {}", position);
                roomPlacement.place(instance, position, currentRoom.schematicPath());
            }
        }
    }
}
