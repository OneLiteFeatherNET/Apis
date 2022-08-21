package net.theevilreaper.apis.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minestom.server.instance.Instance;
import net.theevilreaper.apis.api.data.DoorFace;
import net.theevilreaper.apis.api.data.RoomData;
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

/**
 * The class contains the base implementation of a dungeon generator.
 * This class implements to loading of a floor plan of a file
 * @author Jolta
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BaseGenerator implements DungeonGenerator {

    protected static Logger generatorLogger = LoggerFactory.getLogger(BaseGenerator.class);
    protected final Path filePath;
    protected Instance instance;
    protected RoomData[][] floorPlan;
    protected RoomData startRoom;
    protected List<RoomData> roomData;
    protected int roomScale = DEFAULT_ROOM_SIZE;
    private final String name;

    /**
     * Creates a new instance of the class with the given values.
     * @param name the name from the generator
     * @param instance the instance for the generator
     * @param filePath the path to the file
     */
    protected BaseGenerator(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath) {
        this.name = name;
        this.instance = instance;
        this.filePath = filePath;
        this.roomData = new ArrayList<>();
    }

    /**
     * Creates a new instance of the class with the given values.
     * @param name the name from the generator
     * @param filePath the path to the file
     */
    protected BaseGenerator(@NotNull String name, @NotNull Path filePath) {
        this.name = name;
        this.filePath = filePath;
        this.roomData = new ArrayList<>();
    }

    /**
     * Load a dungeon from a given file.
     */
    @Override
    public void loadData() {
        if (!Files.exists(filePath)) {
            throw new NullPointerException("The given path does not exist");
        }

        try (var reader = new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8)) {
            JsonObject entry = GSON.fromJson(reader, JsonObject.class);

            if (!entry.has("height")) {
                throw new NullPointerException("The height attribute is missing");
            }

            var height = entry.get("height").getAsInt();

            if (!entry.has("width")) {
                throw new NullPointerException("The width attribute is missing");
            }

            var width = entry.get("width").getAsInt();

            this.floorPlan = new RoomData[height][width];

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

                if (doorArray == null || doorArray.isEmpty()) {
                    throw new NullPointerException("A room must have at least one door");
                }

                var doors = new DoorFace[doorArray.size()];
                var counter = 0;

                for (JsonElement element : doorArray) {
                    doors[counter] = DoorFace.getFace( element.getAsInt());
                    counter++;
                }

                var room = new RoomData(x ,y, roomType, doors);

                if (startRoom == null) {
                    startRoom = room;
                }

                this.floorPlan[y][x] = room;
                this.roomData.add(room);
            }
        } catch (IOException exception) {
            generatorLogger.warn("An exception occurred while loading floor plan", exception);
        }
    }

    /**
     * Set the instance where the generator should generate the floor.
     * @param instance the instance to set
     */
    @Override
    public void setInstance(@NotNull Instance instance) {
        this.instance = instance;
    }

    /**
     * Set the new roomScale for the generation.
     * @param roomScale the scale to set
     */
    @Override
    public void setRoomScale(int roomScale) {
        if (roomScale > DEFAULT_CHUNK_SCALE) {
            throw new IllegalArgumentException("The given scale can not be higher than: " + DEFAULT_CHUNK_SCALE);
        }
        this.roomScale = roomScale;
    }

    /**
     * Returns the name for the generator.
     * @return the given name
     */
    @NotNull
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the array which includes all {@link RoomData} from the plan.
     * @return the given array
     */
    @NotNull
    @Override
    public RoomData[][] getFloorPlan() {
        return floorPlan;
    }
}
