package net.theevilreaper.apis.api.generator.unit;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public non-sealed class RoomUnitBuilder implements RoomUnit.Builder {

    private Path schematicPath;
    private Vec originPoint;
    private final Map<Vec, Chunk> chunkMap;

    public RoomUnitBuilder() {
        this.chunkMap = new HashMap<>();
    }

    @Override
    public RoomUnit.@NotNull Builder setSchematicPath(@NotNull Path path) {
        this.schematicPath = path;
        return this;
    }

    @Override
    public RoomUnit.@NotNull Builder setOriginPoint(@NotNull Vec vec) {
        this.originPoint = vec;
        return this;
    }

    @Override
    public RoomUnit.@NotNull Builder addChunk(@NotNull Vec vec, @Nullable Chunk chunk) {
        this.chunkMap.put(vec, chunk);
        return this;
    }

    @Override
    public @NotNull RoomUnit build() {
        return new RoomUnitImpl(this.schematicPath, this.originPoint, this.chunkMap);
    }
}
