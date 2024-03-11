package net.theevilreaper.apis.api.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test room type enum handling")
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

