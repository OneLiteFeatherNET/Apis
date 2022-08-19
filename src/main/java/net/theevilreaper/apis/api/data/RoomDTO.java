package net.theevilreaper.apis.api.data;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class RoomDTO {

    private final RoomData roomData;

    private final Path schematicPath;

    private final Path regionPath;

    public RoomDTO(@NotNull RoomData roomData, @NotNull Path schematicPath, @NotNull Path regionPath) {
        this.roomData = roomData;
        this.schematicPath = schematicPath;
        this.regionPath = regionPath;
    }

    public Path getRegionPath() {
        return regionPath;
    }

    public Path getSchematicPath() {
        return schematicPath;
    }

    public RoomData getRoomData() {
        return roomData;
    }
}
