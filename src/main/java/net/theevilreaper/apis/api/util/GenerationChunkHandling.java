package net.theevilreaper.apis.api.util;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.chunk.ChunkUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Utility class for handling chunk loading during the room placement.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class GenerationChunkHandling {

    private GenerationChunkHandling() {}

    /**
     * Ensures that the chunks required for generation are loaded in the given instance.
     *
     * @param instance The instance in which chunks need to be loaded.
     * @param chunkMap A map containing positions (Vec) as keys and corresponding Chunk objects as values.
     *                 Chunks at these positions will be loaded if not already loaded.
     *
     * @throws NullPointerException If instance or chunkMap is null.
     */
    public static void handleGenerationChunkLoading(@NotNull Instance instance, @NotNull Map<Vec, Chunk> chunkMap) {
        if (chunkMap.isEmpty()) return;

        for (Map.Entry<Vec, Chunk> posChunks : chunkMap.entrySet()) {
            if (!ChunkUtils.isLoaded(posChunks.getValue())) {
                instance.loadChunk(posChunks.getKey());
            }
        }
    }
}
