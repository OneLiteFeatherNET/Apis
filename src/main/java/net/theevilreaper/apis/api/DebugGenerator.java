package net.theevilreaper.apis.api;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.apis.api.data.LoadedRoom;
import net.theevilreaper.apis.api.data.RoomType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class DebugGenerator extends BaseGenerator {

    private static final Block START_ROOM = Block.ORANGE_CONCRETE;
    private static final Block NORMAL_ROOM = Block.GRAY_CONCRETE;
    private static final Block BOSS_ROOM = Block.RED_CONCRETE;
    private static final Block SHOP_ROOM = Block.YELLOW_CONCRETE;
    private static final Block ITEM_ROOM = Block.GREEN_CONCRETE;

    private static final int ROOM_SIZE = 4;

    public DebugGenerator(@NotNull String name, @NotNull Instance instance, @NotNull Path filePath) {
        super(name, instance, filePath);
        generatorLogger = LoggerFactory.getLogger(DebugGenerator.class);
    }

    @Override
    public void generate(@NotNull Point startPos) {
        if (!loadedRooms.isEmpty()) {
            int oldStartRoomX = -1;
            int oldStartRoomZ = -1;
            Pos playerPosition = Pos.fromPoint(startPos);
            System.out.println("New Start Room (" + playerPosition.chunkX() + ", " + playerPosition.chunkZ() + ")");
            Chunk startChunk = instance.getChunk(playerPosition.chunkX(), playerPosition.chunkZ());
            buildRoom(startChunk.getChunkX(), startChunk.getChunkZ(), START_ROOM, playerPosition.blockY());

            for (LoadedRoom room : loadedRooms) {
                if (room.type().equals(RoomType.START_ROOM)) {
                    oldStartRoomX = room.x();
                    oldStartRoomZ = room.z();
                }
            }
            System.out.println("Old Start Room (" + oldStartRoomX + ", " + oldStartRoomZ + ")");


            for (LoadedRoom room : loadedRooms) {
                if (room.type() != RoomType.START_ROOM) {
                    int chunkX = room.x() - (oldStartRoomX - startChunk.getChunkX());
                    chunkX +=  (chunkX - startChunk.getChunkX()) * (ROOM_SIZE - 1);
                    //chunkX *= (ROOM_SIZE - 1);

                    int chunkZ = room.z() - (oldStartRoomZ - startChunk.getChunkZ());
                    chunkZ += (chunkZ - startChunk.getChunkZ()) * (ROOM_SIZE - 1);
                    //chunkZ *= (ROOM_SIZE - 1);


                    buildRoom(chunkX, chunkZ, getBlock(room), playerPosition.blockY());

                    System.out.println("chunkX is " +   chunkX);
                    System.out.println("chunkZ is " + chunkZ);
                }
            }
        }
        generatorLogger.info("Finish");
    }

    private void buildRoom(int chunkX, int chunkZ, Block block, int y)
    {
        System.out.println("chunkX: " + chunkX + " chunkZ: " + chunkZ);
        ChunkBatch chunkBatch;
        Chunk currentChunk;

        for (int xOffset = 0; xOffset <= (ROOM_SIZE - 1); xOffset++) {
            for (int zOffset = 0; zOffset <= (ROOM_SIZE - 1); zOffset++) {
                //To add extra values (it just moves the whole dungeon)
                currentChunk = instance.getChunk(chunkX + xOffset, chunkZ + zOffset);

                if (currentChunk == null || !currentChunk.isLoaded()) {
                    instance.loadChunk(chunkX + xOffset, chunkZ + zOffset).thenAccept(chunk -> {
                        var batch  = new ChunkBatch();
                        for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                            for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                                batch.setBlock(x, y, z, block);
                            }
                        }
                        batch.apply(this.instance, chunk, null);
                    }).join();
                } else {
                    chunkBatch = new ChunkBatch();
                    for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                        for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                            chunkBatch.setBlock(x, y, z, block);
                        }
                    }
                    chunkBatch.apply(this.instance, currentChunk, null);
                }
            }
        }
    }

    private Block getBlock(@NotNull LoadedRoom loadedRoom) {
        if (loadedRoom.isStart()) {
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
        }
        return NORMAL_ROOM;
    }
}
