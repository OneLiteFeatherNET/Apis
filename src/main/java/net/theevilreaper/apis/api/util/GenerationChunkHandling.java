package net.theevilreaper.apis.api.util;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.chunk.ChunkUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public final class GenerationChunkHandling {

    private GenerationChunkHandling() {}

    public static void handleGenerationChunkLoading(@NotNull Instance instance, @NotNull Map<Vec, Chunk> chunkMap) {
        if (chunkMap.isEmpty()) return;

        for (Map.Entry<Vec, Chunk> posChunks : chunkMap.entrySet()) {
            if (!ChunkUtils.isLoaded(posChunks.getValue())) {
                instance.loadChunk(posChunks.getKey());
            }
        }
    }
}
