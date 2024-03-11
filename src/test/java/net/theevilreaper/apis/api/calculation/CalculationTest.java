package net.theevilreaper.apis.api.calculation;

import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Test some position calculations")
class CalculationTest {

    static final Vec START_POS = new Vec(0);
    static final int ROOM_SCALE = 2;
    static final int CHUNK_SIZE = 16;

    @Test
    void testLineDungeonCalculation() {
        // Given dungeon layout
        // 0 1 2
        // - x - 0 <-- Start
        // - x - 1 <-- NORMAL
        // - x - 2 <-- END

        var normalStartPos = new Vec(START_POS.blockX() + ((ROOM_SCALE * CHUNK_SIZE)), START_POS.y(), START_POS.z());
        var endStartPos = new Vec(START_POS.blockX() + (2 * (ROOM_SCALE * CHUNK_SIZE)), START_POS.y(), START_POS.z());

        Assertions.assertEquals(new Vec(32, 0,0 ), normalStartPos);
        Assertions.assertEquals(new Vec(64, 0,0), endStartPos);
    }
}
