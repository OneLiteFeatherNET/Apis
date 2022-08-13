package net.theevilreaper.apis.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minestom.server.instance.Instance;
import net.theevilreaper.apis.api.data.DoorFace;
import net.theevilreaper.apis.api.data.LoadedRoom;
import net.theevilreaper.apis.api.data.RoomType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static net.theevilreaper.apis.api.Constants.*;

public abstract class BaseGenerator implements DungeonGenerator {

    protected static Logger generatorLogger = LoggerFactory.getLogger(BaseGenerator.class);

    protected final Instance instance;

    protected final Path filePath;

    protected LoadedRoom[][] floorPlan;

    protected LoadedRoom startRoom;

    protected List<LoadedRoom> loadedRooms;

    private final String name;

    protected BaseGenerator(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath) {
        this.name = name;
        this.instance = instance;
        this.filePath = filePath;
        this.loadedRooms = new ArrayList<>();
    }

    public void loadData() {
        if (!Files.exists(filePath)) {
            throw new NullPointerException("The given path does not exist");
        }

        try {
            JsonObject entry = GSON.fromJson(new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8), JsonObject.class);

            if (!entry.has("height")) {
                throw new NullPointerException("The height attribute is missing");
            }

            var height = entry.get("height").getAsInt();

            if (!entry.has("width")) {
                throw new NullPointerException("The width attribute is missing");
            }

            var width = entry.get("width").getAsInt();

            this.floorPlan = new LoadedRoom[height][width];

            if (!entry.has("floor")) {
                throw new NullPointerException("The floor attribute is missing");
            }

            var floor = entry.get("floor").getAsJsonArray();

            if (floor.isEmpty()) {
                throw new NullPointerException("The floor can not be empty");
            }

            for (JsonElement jsonElement : floor) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();

                var x = asJsonObject.get(ROOM_X).getAsInt();
                var y = asJsonObject.get(ROOM_Y).getAsInt();

                var roomType = RoomType.getRoomType(asJsonObject.get(ROOM_TYPE).getAsInt());

                var doorArray = asJsonObject.get(ROOM_DOORS).getAsJsonArray();

                var doors = new DoorFace[doorArray.size()];

                int counter = 0;

                for (JsonElement element : doorArray) {
                    doors[counter] = DoorFace.getFace( element.getAsInt());
                    counter++;
                }

                var room = new LoadedRoom(x ,y, roomType, doors);

                if (startRoom == null) {
                    startRoom = room;
                }

                this.floorPlan[y][x] = room;
                this.loadedRooms.add(room);
            }
        } catch (IOException exception) {

        }
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public LoadedRoom[][] getFloorPlan() {
        return floorPlan;
    }
}
