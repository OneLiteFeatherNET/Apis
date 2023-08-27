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

    void handleChunks(@NotNull Instance instance, @NotNull Map<Vec, Chunk> chunkMap);
}
