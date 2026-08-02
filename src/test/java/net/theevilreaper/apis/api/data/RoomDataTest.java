package net.theevilreaper.apis.api.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomDataTest {

    @Test
    void testIsRoomType() {
        RoomData roomData = new RoomData(0, 0, RoomType.BOSS, new DoorFace[0]);
        assertTrue(roomData.is(RoomType.BOSS));
        assertFalse(roomData.is(RoomType.NORMAL));
    }
}
