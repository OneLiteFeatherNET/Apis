package net.theevilreaper.apis.api.generator.functional;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * The interface can be used to handle unloaded chunks which can block the generation of a dungeon.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface ChunkHandling {

    /**
     * The method contains the logic to handle the chunks which are not loaded yet.
     * @param instance the instance to handle the chunks
     * @param chunkMap the map of chunks to handle
     */
    void handleChunks(@NotNull Instance instance, @NotNull Map<Vec, Chunk> chunkMap);
}
