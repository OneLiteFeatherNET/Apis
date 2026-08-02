package net.theevilreaper.apis.api.generator.debug;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.apis.api.BaseGenerator;
import net.theevilreaper.apis.api.data.RoomData;
import net.theevilreaper.apis.api.data.RoomType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"java:S3252"})
public final class DebugGenerator extends BaseGenerator {

    private static final Block START_ROOM = Block.ORANGE_CONCRETE;
    private static final Block NORMAL_ROOM = Block.GRAY_CONCRETE;
    private static final Block BOSS_ROOM = Block.RED_CONCRETE;
    private static final Block SHOP_ROOM = Block.YELLOW_CONCRETE;
    private static final Block ITEM_ROOM = Block.GREEN_CONCRETE;

    public DebugGenerator(@NotNull Path filePath) {
        super("Debug", filePath);
        generatorLogger = LoggerFactory.getLogger(DebugGenerator.class);
    }

    @Override
    public void generate(@NotNull Point startPos) {
        if (!roomData.isEmpty()) {
            int oldStartRoomX = -1;
            int oldStartRoomZ = -1;
            Pos playerPosition = startPos.asPos();
            generatorLogger.debug("New Start Room ({}, {})", playerPosition.chunkX(), playerPosition.chunkZ());
            Chunk startChunk = instance.getChunk(playerPosition.chunkX(), playerPosition.chunkZ());
            buildRoom(startChunk.getChunkX(), startChunk.getChunkZ(), START_ROOM, playerPosition.blockY());

            for (RoomData room : roomData) {
                if (room.type().equals(RoomType.START)) {
                    oldStartRoomX = room.x();
                    oldStartRoomZ = room.z();
                }
            }

            generatorLogger.debug("Old Start Room ({}, {})", oldStartRoomX, oldStartRoomZ);

            for (RoomData room : roomData) {
                if (room.type() != RoomType.START) {
                    int chunkX = room.x() - (oldStartRoomX - startChunk.getChunkX());
                    chunkX +=  (chunkX - startChunk.getChunkX()) * (roomScale - 1);

                    int chunkZ = room.z() - (oldStartRoomZ - startChunk.getChunkZ());
                    chunkZ += (chunkZ - startChunk.getChunkZ()) * (roomScale - 1);

                    buildRoom(chunkX, chunkZ, getBlock(room), playerPosition.blockY());
                    generatorLogger.debug("ChunkX is {}", chunkX);
                    generatorLogger.debug("ChunkZ is {}", chunkZ);
                }
            }
        }
        generatorLogger.debug("Finished debug generation");
    }

    private void buildRoom(int chunkX, int chunkZ, Block block, int y) {
        generatorLogger.debug("chunkX: {}, chunkZ: {}", chunkX, chunkZ);
        Chunk currentChunk;
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int xOffset = 0; xOffset <= (roomScale - 1); xOffset++) {
            for (int zOffset = 0; zOffset <= (roomScale - 1); zOffset++) {
                currentChunk = instance.getChunk(chunkX + xOffset, chunkZ + zOffset);
                if (currentChunk == null || !currentChunk.isLoaded()) {
                    futures.add(instance.loadChunk(chunkX + xOffset, chunkZ + zOffset)
                            .thenAccept(chunk -> this.createChunkBatch(chunk, block, y)));
                } else {
                    this.createChunkBatch(currentChunk, block, y);
                }
            }
        }
        
        if (!futures.isEmpty()) {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }

    /**
     * Places a block into a given chunk.
     * @param chunk the chunk to place the block
     * @param block the block which should be placed
     * @param y the y height for the generation
     */
    private void createChunkBatch(@NotNull Chunk chunk, @NotNull Block block, int y) {
        var batch  = new ChunkBatch();
        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                batch.setBlock(x, y, z, block);
            }
        }
        batch.apply(this.instance, chunk, null);
    }

    private Block getBlock(@NotNull RoomData loadedRoom) {
        return switch (loadedRoom.type()) {
            case START -> START_ROOM;
            case BOSS -> BOSS_ROOM;
            case SHOP -> SHOP_ROOM;
            case ITEM -> ITEM_ROOM;
            default -> NORMAL_ROOM;
        };
    }
}
