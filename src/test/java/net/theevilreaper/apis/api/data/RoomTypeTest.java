package net.theevilreaper.apis.api.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTypeTest {

    @Test
    void testGetId() {
        assertEquals(0, RoomType.NORMAL_ROOM.getId());
    }

    @Test
    void testGetName() {
        assertNotEquals("NORMAL", RoomType.ITEM_ROOM.name());
    }

    @Test
    void testGetType() {
       assertEquals(RoomType.TELEPORT_ROOM, RoomType.getRoomType(5));
       assertEquals(RoomType.NORMAL_ROOM, RoomType.getRoomType(69));
    }
}

