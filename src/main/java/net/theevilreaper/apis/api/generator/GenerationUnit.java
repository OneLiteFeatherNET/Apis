package net.theevilreaper.apis.api.generator;

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

public class GenerationUnit {

    private Vec originPoint;
    private final Map<Vec, Chunk> chunks;
    private Path schematicPath;

    public GenerationUnit() {
        this.chunks = new HashMap<>();
    }

    public void setOriginPoint(Vec originPoint) {
        this.originPoint = originPoint;
    }

    public void setSchematicPath(Path schematicPath) {
        this.schematicPath = schematicPath;
    }

    public void addChunk(@NotNull Vec pos, @Nullable Chunk chunk) {
        this.chunks.put(pos, chunk);
    }
    public Vec getOriginPoint() {
        return originPoint;
    }

    public Map<Vec, Chunk> getChunks() {
        return chunks;
    }

    public Path getSchematicPath() {
        return schematicPath;
    }
}
