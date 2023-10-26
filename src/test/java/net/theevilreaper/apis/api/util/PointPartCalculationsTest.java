package net.theevilreaper.apis.api.util;

import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointPartCalculationsTest {

    static final int ROOM_SCALE = 2;

    static Vec START_POINT = Vec.ZERO;

    @Test
    void testXCalculation() {
        int newXPos = PointPartCalculations.calculateXPart(START_POINT.blockX(), 2, 3, ROOM_SCALE);
        assertEquals(-32, newXPos);
        var newVec = new Vec(newXPos, START_POINT.blockY(), START_POINT.blockZ());
        assertEquals(new Vec(-32, 0,0), newVec);
    }

    @Test
    void testZCalculation() {
        int newZPos = PointPartCalculations.calculateZPart(START_POINT.blockZ(), 3, 3, ROOM_SCALE);
        assertEquals(0, newZPos);
        var newVec = new Vec(START_POINT.blockX(), START_POINT.blockY(), newZPos);
        assertEquals(new Vec(0, 0,0), newVec);
    }

}