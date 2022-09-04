package net.theevilreaper.apis.api.data;

import org.jetbrains.annotations.Nullable;

/**
 * The enum includes all possible directions where a room can have doors.
 * @author Joltra
 * @version 1.0.0
 * @since 1.0.0
 **/
public enum DoorFace {
    UP(0, "UP"),
    RIGHT(1, "RIGHT"),
    DOWN(2, "DOWN"),
    LEFT(3, "LEFT");

    private final int id;
    private final String name;
    private static final DoorFace[] VALUES = values();

    /**
     * Creates a reference from a DoorFace.
     * @param id the id for the door
     * @param name the name for the door
     */
    DoorFace(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Returns the id from the door.
     * @return the given id
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name from a door entry.
     * @return the give name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an enum entry from a door by his id.
     * @param id the id from the door
     * @return the fetched entry or null
     */
    @Nullable
    public static DoorFace getFace(int id) {
        return VALUES[id];
    }
}
