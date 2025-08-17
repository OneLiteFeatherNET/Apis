package net.theevilreaper.apis.api.util;

import net.minestom.server.instance.Chunk;

/**
 * The class contains default implementation from the {@link net.theevilreaper.apis.api.generator.functional.OriginPointPartCalculation}.
 * Each of the methods is used to determine the x and z coordinate for the room placement.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class PointPartCalculations {

    private PointPartCalculations() { }

    /**
     * Calculates the x coordinate for the room position.
     * @param oldStartRoomX the original starting x coordinate value
     * @param startRoomX the new starting x coordinate value
     * @param currentRoomX the current x coordinate value
     * @param roomScale a scaling factor for the room size
     * @return an integer value representing the adjusted x coordinate.
     */
    public static int calculateXPart(int oldStartRoomX, int startRoomX, int currentRoomX, int roomScale) {
        return oldStartRoomX + ((startRoomX - currentRoomX) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
    }

    /**
     * Calculates the z coordinate for the room position.
     * @param oldStartRoomZ the original starting z coordinate value
     * @param startRoomZ the new starting z coordinate value
     * @param currentRoomZ the current z coordinate value
     * @param roomScale a scaling factor for the room size
     * @return an integer value representing the adjusted z coordinate.
     */
    public static int calculateZPart(int oldStartRoomZ, int startRoomZ, int currentRoomZ, int roomScale) {
        return oldStartRoomZ + ((startRoomZ - currentRoomZ) * (roomScale * Chunk.CHUNK_SECTION_SIZE));
    }
}
