package net.theevilreaper.apis;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;
import net.theevilreaper.apis.command.DebugCommand;
import net.theevilreaper.apis.schematic.LegacyLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class GeneratorExtension extends Extension {

    private static final String LEGACY_FILE = "legacy.json";

    private final Logger extensionLogger;

    private Path floorPath = null;
    private Path schematicPath = null;

    public GeneratorExtension() {
        this.extensionLogger = LoggerFactory.getLogger(GeneratorExtension.class);
    }

    @Override
    public void initialize() {
        this.initFolder();
        new LegacyLookup(getDataDirectory().resolve(LEGACY_FILE));

        var debugProperty = Boolean.parseBoolean(System.getProperty("apis.debug"));

        if (debugProperty) {
            MinecraftServer.getCommandManager().register(new DebugCommand(floorPath, schematicPath));
        }
    }

    @Override
    public void terminate() {
        // Nothing to do here
    }

    private void initFolder() {
        if (!Files.exists(getDataDirectory())) {
            try {
                Files.createDirectories(getDataDirectory());
            } catch (IOException exception) {
                extensionLogger.warn("Unable to create data directory of the extension", exception);
            }
        }

        var legacyFilePath = getDataDirectory().resolve(LEGACY_FILE);

        if (!Files.exists(legacyFilePath)) {
            try {
                var resourceFile = getResource(LEGACY_FILE);
                if (resourceFile == null) {
                    return;
                }
                Files.copy(resourceFile, legacyFilePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                extensionLogger.warn("Unable to copy {} from the resource folder {}", LEGACY_FILE, exception);
            }
        }
    }
}
