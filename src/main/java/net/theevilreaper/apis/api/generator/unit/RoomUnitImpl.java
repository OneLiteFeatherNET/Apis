package net.theevilreaper.apis.api.generator.unit;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class RoomUnitImpl implements RoomUnit {

    private final Path schematicPath;
    private final Vec originPoint;
    private final Map<Vec, Chunk> chunkMap;

    public RoomUnitImpl(Path schematicPath, Vec originPoint, Map<Vec, Chunk> chunkMap) {
        this.schematicPath = schematicPath;
        this.originPoint = originPoint;
        this.chunkMap = chunkMap;
    }

    @Override
    public @NotNull Path getSchematicPath() {
        return schematicPath;
    }

    @Override
    public @NotNull Vec getOriginPoint() {
        return originPoint;
    }

    @Override
    public @NotNull Map<Vec, Chunk> getChunks() {
        return chunkMap;
    }
}
