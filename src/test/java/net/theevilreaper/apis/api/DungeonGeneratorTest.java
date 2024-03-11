package net.theevilreaper.apis.api;

import com.google.gson.JsonObject;
import net.theevilreaper.apis.api.generator.DungeonGeneratorImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test the dungeon generator")
class DungeonGeneratorTest {

    @Test
    void testGson() {
        var jsonString = "{\"name\":\"DungeonGenerator\"}";
        assertEquals("DungeonGenerator",
                DungeonGenerator.GSON.fromJson(jsonString, JsonObject.class).get("name").getAsString());
    }

    @Test
    void testRoomSize() {
        assertEquals(4, DungeonGenerator.DEFAULT_ROOM_SIZE);
    }

    @Test
    void testChunkScale() {
        assertEquals(2, DungeonGenerator.DEFAULT_CHUNK_SCALE);
    }

    @Test
    void testName() {
        var generator = new DungeonGeneratorImpl( null, null);
        assertNotEquals("DungeonGenerator", generator.getName());
    }

    @Test
    void testScaleSet() {
        var generator = new DungeonGeneratorImpl( null, null);
        var exception = assertThrows(IllegalArgumentException.class, () -> generator.setRoomScale(12));
        assertSame("The given scale can not be higher than: " + DungeonGenerator.DEFAULT_CHUNK_SCALE, exception.getMessage());
    }
}