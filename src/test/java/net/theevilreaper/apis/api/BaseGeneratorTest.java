package net.theevilreaper.apis.api;

import net.theevilreaper.apis.api.generator.DungeonGeneratorImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Test generator mapping")
class BaseGeneratorTest {

    private static final Stream<Arguments> INVALID_ARGUMENTS = Stream.of(
            Arguments.of(
                    "dungeon_failed_all.json",
                    NullPointerException.class,
                    "The height attribute is missing"
            ),
            Arguments.of(
                    "dungeon_miss_width.json",
                    NullPointerException.class,
                    "The width attribute is missing"
            ),
            Arguments.of(
                    "dungeon_miss_height.json",
                    NullPointerException.class,
                    "The floor attribute is missing"
            ),
            Arguments.of(
                    "dungeon_miss_room.json",
                    IllegalArgumentException.class,
                    "Only a boss rom can have zero doors"
            )
    );

    private static Stream<Arguments> getInvalidArguments() {
        return INVALID_ARGUMENTS;
    }

    private static @NotNull DungeonGenerator generator(@NotNull String file) {
        Path resourceDirectory = Paths.get("src","test","resources");
        var filePath = resourceDirectory.resolve(file);
        return new DungeonGeneratorImpl(filePath, null);
    }

    @ParameterizedTest(name = "Test invalid mapping for {0}")
    @MethodSource("getInvalidArguments")
    void testInvalidMapping(@NotNull String file, @NotNull Class<Exception> exceptionCLass, @NotNull String message) {
        var generator = generator(file);
        var exception = assertThrows(exceptionCLass, generator::loadData);
        assertEquals(message, exception.getMessage());
    }

    @Order(1)
    @Test
    void testFailWithoutPath() {
        var generator = new DungeonGeneratorImpl(Paths.get("bla"), null);
        var exception = assertThrows(NullPointerException.class, generator::loadData);
        assertSame(NullPointerException.class, exception.getClass());
        assertEquals("The given path does not exist", exception.getMessage());
    }
}