package net.theevilreaper.apis.api;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class DebugGenerator extends BaseGenerator {

    private static final Block START_ROOM = Block.ORANGE_CONCRETE;
    private static final Block NORMAL_ROOM = Block.GRAY_CONCRETE;
    private static final Block BOSS_ROOM = Block.RED_CONCRETE;
    private static final Block SHOP_ROOM = Block.YELLOW_CONCRETE;
    private static final Block ITEM_ROOM = Block.GREEN_CONCRETE;

    public DebugGenerator(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath) {
        super(name, instance, filePath);
        LOGGER = LoggerFactory.getLogger(DebugGenerator.class);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void generate(@NotNull Point startPos) {

    }

    @Override
    public void generate(@NotNull Player player) {

    }

    private Block getBlock(@NotNull Object loadedRoom) {
        /*if (loadedRoom.isStart()) {
            return START_ROOM;
        }

        if (loadedRoom.isBoos()) {
            return BOSS_ROOM;
        }

        if (loadedRoom.isShop()) {
            return SHOP_ROOM;
        }

        if (loadedRoom.isItem()) {
            return ITEM_ROOM;
        }**/
        return NORMAL_ROOM;
    }
}
