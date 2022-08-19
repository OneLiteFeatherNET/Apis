package net.theevilreaper.apis.api.data;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
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
    public @NotNull String toString() {
        return "LoadedRoom{" +
                "x=" + x +
                ", z=" + z +
                ", type=" + type +
                '}';
    }

    public boolean isStart() {
        return this.type == RoomType.START_ROOM;
    }

    public boolean isShop() {
        return this.type == RoomType.SHOP_ROOM;
    }

    public boolean isBoss() {
        return this.type == RoomType.BOSS_ROOM;
    }

    public boolean isItem() {
        return this.type == RoomType.ITEM_ROOM;
    }
}