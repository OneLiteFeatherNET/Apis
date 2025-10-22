package net.theevilreaper.apis.api.util;

import net.hollowcube.schem.Schematic;
import net.hollowcube.schem.reader.SchematicReader;
import net.hollowcube.schem.util.Rotation;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.theevilreaper.apis.api.generator.functional.SchematicPlacement;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The class contains a default implementation from the {@link SchematicPlacement} interface.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class RoomSchematicPlacement {

    private RoomSchematicPlacement() { }

    /**
     * Places a room from a schematic at a specified position in a given instance.
     *
     * @param instance the target instance where the room will be placed.
     * @param position the position where the room will be placed within the instance.
     * @param schematicPath he path to the schematic file containing the room's design.
     *
     * @throws NullPointerException if instance, position, or schematicPath is null.
     */
    public static void placeRoom(@NotNull Instance instance, @NotNull Point position, @NotNull Path schematicPath) {
        byte[] schematicContent = null;
        try {
            schematicContent = Files.readAllBytes(schematicPath);
            Schematic schematic = SchematicReader.detecting().read(schematicContent);
            schematic.createBatch(Rotation.NONE).apply(instance, position, () -> {});
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
