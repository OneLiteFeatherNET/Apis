package net.theevilreaper.apis.api.generator.unit;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public interface RoomUnit {

    @Contract(pure = true)
    @NotNull static Builder builder() {
        return new RoomUnitBuilder();
    }

    @NotNull Path getSchematicPath();

    @NotNull Vec getOriginPoint();

    @NotNull Map<Vec, Chunk> getChunks();

    sealed interface Builder permits RoomUnitBuilder {

        @NotNull Builder setSchematicPath(@NotNull Path path);

        @NotNull Builder setOriginPoint(@NotNull Vec vec);

        @NotNull Builder addChunk(@NotNull Vec vec, @NotNull Chunk chunk);

        /**
         * Creates a new object reference of an {@link RoomUnit} implementation.
         * @return the created object
         */
        @NotNull RoomUnit build();
    }
}
