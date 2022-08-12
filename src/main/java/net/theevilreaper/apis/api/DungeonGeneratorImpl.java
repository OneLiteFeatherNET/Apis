package net.theevilreaper.apis.api;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class DungeonGeneratorImpl extends BaseGenerator {

    public DungeonGeneratorImpl(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath) {
        super(name, instance, filePath);
        LOGGER = LoggerFactory.getLogger(DungeonGeneratorImpl.class);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void generate(@NotNull Point startPos) {
        super.generate(startPos);
    }

    @Override
    public void generate(@NotNull Player player) {
        super.generate(player);
    }

    @Override
    public void save() {
        super.save();
    }
}
