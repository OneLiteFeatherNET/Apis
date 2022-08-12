package net.theevilreaper.apis.api;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseGenerator implements DungeonGenerator {

    protected static Logger LOGGER = LoggerFactory.getLogger(BaseGenerator.class);

    protected final Instance instance;

    protected final Path filePath;

    protected Object[][] floorPlan;

    protected Object startRoom;

    protected List<Object> loadedRooms;

    private final String name;

    protected BaseGenerator(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath) {
        this.name = name;
        this.instance = instance;
        this.filePath = filePath;
        this.loadedRooms = new ArrayList<>();
    }

    public abstract void loadData();

    @Override
    public void generate(@NotNull Point startPos) {

    }

    @Override
    public void generate(@NotNull Player player) {
        this.generate(player.getPosition().asVec());
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }
}
