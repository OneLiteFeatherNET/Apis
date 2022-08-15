package net.theevilreaper.apis.command;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.theevilreaper.apis.api.DebugGenerator;
import net.theevilreaper.apis.api.DungeonGenerator;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class DebugCommand extends Command {

    private final DungeonGenerator generate;

    public DebugCommand(@NotNull Instance instance, @NotNull Path path) {
        super("test", "t");
        this.generate = new DebugGenerator("Debug", instance, path);
        addSyntax(this::onGenerate);
    }

    private void onGenerate(@NotNull CommandSender sender, @NotNull CommandContext context) {
        var player = (Player) sender;
        this.generate.setInstance(player.getInstance());
        this.generate.generate(player);
    }
}
