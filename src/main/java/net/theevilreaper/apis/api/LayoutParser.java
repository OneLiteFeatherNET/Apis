package net.theevilreaper.apis.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.theevilreaper.apis.api.data.DoorFace;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.data.RoomType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.theevilreaper.apis.api.util.Constants.ROOM_DOORS;
import static net.theevilreaper.apis.api.util.Constants.ROOM_TYPE;
import static net.theevilreaper.apis.api.util.Constants.ROOM_X;
import static net.theevilreaper.apis.api.util.Constants.ROOM_Y;

/**
 * The interface contains the main logic to parse a dungeon layout from a json file.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public sealed interface LayoutParser permits DungeonGenerator {

    /**
     * The method parses the dungeon layout from the provided {@link JsonArray} into segments that the generator can utilize for the generation process.
     * @param floor the {@link JsonArray} which contains the structure of a dungeon
     * @param roomData a {@link List} which contains all data about rooms
     * @param floorPlan the plan which represents the later the parsed floor as 2D array
     */
    default void parseLayout(@NotNull JsonArray floor, @NotNull List<RoomData> roomData, RoomData[][] floorPlan) {
        for (JsonElement jsonElement : floor) {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            var x = asJsonObject.get(ROOM_X).getAsInt();
            var y = asJsonObject.get(ROOM_Y).getAsInt();
            var roomType = RoomType.getRoomType(asJsonObject.get(ROOM_TYPE).getAsInt());
            var doorArray = asJsonObject.get(ROOM_DOORS).getAsJsonArray();

            if (doorArray == null) {
                throw new NullPointerException("A room must have at least one door");
            }

            if (doorArray.isEmpty() && roomType != RoomType.BOSS_ROOM) {
                throw new IllegalArgumentException("Only a boss rom can have zero doors");
            }

            var doors = new DoorFace[doorArray.size()];
            var counter = 0;

            for (JsonElement element : doorArray) {
                doors[counter] = DoorFace.getFace(element.getAsInt());
                counter++;
            }

            var room = new RoomData(x, y, roomType, doors);

            floorPlan[y][x] = room;
            roomData.add(room);
        }
    }
}
