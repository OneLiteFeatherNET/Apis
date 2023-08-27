package net.theevilreaper.apis.api.generator.functional;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/
@FunctionalInterface
public interface SchematicPlacement {

    void place(@NotNull Instance instance, @NotNull Pos position, @NotNull Path schematicPath, @Nullable Runnable callback);
}
