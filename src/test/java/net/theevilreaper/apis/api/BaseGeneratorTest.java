package net.theevilreaper.apis.api;

import net.theevilreaper.apis.api.generator.DungeonGeneratorImpl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BaseGeneratorTest {

    @Order(1)
    @Test
    void testFailWithoutPath() {
        var generator = new DungeonGeneratorImpl(Paths.get("bla"), null);
        var exception = assertThrows(NullPointerException.class, generator::loadData);
        assertSame(NullPointerException.class, exception.getClass());
        assertEquals("The given path does not exist", exception.getMessage());
    }

    @Order(2)
    @Test
    void testMissingHeightValue() {
        Path resourceDirectory = Paths.get("src","test","resources");
        var filePath = resourceDirectory.resolve("dungeon_failed_all.json");

        var generator = new DungeonGeneratorImpl(filePath, null);

        var exception = assertThrows(NullPointerException.class, generator::loadData);
        assertEquals("The height attribute is missing", exception.getMessage());
    }

    @Order(3)
    @Test
    void testMissingWidthValue() {
        Path resourceDirectory = Paths.get("src","test","resources");
        var filePath = resourceDirectory.resolve("dungeon_miss_width.json");

        var generator = new DungeonGeneratorImpl(filePath, null);

        var exception = assertThrows(NullPointerException.class, generator::loadData);
        assertEquals("The width attribute is missing", exception.getMessage());
    }

    @Order(4)
    @Test
    void testMissingFloorValue() {
        Path resourceDirectory = Paths.get("src","test","resources");
        var filePath = resourceDirectory.resolve("dungeon_miss_height.json");

        var generator = new DungeonGeneratorImpl(filePath, null);

        var exception = assertThrows(NullPointerException.class, generator::loadData);
        assertEquals("The floor attribute is missing", exception.getMessage());
    }

    @Order(5)
    @Test
    void testMissingDoorValue() {
        Path resourceDirectory = Paths.get("src","test","resources");
        var filePath = resourceDirectory.resolve("dungeon_miss_room.json");

        var generator = new DungeonGeneratorImpl(filePath, null);

        var exception = assertThrows(IllegalArgumentException.class, generator::loadData);
        assertEquals("Only a boss rom can have zero doors", exception.getMessage());
    }
}