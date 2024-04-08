package net.theevilreaper.apis.api.data;

import net.theevilreaper.apis.api.generator.exception.RoomTypeNotFoundException;
import org.jetbrains.annotations.NotNull;

/**
 * The enum contains all room types that currently exist.
 * The types provide a clear distinction as to which room is involved.
 * Logic can also be separated according to this
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 **/
public enum RoomType {

    NORMAL(0),
    DEAD(1),
    ITEM(2),
    SHOP(3),
    START(4),
    TELEPORT(5),
    BOSS(6),
    SECRET(7);

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
    public static @NotNull RoomType getRoomType(int id) throws RoomTypeNotFoundException {
        if (id < 0 || id > values.length) throw new RoomTypeNotFoundException("The room type with the id " + id + " does not exist");
        return values[id];
    }
}
