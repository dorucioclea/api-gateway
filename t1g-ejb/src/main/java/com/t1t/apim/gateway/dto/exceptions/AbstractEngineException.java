package com.t1t.apim.gateway.dto.exceptions;

/**
 * Base class for all engine exceptions.
 *
 */
public class AbstractEngineException extends RuntimeException {

    private static final long serialVersionUID = -1802150539023180027L;

    /**
     * Constructor.
     */
    public AbstractEngineException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public AbstractEngineException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param cause the exception cause
     */
    public AbstractEngineException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * @param message the exception message
     * @param cause the exception cause
     */
    public AbstractEngineException(String message, Throwable cause) {
        super(message, cause);
    }

}
