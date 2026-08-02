package net.theevilreaper.apis.api.data;

import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.Objects;

/**
 * The class represents an image of a room from the floor plan.
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 **/
public record RoomData(int x, int z, RoomType type, DoorFace... doors) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomData roomData = (RoomData) o;
        return x == roomData.x && z == roomData.z && type == roomData.type && Arrays.equals(doors, roomData.doors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(x, z, type);
        result = 31 * result + Arrays.hashCode(doors);
        return result;
    }

    @Contract(pure = true)
    @Override
    public String toString() {
        return "LoadedRoom{" +
                "x=" + x +
                ", z=" + z +
                ", type=" + type +
                '}';
    }

    /**
     * Checks if the room matches the specified room type.
     * @param expectedType the room type to check against
     * @return true if the room type matches the expected type, false otherwise
     */
    public boolean is(RoomType expectedType) {
        return this.type == expectedType;
    }
}