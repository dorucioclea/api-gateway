package com.t1t.apim.core.exceptions;

/**
 * Thrown if the plugin is invalid
 */
public class InvalidPluginException extends Exception {

    private static final long serialVersionUID = 8114322400739079970L;

    /**
     * Constructor.
     */
    public InvalidPluginException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public InvalidPluginException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause the exception cause cause of the exception
     */
    public InvalidPluginException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     * @param cause   the exception cause cause of the exception
     */
    public InvalidPluginException(String message, Throwable cause) {
        super(message, cause);
    }

}
