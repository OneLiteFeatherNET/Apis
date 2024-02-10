package net.theevilreaper.apis.api;

import net.theevilreaper.apis.api.util.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testRoomIDConstant() {
        assertSame("_id", Constants.ROOM_ID);
    }

    @Test
    void testRoomXConstant() {
        assertSame("_x", Constants.ROOM_X);
    }

    @Test
    void testRoomYConstant() {
        assertSame("_y", Constants.ROOM_Y);
    }

    @Test
    void testRoomDoorConstant() {
        assertSame("_doors", Constants.ROOM_DOORS);
    }

    @Test
    void testRoomTypeConstant() {
        assertSame("_type", Constants.ROOM_TYPE);
    }


}