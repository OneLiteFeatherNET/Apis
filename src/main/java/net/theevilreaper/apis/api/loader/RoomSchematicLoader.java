package net.theevilreaper.apis.api.loader;

import net.theevilreaper.apis.api.data.RoomDTO;
import net.theevilreaper.apis.api.data.RoomData;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class RoomSchematicLoader {

    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\.");

    private static final String REGION_FILE = ".json";

    private static final String SCHEMATIC_FILE = ".schem";

    private final Path basePath;

    public RoomSchematicLoader(@NotNull Path basePath) {
        this.basePath = basePath;
    }

    public List<Path> findRegions() {
        try (Stream<Path> stream = Files.walk(basePath)) {
            return stream.filter(Files::isRegularFile).filter(this::isRegionFile).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Path> findSchematics() {
        try (Stream<Path> stream = Files.walk(basePath)) {
            return stream.filter(Files::isRegularFile).filter(this::isSchematicFile).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ice_boss_2.schem / json

    /**
     * ice_normal_
     */

    public Map<Path, Path> mapSchematicsByRegionsFiles(@NotNull List<Path> paths) {
        Map<Path, Path> schematics = new HashMap<>();
        for (Path path : paths) {
            Path absolutPath = path.toAbsolutePath().getParent();
            var fileName = SPLIT_PATTERN.split(path.getFileName().toString())[0];
            var schematicPath = absolutPath.resolve(fileName + SCHEMATIC_FILE);
            schematics.put(path, schematicPath);
        }
        return schematics;
    }

    public RoomDTO findByRoomData(RoomData roomData, Map<Path, Path> mappedFiles) throws IOException {
        RoomDTO result = null;
        for (Map.Entry<Path, Path> entry : mappedFiles.entrySet()) {
            Path key = entry.getKey();
            UserDefinedFileAttributeView view = Files.getFileAttributeView(key, UserDefinedFileAttributeView.class);
            ByteBuffer allocate = ByteBuffer.allocate(view.size("dungeon.roomtype"));
            view.read("dungeon.roomtype", allocate);
            allocate.flip();
            var roomType= Integer.parseInt(new String(allocate.array(), StandardCharsets.UTF_8));
            /*var roomRotation = Files.getAttribute(key, "RoomRotation");*/
            if (roomData.type().getId() == roomType) {
                result = new RoomDTO(roomData, entry.getValue(), key);
            }
        }
        return result;
    }

    private boolean isRegionFile(@NotNull Path path) {
        return path.toString().endsWith(REGION_FILE);
    }

    private boolean isSchematicFile(@NotNull Path path) {
        return path.toString().endsWith(SCHEMATIC_FILE);
    }
}
