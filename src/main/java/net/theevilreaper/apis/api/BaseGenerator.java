package net.theevilreaper.apis.api;

import com.google.gson.JsonObject;
import net.minestom.server.instance.Instance;
import net.minestom.server.utils.validate.Check;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.generator.functional.ChunkHandling;
import net.theevilreaper.apis.api.generator.functional.OriginPointPartCalculation;
import net.theevilreaper.apis.api.generator.functional.SchematicPlacement;
import net.theevilreaper.apis.api.util.GenerationChunkHandling;
import net.theevilreaper.apis.api.util.PointPartCalculations;
import net.theevilreaper.apis.api.util.RoomSchematicPlacement;
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

import static net.theevilreaper.apis.api.util.Constants.*;

/**
 * The class contains the base implementation of a dungeon generator.
 * This class implements to loading of a floor plan of a file
 * @author Jolta
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract non-sealed class BaseGenerator implements DungeonGenerator {

    protected static Logger generatorLogger = LoggerFactory.getLogger(BaseGenerator.class);
    protected final Path filePath;

    protected Instance instance;
    protected RoomData[][] floorPlan;
    protected List<RoomData> roomData;
    protected int roomScale = DEFAULT_ROOM_SIZE;
    private final String name;
    protected SchematicPlacement roomPlacement;
    protected OriginPointPartCalculation xPartCalculation;
    protected OriginPointPartCalculation zPartCalculation;
    protected ChunkHandling chunkHandling;

    /**
     * Creates a new instance of the class with the given values.
     * @param name the name from the generator
     * @param filePath the path to the file
     */
    protected BaseGenerator(@NotNull String name, @NotNull Path filePath) {
        this.name = name;
        this.filePath = filePath;
        this.roomData = new ArrayList<>();
        this.roomPlacement = RoomSchematicPlacement::placeRoom;
        this.xPartCalculation = PointPartCalculations::calculateXPart;
        this.zPartCalculation = PointPartCalculations::calculateZPart;
        this.chunkHandling = GenerationChunkHandling::handleGenerationChunkLoading;
    }

    /**
     * Load a dungeon from a given file.
     */
    @Override
    public void loadData() {
        if (Files.notExists(filePath)) {
            throw new NullPointerException("The given path does not exist");
        }

        try (var reader = new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8)) {
            JsonObject entry = GSON.fromJson(reader, JsonObject.class);

            if (!entry.has(HEIGHT)) {
                throw new NullPointerException("The height attribute is missing");
            }

            var height = entry.get(HEIGHT).getAsInt();

            if (!entry.has(WIDTH)) {
                throw new NullPointerException("The width attribute is missing");
            }

            var width = entry.get(WIDTH).getAsInt();

            this.floorPlan = new RoomData[height][width];

            if (!entry.has(FLOOR)) {
                throw new NullPointerException("The floor attribute is missing");
            }

            var floor = entry.get(FLOOR).getAsJsonObject().get("_rooms").getAsJsonArray();

            if (floor == null || floor.isEmpty()) {
                throw new NullPointerException("The floor can not be empty");
            }

            this.parseLayout(floor, this.roomData, this.floorPlan);
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
        Check.argCondition(roomScale % 2 != 0, "The scale must be within the range of a power of two");
        Check.argCondition(roomScale > DEFAULT_CHUNK_SCALE, "The given scale can not be higher than: " + DEFAULT_CHUNK_SCALE);
        this.roomScale = roomScale;
    }

    /**
     * Returns the name for the generator.
     * @return the given name
     */
    @Override
    public @NotNull String getName() {
        return name;
    }

    /**
     * Returns the array which includes all {@link RoomData} from the plan.
     * @return the given array
     */
    @Override
    public @NotNull RoomData[][] getFloorPlan() {
        return floorPlan;
    }
}
