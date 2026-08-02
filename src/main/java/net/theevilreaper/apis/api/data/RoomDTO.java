package net.theevilreaper.apis.api.data;


import java.nio.file.Path;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public record RoomDTO(RoomData roomData, Path schematicPath, Path regionPath) { }
