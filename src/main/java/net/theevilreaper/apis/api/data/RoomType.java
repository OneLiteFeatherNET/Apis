package net.theevilreaper.apis.api.data;

/**
 * The enum contains all room types that currently exist.
 * The types provide a clear distinction as to which room is involved.
 * Logic can also be separated according to this
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 **/
public enum RoomType {

    NORMAL_ROOM(0),
    DEAD_END(1),
    ITEM_ROOM(2),
    SHOP_ROOM(3),
    START_ROOM(4),
    TELEPORT_ROOM(5),
    BOSS_ROOM(6);

    private final int id;
    private static final RoomType[] values = values();

    /**
     * Creates a new entry for the enum.
     * @param id the id for the room
     */
    RoomType(int id) {
        this.id = id;
    }

    /**
     * Returns the id from the room.
     * @return the given id
     */
    public int getId() {
        return id;
    }

    /**
     * Fetch a RoomType by a given id.
     * @param id the id to check
     * @return the given type if it exists, NORMAL_ROOM otherwise
     */
    public static RoomType getRoomType(int id) {
        return id > values.length ? NORMAL_ROOM : values[id];
    }
}
