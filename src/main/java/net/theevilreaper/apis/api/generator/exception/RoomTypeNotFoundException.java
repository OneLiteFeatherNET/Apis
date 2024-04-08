package net.theevilreaper.apis.api.generator.exception;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public final class RoomTypeNotFoundException extends Exception {

    public RoomTypeNotFoundException(@NotNull String message) {
        super(message);
    }
}
