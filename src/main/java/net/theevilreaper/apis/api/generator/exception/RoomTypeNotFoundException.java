package net.theevilreaper.apis.api.generator.exception;


/**
 * Exception which will be thrown if a room type is not found.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.1.0
 */
public final class RoomTypeNotFoundException extends RuntimeException {

    /**
     * Creates a new room type not found exception.
     *
     * @param message the exception message
     */
    public RoomTypeNotFoundException(String message) {
        super(message);
    }
}
