package net.theevilreaper.apis.api.generator.functional;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * The interface can be used to bind the logic to place a schematic file into a {@link Instance} from the server.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@FunctionalInterface
public interface SchematicPlacement {

    /**
     * The method contains the logic to place a schematic onto a given {@link Instance}.
     * @param instance the instance to place the schematic into it
     * @param position the origin point to place the schematic
     * @param schematicPath the path to the schematic file
     */
    default void place(@NotNull Instance instance, @NotNull Point position, @NotNull Path schematicPath) {
        this.place(instance, position, schematicPath, null);
    }

    /**
     * The method contains the logic to place a schematic onto a given {@link Instance}.
     * @param instance the instance to place the schematic into it
     * @param position the origin point to place the schematic
     * @param schematicPath the path to the schematic file
     * @param callback a callback which can use triggered to execute after the placement
     */
    void place(@NotNull Instance instance, @NotNull Point position, @NotNull Path schematicPath, @Nullable Runnable callback);
}
