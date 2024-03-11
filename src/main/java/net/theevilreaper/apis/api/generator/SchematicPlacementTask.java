package net.theevilreaper.apis.api.generator;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Task;
import net.theevilreaper.apis.api.generator.functional.SchematicPlacement;
import net.theevilreaper.apis.api.generator.unit.RoomUnit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

/**
 * The task is used to place each schematic into the world to construct the layout.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@ApiStatus.Internal
public final class SchematicPlacementTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchematicPlacementTask.class);
    private final Instance instance;
    private final Queue<RoomUnit> roomQueue;
    private final SchematicPlacement schematicPlacement;
    private final Runnable callback;
    private Task task;

    /**
     * Creates a new reference from this task with given values.
     * @param instance the instance to place the schematic
     * @param units a {@link List} which contains {@link RoomUnit} objects for the placement
     * @param schematicPlacement an implementation of {@link SchematicPlacement} interface
     * @param callback additional code which should be executed at the end
     */
    public SchematicPlacementTask(@NotNull Instance instance, @NotNull List<RoomUnit> units, @NotNull SchematicPlacement schematicPlacement, @Nullable Runnable callback) {
        this.instance = instance;
        this.roomQueue = new ArrayDeque<>(units);
        this.schematicPlacement = schematicPlacement;
        this.task = MinecraftServer.getSchedulerManager().buildTask(this).repeat(500, ChronoUnit.MILLIS).schedule();
        this.callback = callback;
    }

    /**
     * The method handles the placement of each room.
     */
    @Override
    public void run() {
        if (this.roomQueue.isEmpty()) {
            this.task.cancel();
            this.task = null;
            LOGGER.info("Finished placement of all rooms");
            if (this.callback != null) {
               this.callback.run();
            }
            return;
        }

        var room = this.roomQueue.poll();
        this.schematicPlacement.place(this.instance, room.originPoint(), room.schematicPath());
    }
}
