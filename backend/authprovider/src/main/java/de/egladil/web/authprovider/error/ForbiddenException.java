package de.egladil.web.authprovider.error;

/**
 * ForbiddenException.
 */
public class ForbiddenException extends RuntimeException {

    /**
     * ForbiddenException.
     *
     * @param message String
     */
    public ForbiddenException(String message) {
        super(message);
    }

    /**
     * ForbiddenException.
     *
     * @param message String
     * @param cause   Throwable
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

}
