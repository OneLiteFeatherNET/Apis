package net.theevilreaper.apis.api.generator;

import net.hollowcube.util.schem.Rotation;
import net.hollowcube.util.schem.SchematicReader;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.theevilreaper.apis.api.BaseGenerator;
import net.theevilreaper.apis.api.data.RoomDTO;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.data.RoomType;
import net.theevilreaper.apis.api.loader.RoomSchematicLoader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
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
        this.dtos.clear();
        var regions = this.roomSchematicLoader.findRegions();
        if (regions.isEmpty()) {
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
        // -100 * 4 = 400
        // 16 * 4 = -100 +

/*
        var endX = (startPos.chunkX() * 16) + (roomScale);
        var endZ = (startPos.chunkZ() * 16) + (16 * roomScale);
        var endPos = new Pos(endX, startPos.blockY(), endZ);*/

        //V(1, 150, 1) -> endX = 1 + (4-1) = 4

        var endX = startPos.blockX() + (16 * roomScale) - 1;
        var endZ = startPos.blockZ() + (16 * roomScale) - 1;
        var endPos = new Vec(endX, startPos.blockY(), endZ + 1);


        if (!dtos.isEmpty()) {
            int oldStartRoomX = 0;
            int oldStartRoomZ = 0;

            //Pos playerPosition = Pos.fromPoint(startPos);
            Chunk startChunk = instance.getChunk(endPos.chunkX(), endPos.chunkZ());
            generatorLogger.info("New Start Room ({}, {})", startPos.chunkX(), startPos.chunkZ());
            for (RoomDTO room : dtos) {
                if (room.getRoomData().type().equals(RoomType.START_ROOM)) {
                    buildRoom(endPos, room.getSchematicPath()); //Why endPos?
                    oldStartRoomX = room.getRoomData().x();
                    oldStartRoomZ = room.getRoomData().z();
                }
            }

            for (RoomDTO dto : dtos) {
                if (dto.getRoomData().type() != RoomType.START_ROOM) {
                    int chunkX = dto.getRoomData().x() - (oldStartRoomX - startChunk.getChunkX());
                    chunkX += (chunkX - startChunk.getChunkX()) * (roomScale - 1);

                    int chunkZ = dto.getRoomData().z() - (oldStartRoomZ - startChunk.getChunkZ());
                    chunkZ += (chunkZ - startChunk.getChunkZ()) * (roomScale - 1);

                    var newVec = new Vec(((endPos.blockX()) + (double) ((Chunk.CHUNK_SIZE_X) * chunkX)), startPos.blockY(), (endPos.blockZ()) + (double) (Chunk.CHUNK_SIZE_Z * chunkZ) + 1);

                    if (newVec.blockX() % 64 != 0 || newVec.blockZ() % 64 != 0) {
                        generatorLogger.info("ChunkX is {}", chunkX);
                        generatorLogger.info("ChunkZ is {}", chunkZ);
                        generatorLogger.info("PosX is {}", startPos.blockX() + (16 * chunkX));
                        generatorLogger.info("PosZ is {}", startPos.blockZ() + (16 * chunkZ));
                    }

                    buildRoom(newVec, dto.getSchematicPath());
                }
            }
        }
    }

    private void buildRoom(@NotNull Point position, @NotNull Path schematicPath) {
        var schematic = SchematicReader.read(schematicPath);
        schematic.build(Rotation.NONE, null).apply(instance, position, () -> {
            generatorLogger.info("Schematic successfully placed");
        });

    }

    /**
     * Implementation of the save method to save a dungeon into the world folder.
     */
    @Override
    public void save() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
