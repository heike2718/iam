package de.egladil.web.authprovider.error;

public class NewClientAuthException extends RuntimeException {

    /**
     * NewClientAuthException.
     *
     * @param message String
     */
    public NewClientAuthException(String message) {
        super(message);
    }

    /**
     * NewClientAuthException.
     *
     * @param message String
     * @param cause   Throwable
     */
    public NewClientAuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
