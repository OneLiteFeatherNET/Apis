package net.theevilreaper.apis.api.event;

import net.minestom.server.event.Event;
import net.theevilreaper.apis.api.generator.unit.RoomUnit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * The event will be triggered when a generator instance has finished the placement of all rooms from the layout.
 * @param name the name from teh generator
 * @param roomUnits a {@link List} which contains all data from each {@link RoomUnit} which was involved
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public record GeneratorFinishEvent(@NotNull String name, @NotNull List<RoomUnit> roomUnits) implements Event { }
