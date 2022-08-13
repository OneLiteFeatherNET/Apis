package net.theevilreaper.apis.api.data;

/**
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

    DoorFace(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static DoorFace getFace(int id) {
        return VALUES[id];
    }
}
