package net.theevilreaper.apis.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.theevilreaper.apis.api.data.LoadedRoom;
import org.jetbrains.annotations.NotNull;

/**
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 */
public interface DungeonGenerator {
    Gson GSON = new GsonBuilder().create();

    int DEFAULT_ROOM_SIZE = 8;

    int DEFAULT_CHUNK_SCALE = 4;

    /**
     * Generates the dungeon to the given position.
     *
     * @param startPos the position to start the dungeon at.
     */
    void generate(@NotNull Point startPos);

    /**
     * Generates the dungeon to the given position.
     *
     * @param player the player to generate the dungeon for.
     */
    default void generate(@NotNull Player player) {
        this.generate(player.getPosition().asVec());
    }

    default void save() {
        throw new RuntimeException("Not implemented yet");
    }

    @NotNull String getName();

    @NotNull LoadedRoom[][] getFloorPlan();
}
