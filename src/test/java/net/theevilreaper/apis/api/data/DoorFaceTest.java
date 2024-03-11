package net.theevilreaper.apis.api.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test door face enum handling")
class DoorFaceTest {

    @Test
    void testGetId() {
        assertEquals(0, DoorFace.UP.getId());
    }

    @Test
    void testGetName() {
        assertEquals("UP", DoorFace.UP.getName());
    }

    @Test
    void testGetFace() {
        assertEquals(DoorFace.UP, DoorFace.getFace(0));
        assertThrowsExactly(ArrayIndexOutOfBoundsException.class, () -> DoorFace.getFace(4));
    }
}
