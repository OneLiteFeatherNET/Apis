package net.theevilreaper.apis.api.generator.exception;


/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class GeneratorGenerationException extends RuntimeException {

    public GeneratorGenerationException(String message) {
        super(message);
    }

    public GeneratorGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
