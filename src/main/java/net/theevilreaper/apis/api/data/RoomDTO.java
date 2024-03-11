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

    /**
     * Constructs a new instance from the class with the given values.
     * @param roomData the instance to a {@link RoomData}
     * @param schematicPath the path to a schematic file
     * @param regionPath the path to a region file
     */
    public RoomDTO(@NotNull RoomData roomData, @NotNull Path schematicPath, @NotNull Path regionPath) {
        this.roomData = roomData;
        this.schematicPath = schematicPath;
        this.regionPath = regionPath;
    }

    /**
     * Returns the path to the region file.
     * @return the path reference
     */
    public Path getRegionPath() {
        return regionPath;
    }

    /**
     * Returns the path to the schematic file.
     * @return the path reference
     */
    public Path getSchematicPath() {
        return schematicPath;
    }

    /**
     * Returns the given {@link RoomData} reference.
     * @return the reference
     */
    public RoomData getRoomData() {
        return roomData;
    }
}
