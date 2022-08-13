package net.theevilreaper.apis.api;

import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class DungeonGeneratorImpl extends BaseGenerator {

    public DungeonGeneratorImpl(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath) {
        super(name, instance, filePath);
        generatorLogger = LoggerFactory.getLogger(DungeonGeneratorImpl.class);
    }

    @Override
    public void generate(@NotNull Point startPos) {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public void save() {
        super.save();
    }
}
