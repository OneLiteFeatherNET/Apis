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
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@ApiStatus.Internal
public final class SchematicPlacementTask implements Runnable {

    private final Logger logger;
    private final Instance instance;
    private final Queue<RoomUnit> roomQueue;
    private final SchematicPlacement schematicPlacement;
    private final Runnable callback;
    private Task task;

    public SchematicPlacementTask(@NotNull Instance instance, @NotNull List<RoomUnit> units, @NotNull SchematicPlacement schematicPlacement, @Nullable Runnable callback) {
        this.logger = LoggerFactory.getLogger(SchematicPlacementTask.class);
        this.logger.info("CALLL");
        this.instance = instance;
        this.roomQueue = new ArrayDeque<>(units);
        this.schematicPlacement = schematicPlacement;
        this.task = MinecraftServer.getSchedulerManager().buildTask(this).repeat(500, ChronoUnit.MILLIS).schedule();
        this.callback = callback;
    }

    @Override
    public void run() {
        if (this.roomQueue.isEmpty()) {
            this.task.cancel();
            this.task = null;
            logger.info("Finished placement of all rooms");
            if (this.callback != null) {
               this.callback.run();
            }
            return;
        }

        var room = this.roomQueue.poll();
        this.schematicPlacement.place(this.instance, room.getOriginPoint(), room.getSchematicPath());
    }
}
