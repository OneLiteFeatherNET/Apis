package net.theevilreaper.apis.api;

import net.theevilreaper.apis.api.util.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testRoomIDConstant() {
        assertSame("_Room__id", Constants.ROOM_ID);
    }

    @Test
    void testRoomXConstant() {
        assertSame("_Room__x", Constants.ROOM_X);
    }

    @Test
    void testRoomYConstant() {
        assertSame("_Room__y", Constants.ROOM_Y);
    }

    @Test
    void testRoomDoorConstant() {
        assertSame("doors", Constants.ROOM_DOORS);
    }

    @Test
    void testRoomTypeConstant() {
        assertSame("room_type", Constants.ROOM_TYPE);
    }


}