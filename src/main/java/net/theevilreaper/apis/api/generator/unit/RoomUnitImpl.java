package net.theevilreaper.apis.api.generator.unit;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public record RoomUnitImpl(
        Path schematicPath,
        Vec originPoint,
        Map<Vec, Chunk> chunks
) implements RoomUnit {

}