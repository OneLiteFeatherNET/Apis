package net.theevilreaper.apis.api.generator.unit;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public interface RoomUnit {

    @Contract(pure = true)
    static Builder builder() {
        return new RoomUnitBuilder();
    }

    Path schematicPath();

    Vec originPoint();

    Map<Vec, Chunk> chunks();

    sealed interface Builder permits RoomUnitBuilder {

        Builder setSchematicPath(Path path);

        Builder setOriginPoint(Vec vec);

        Builder addChunk(Vec vec, @Nullable Chunk chunk);

        /**
         * Creates a new object reference of an {@link RoomUnit} implementation.
         * @return the created object
         */
        RoomUnit build();
    }
}
