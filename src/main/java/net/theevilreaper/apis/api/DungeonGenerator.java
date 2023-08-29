package net.theevilreaper.apis.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import net.theevilreaper.apis.api.data.RoomData;
import org.jetbrains.annotations.NotNull;

/**
 * The class defines the basic structure of method for a {@link DungeonGenerator}.
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 */
public sealed interface DungeonGenerator extends LayoutParser permits BaseGenerator {
    Gson GSON = new GsonBuilder().create();

    int MAX_FLOOR_ID = 5;

    int DEFAULT_ROOM_SIZE = 4;

    int DEFAULT_CHUNK_SCALE = 2;

    int MAX_Y_HEIGHT = 180;

    /**
     * Loads the plan structure of a floor from a file.
     */
    void loadData();

    /**
     * Generates the dungeon to the given position.
     *
     * @param startPos the position to start the dungeon at.
     */
    void generate(@NotNull Point startPos);

    /**
     * Saves the dungeon into a world folder.
     * Please note that the method is not implemented!!!
     */
    default void save() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Set the room scale for the generation.
     *
     * @param scale the scale to set
     */
    void setRoomScale(int scale);

    /**
     * Set the instance for the generation of the dungeon.
     *
     * @param instance the instance object to set
     */
    void setInstance(@NotNull Instance instance);

    /**
     * Returns the name of the generator instance.
     *
     * @return the given name
     */
    @NotNull String getName();

    /**
     * Returns the array which contains the loaded floor plan.
     * @return the given array
     */
    @NotNull RoomData[][] getFloorPlan();
}
