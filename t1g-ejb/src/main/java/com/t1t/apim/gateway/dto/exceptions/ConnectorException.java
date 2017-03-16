package com.t1t.apim.gateway.dto.exceptions;

/**
 * Exception thrown when attempting (but failing) to connect to a back end system.
 *
 */
public class ConnectorException extends AbstractEngineException {

    private static final long serialVersionUID = -3509254747425991797L;

    /**
     * Constructor.
     * @param message an error message
     */
    public ConnectorException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message an error message
     * @param cause the exception cause the root cause
     */
    public ConnectorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     * @param cause the exception cause the root cause
     */
    public ConnectorException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
