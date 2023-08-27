package net.theevilreaper.apis.api.generator.exception;

import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class GeneratorGenerationException extends RuntimeException {

    public GeneratorGenerationException(@NotNull String message) {
        super(message);
    }

    public GeneratorGenerationException(@NotNull String message, @NotNull Throwable cause) {
        super(message, cause);
    }
}
