package net.theevilreaper.apis.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.theevilreaper.apis.api.DungeonGenerator;
import net.theevilreaper.apis.api.DungeonGeneratorImpl;
import net.theevilreaper.apis.api.data.RoomType;
import net.theevilreaper.apis.api.loader.RoomSchematicLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

import static net.theevilreaper.apis.api.Constants.*;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class DebugCommand extends Command {

    private final Argument<String> schematicArgument;

    private final ArgumentEnum<RoomType> roomArgument;
    private final ArgumentLiteral gen;

    private final DungeonGenerator generate;
    private final List<Path> schematics;

    public DebugCommand(@NotNull Path floorPlanPath, @NotNull Path path) {
        super("test", "t");
        RoomSchematicLoader roomSchematicLoader = new RoomSchematicLoader(path);
        this.schematics = roomSchematicLoader.findSchematics();
        this.gen = ArgumentType.Literal("gen");
        this.generate = new DungeonGeneratorImpl("Debug", floorPlanPath, roomSchematicLoader);
        this.schematicArgument = ArgumentType.String("schematic").setSuggestionCallback((sender, context, suggestion) -> {
            for (Path schematic : schematics) {
                suggestion.addEntry(new SuggestionEntry(schematic.getFileName().toString(), Component.text("Schematic", NamedTextColor.WHITE)));
            }
        });
        this.roomArgument = new ArgumentEnum<>("type", RoomType.class);

        addSyntax(this::gen, this.gen);
        addSyntax(this::onRegion, schematicArgument, roomArgument);
    }

    private void gen(@NotNull CommandSender commandSender, @NotNull CommandContext commandContext) {
        if (commandSender instanceof Player player) {
            this.generate.loadData();
            this.generate.generate(player);
        }
    }

    // /t <schemac> <type> -> name.schema | name.json + 1 Atrr
    private void onRegion(@NotNull CommandSender sender, @NotNull CommandContext context) {
        var schematic = context.get(schematicArgument);
        var type = context.get(roomArgument);
        Path schematicFinalPath = null;
        for (Path schematicPath : schematics) {
            if (schematicPath.getFileName().toString().equalsIgnoreCase(schematic)) {
                schematicFinalPath = schematicPath;
            }
        }
        if (schematicFinalPath != null) {
            Path parent = schematicFinalPath.toAbsolutePath().getParent();
            var regionFileName = schematicFinalPath.getFileName().toString().replace(SCHEMATIC_FILE, "");
            Path regionFile = parent.resolve(Paths.get(regionFileName, REGION_FILE));
            try {
                Files.createFile(regionFile);
                UserDefinedFileAttributeView view = Files.getFileAttributeView(regionFile, UserDefinedFileAttributeView.class);
                view.write("dungeon.roomtype", StandardCharsets.UTF_8.encode(String.valueOf(type.getId())));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
