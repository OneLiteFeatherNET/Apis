package net.theevilreaper.apis.api.generator.unit;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public non-sealed class RoomUnitBuilder implements RoomUnit.Builder {

    private Path schematicPath;
    private Vec originPoint;
    private final Map<Vec, Chunk> chunkMap;

    public RoomUnitBuilder() {
        this.chunkMap = new HashMap<>();
    }

    @Override
    public RoomUnit.Builder setSchematicPath(Path path) {
        this.schematicPath = path;
        return this;
    }

    @Override
    public RoomUnit.Builder setOriginPoint(Vec vec) {
        this.originPoint = vec;
        return this;
    }

    @Override
    public RoomUnit.Builder addChunk(Vec vec, @Nullable Chunk chunk) {
        this.chunkMap.put(vec, chunk);
        return this;
    }

    @Override
    public RoomUnit build() {
        Objects.requireNonNull(this.schematicPath, "schematicPath cannot be null");
        Objects.requireNonNull(this.originPoint, "originPoint cannot be null");
        return new RoomUnitImpl(this.schematicPath, this.originPoint, this.chunkMap);
    }
}
