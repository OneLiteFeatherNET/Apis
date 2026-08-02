package net.theevilreaper.apis.api.generator.debug;

import net.theevilreaper.apis.api.BaseGenerator;
import net.theevilreaper.apis.api.data.RoomDTO;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.loader.RoomSchematicLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for debug generators that load schematics into DTOs.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class AbstractDebugGenerator extends BaseGenerator {

    protected final RoomSchematicLoader roomSchematicLoader;
    protected final List<RoomDTO> dtos;

    protected AbstractDebugGenerator(String name, Path filePath, RoomSchematicLoader roomSchematicLoader) {
        super(name, filePath);
        this.roomSchematicLoader = roomSchematicLoader;
        this.dtos = new ArrayList<>();
    }

    @Override
    public void loadData() {
        super.loadData();
        this.dtos.clear();
        var regions = this.roomSchematicLoader.findRegions();
        if (regions.isEmpty()) {
            throw new IllegalArgumentException("Found a floor which does not contain any schematics");
        }
        var mapped = this.roomSchematicLoader.mapSchematicsByRegionsFiles(regions);
        for (RoomData roomDat : roomData) {
            try {
                dtos.add(this.roomSchematicLoader.findByRoomData(roomDat, mapped));
            } catch (IOException exception) {
                generatorLogger.warn("Unable to add schematic into the dto list", exception);
            }
        }
    }
}
