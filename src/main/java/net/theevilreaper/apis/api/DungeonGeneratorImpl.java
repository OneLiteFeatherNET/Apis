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

/**
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public class DungeonGeneratorImpl extends BaseGenerator {

    private final RoomSchematicLoader roomSchematicLoader;
    private final List<RoomDTO> dtos;

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
        if (regions == null || regions.isEmpty()) {
            throw new IllegalArgumentException("Found a floor which does not contains any schematics");
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

    @Override
    public void generate(@NotNull Point startPos) {
        var endX = startPos.blockX() * (roomScale - 1);
        var endZ = startPos.blockZ() * (roomScale - 1);
        var endPos = new Vec(endX, startPos.blockY(), endZ);

        if (!dtos.isEmpty()) {
            int oldStartRoomX = -1;
            int oldStartRoomZ = 1;
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
                    chunkX +=  (chunkX - startChunk.getChunkX()) * (roomScale - 1);

                    int chunkZ = dto.getRoomData().z() - (oldStartRoomZ - startChunk.getChunkZ());
                    chunkZ += (chunkZ - startChunk.getChunkZ()) * (roomScale - 1);

                    var newVec = new Vec(((startPos.blockX())+ (double)((Chunk.CHUNK_SIZE_X) * chunkX)), startPos.blockY(), (startPos.blockZ()) + (double)(Chunk.CHUNK_SIZE_Z * chunkZ));

                    if (newVec.blockX() % 64 != 0 || newVec.blockZ() % 64 != 0) {
                        generatorLogger.info("ChunkX is {}", chunkX);
                        generatorLogger.info("ChunkZ is {}", chunkZ);
                        generatorLogger.info("PosX is {}", startPos.blockX() + (16 * chunkX));
                        generatorLogger.info("PosZ is {}", startPos.blockZ() + (16 * chunkZ));
                    }

                    buildRoom(newVec, dto.getSchematicPath());
                   /* generatorLogger.info("ChunkX is {}", chunkX);
                    generatorLogger.info("ChunkZ is {}", chunkZ);
                    generatorLogger.info("PosX is {}", startPos.blockX() + (16 * chunkX));
                    generatorLogger.info("PosZ is {}", startPos.blockZ() + (16 * chunkZ));*/
                }
            }
        }
    }

    private void buildRoom(@NotNull Point position, @NotNull Path schematicPath) {
        try {
            var future = Scaffolding.fromPath(schematicPath);
            future.thenAccept(schematic -> schematic.build(instance, position).thenRun(() ->
                    generatorLogger.info("Schematic successfully placed")).join());
        } catch (IOException | NBTException exception) {
            generatorLogger.warn("Unable to generate the schematic", exception);
        }
    }

    /**
     * Implementation of the save method to save a dungeon into the world folder.
     */
    @Override
    public void save() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
