package net.theevilreaper.apis.api.generator.unit;

import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoomUnitBuilderTest {

    @Test
    void testFailWithoutSchematicPath() {
        var builder = new RoomUnitBuilder().setOriginPoint(Vec.ZERO);
        var exception = assertThrows(NullPointerException.class, builder::build);
        assertEquals("schematicPath cannot be null", exception.getMessage());
    }

    @Test
    void testFailWithoutOriginPoint() {
        var builder = new RoomUnitBuilder().setSchematicPath(Paths.get("test.schem"));
        var exception = assertThrows(NullPointerException.class, builder::build);
        assertEquals("originPoint cannot be null", exception.getMessage());
    }

    @Test
    void testSuccess() {
        var builder = new RoomUnitBuilder()
                .setSchematicPath(Paths.get("test.schem"))
                .setOriginPoint(Vec.ZERO);
        assertDoesNotThrow(() -> {
            var unit = builder.build();
            assertNotNull(unit);
            assertEquals(Vec.ZERO, unit.originPoint());
            assertNotNull(unit.schematicPath());
        });
    }
}
