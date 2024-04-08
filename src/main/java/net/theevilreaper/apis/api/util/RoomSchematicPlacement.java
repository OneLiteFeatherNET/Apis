package net.theevilreaper.apis.api.util;

import net.hollowcube.util.schem.Rotation;
import net.hollowcube.util.schem.SchematicReader;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * The class contains a default implementation from the {@link net.theevilreaper.apis.api.generator.functional.SchematicPlacement} interface.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@ApiStatus.NonExtendable
public final class RoomSchematicPlacement {

    private RoomSchematicPlacement() { }

    /**
     * Places a room from a schematic at a specified position in a given instance.
     *
     * @param instance The target instance where the room will be placed.
     * @param position The position where the room will be placed within the instance.
     * @param schematicPath The path to the schematic file containing the room's design.
     * @param callback An optional callback to be executed after the room is placed. Can be null.
     *
     * @throws NullPointerException If instance, position, or schematicPath is null.
     */
    public static void placeRoom(@NotNull Instance instance, @NotNull Point position, @NotNull Path schematicPath, @Nullable Runnable callback) {
        var schematic = SchematicReader.read(schematicPath);
        schematic.build(Rotation.NONE, null).apply(instance, position, callback);
    }
}
