package net.theevilreaper.apis.api.generator.functional;

/**
 * The interface can be implemented to define the calculation process for the positions which are needed to place
 * a room correctly into the world.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@FunctionalInterface
public interface OriginPointPartCalculation {

   /**
    * Returns an integer which is used to determine the right room position.
    * @param oldStartRoom the old start coordinate
    * @param startPoint the position coordinate from the startRoom
    * @param currentPoint the current position
    * @param roomScale the scale of a room
    * @return the calculated part of a position
    */
   int calculatePointPart(int oldStartRoom, int startPoint, int currentPoint, int roomScale);

}
