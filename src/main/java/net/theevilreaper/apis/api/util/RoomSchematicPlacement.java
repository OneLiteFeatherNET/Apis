package net.theevilreaper.apis.api.util;

import net.hollowcube.util.schem.Rotation;
import net.hollowcube.util.schem.SchematicReader;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public final class RoomSchematicPlacement {

    private RoomSchematicPlacement() { }

    public static void placeRoom(@NotNull Instance instance, @NotNull Point position, @NotNull Path schematicPath, @Nullable Runnable callback) {
        var schematic = SchematicReader.read(schematicPath);
        schematic.build(Rotation.NONE, null).apply(instance, position, callback);
    }
}
