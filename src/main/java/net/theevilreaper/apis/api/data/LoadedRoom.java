package net.theevilreaper.apis.api.data;

/**
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 **/
//TODO: Rework the class
public record LoadedRoom(int x, int z, RoomType type, DoorFace... doors) {

    public boolean isStart() {
        return this.type == RoomType.START_ROOM;
    }

    public boolean isShop() {
        return this.type == RoomType.SHOP_ROOM;
    }

    public boolean isBoss() {
        return this.type == RoomType.BOSS_ROOM;
    }

    @Override
    public String toString() {
        return "LoadedRoom{" +
                "x=" + x +
                ", z=" + z +
                ", type=" + type +
                '}';
    }

    public boolean isItem() {
        return this.type == RoomType.ITEM_ROOM;
    }
}