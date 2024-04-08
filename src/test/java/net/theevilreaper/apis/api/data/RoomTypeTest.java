package net.theevilreaper.apis.api.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test room type enum handling")
class RoomTypeTest {

    @Test
    void testGetId() {
        assertNotEquals(0, RoomType.START.getId());
    }

    @Test
    void testGetName() {
        assertNotEquals("NORMAL", RoomType.ITEM.name());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 2, 6, 7})
    void testGetType(int id){
        assertDoesNotThrow(() -> assertNotNull(RoomType.getRoomType(id)));
    }
}

