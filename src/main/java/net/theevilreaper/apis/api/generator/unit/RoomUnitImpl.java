package net.theevilreaper.apis.api.generator.unit;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public record RoomUnitImpl(
        @NotNull Path schematicPath,
        @NotNull Vec originPoint,
        @NotNull Map<Vec, Chunk> chunks
) implements RoomUnit {

}