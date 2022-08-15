package net.theevilreaper.apis.api;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DungeonGeneratorTest {

    @Test
    void testGson() {
        var jsonString = "{\"name\":\"DungeonGenerator\"}";
        assertEquals("DungeonGenerator",
                DungeonGenerator.GSON.fromJson(jsonString, JsonObject.class).get("name").getAsString());
    }

    @Test
    void testRoomSize() {
        assertEquals(8, DungeonGenerator.DEFAULT_ROOM_SIZE);
    }

    @Test
    void testChunkScale() {
        assertEquals(2, DungeonGenerator.DEFAULT_CHUNK_SCALE);
    }

    @Test
    void testName() {
        var generator = new DungeonGeneratorImpl("Isaac", null, null);
        assertNotEquals("DungeonGenerator", generator.getName());
    }

    @Test
    void testSave() {
        var generator = new DungeonGeneratorImpl("Isaac", null, null);
        var exception = assertThrows(RuntimeException.class, generator::save);
        assertEquals("Not implemented yet", exception.getMessage());
    }

}