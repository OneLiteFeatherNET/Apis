package net.theevilreaper.apis.api.data;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public record RoomDTO(@NotNull RoomData roomData, @NotNull Path schematicPath, @NotNull Path regionPath) { }
