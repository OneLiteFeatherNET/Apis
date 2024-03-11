package net.theevilreaper.apis.api.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * The class represents an image of a room from the floor plan.
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 **/
public record RoomData(int x, int z, @NotNull RoomType type, DoorFace... doors) {

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
    public @NotNull String toString() {
        return "LoadedRoom{" +
                "x=" + x +
                ", z=" + z +
                ", type=" + type +
                '}';
    }

    /**
     * Returns true if the given type is {@link RoomType#START_ROOM}
     * @return true or false
     */
    public boolean isStart() {
        return this.type == RoomType.START_ROOM;
    }

    /**
     * Returns true if the given type is {@link RoomType#SHOP_ROOM}
     * @return true or false
     */
    public boolean isShop() {
        return this.type == RoomType.SHOP_ROOM;
    }

    /**
     * Returns true if the given type is {@link RoomType#BOSS_ROOM}
     * @return true or false
     */
    public boolean isBoss() {
        return this.type == RoomType.BOSS_ROOM;
    }

    /**
     * Returns true if the given type is {@link RoomType#TELEPORT_ROOM}
     * @return true or false
     */
    public boolean isTeleport() {
        return this.type == RoomType.TELEPORT_ROOM;
    }

    /**
     * Returns true if the given type is {@link RoomType#ITEM_ROOM}
     * @return true or false
     */
    public boolean isItem() {
        return this.type == RoomType.ITEM_ROOM;
    }
}