package net.theevilreaper.apis.api.util;

import net.minestom.server.instance.Chunk;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public final class PointPartCalculations {

    private PointPartCalculations() { }

    public static int calculateXPart(int oldStartRoomX, int startRoomX, int currentRoomX, int roomScale) {
        return oldStartRoomX + ((startRoomX - currentRoomX) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
    }

    public static int calculateZPart(int oldStartRoomZ, int startRoomZ, int currentRoomZ, int roomScale) {
        return oldStartRoomZ + ((startRoomZ -currentRoomZ) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
    }
}
