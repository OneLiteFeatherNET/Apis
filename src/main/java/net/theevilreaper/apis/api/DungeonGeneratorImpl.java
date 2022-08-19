package net.theevilreaper.apis.api;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.theevilreaper.apis.api.data.RoomDTO;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.data.RoomType;
import net.theevilreaper.apis.api.loader.RoomSchematicLoader;
import net.theevilreaper.apis.schematic.Scaffolding;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBTException;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DungeonGeneratorImpl extends BaseGenerator {

    private final RoomSchematicLoader roomSchematicLoader;
    private final List<RoomDTO> dtos;

    private static final int ROOM_SIZE = 4;

    public DungeonGeneratorImpl(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath, RoomSchematicLoader roomSchematicLoader) {
        super(name, instance, filePath);
        this.roomSchematicLoader = roomSchematicLoader;
        this.dtos = new ArrayList<>();
        generatorLogger = LoggerFactory.getLogger(DungeonGeneratorImpl.class);
    }

    public DungeonGeneratorImpl(@NotNull String name, @NotNull Path filePath, RoomSchematicLoader roomSchematicLoader) {
        super(name, filePath);
        this.roomSchematicLoader = roomSchematicLoader;
        this.dtos = new ArrayList<>();
        generatorLogger = LoggerFactory.getLogger(DungeonGeneratorImpl.class);
    }

    @Override
    public void loadData() {
        super.loadData();
        var regions = this.roomSchematicLoader.findRegions();
        var mapped = this.roomSchematicLoader.mapSchematicsByRegionsFiles(regions);
        for (RoomData roomDat : roomData) {
            try {
                dtos.add(this.roomSchematicLoader.findByRoomData(roomDat, mapped));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void generate(@NotNull Point startPos) {
        var endX = startPos.blockX() * DEFAULT_CHUNK_SCALE;
        var endZ = startPos.blockZ() * DEFAULT_CHUNK_SCALE;
        var endPos = new Vec(endX, startPos.blockY(), endZ);

        if (!dtos.isEmpty()) {
            int oldStartRoomX = -1;
            int oldStartRoomZ = -1;
            Pos playerPosition = Pos.fromPoint(startPos);
            Chunk startChunk = instance.getChunk(playerPosition.chunkX(), playerPosition.chunkZ());
            generatorLogger.info("New Start Room ({}, {})", startPos.chunkX(), startPos.chunkZ());
            for (RoomDTO room : dtos) {
                if (room.getRoomData().type().equals(RoomType.START_ROOM)) {
                    buildRoom(endPos, room.getSchematicPath());
                    oldStartRoomX = room.getRoomData().x();
                    oldStartRoomZ = room.getRoomData().z();
                }
            }
            for (RoomDTO dto : dtos) {
                if (dto.getRoomData().type() != RoomType.START_ROOM) {
                    int chunkX = dto.getRoomData().x() - (oldStartRoomX - startChunk.getChunkX());
                    chunkX +=  (chunkX - startChunk.getChunkX()) * (ROOM_SIZE - 1);
                    //chunkX *= (ROOM_SIZE - 1);

                    int chunkZ = dto.getRoomData().z() - (oldStartRoomZ - startChunk.getChunkZ());
                    chunkZ += (chunkZ - startChunk.getChunkZ()) * (ROOM_SIZE - 1);
                    //chunkZ *= (ROOM_SIZE - 1);

                    buildRoom(new Vec(startPos.blockX() + (16 * chunkX), startPos.blockY(), startPos.blockX() + (16 * chunkZ)), dto.getSchematicPath());
                    generatorLogger.info("ChunkX is {}", chunkX);
                    generatorLogger.info("ChunkZ is {}", chunkZ);
                    generatorLogger.info("PosX is {}", startPos.blockX() + (16 * chunkX));
                    generatorLogger.info("PosZ is {}", startPos.blockX() + (16 * chunkZ));
                }
            }
        }
    }

    private void buildRoom(@NotNull Point startPos, @NotNull Path schematic) {
        try {
            var schematicSchem = Scaffolding.fromPath(schematic);
            schematicSchem.thenAccept(yolo -> {
                yolo.build(instance, startPos).thenRun(() ->
                        generatorLogger.info("Schematic successfully placed")).join();
            });
        } catch (IOException | NBTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
