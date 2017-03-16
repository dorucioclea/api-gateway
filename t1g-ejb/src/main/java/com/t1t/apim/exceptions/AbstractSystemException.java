package com.t1t.apim.exceptions;

/**
 * Base class for all system exceptions.  A system exception is one that happens
 * because something went wrong on the server.  Examples might include an error
 * connecting to a backend storage system, running out of memory, etc.
 *
 */
public abstract class AbstractSystemException extends AbstractRestException {

    private static final long serialVersionUID = 2309997591141385467L;

    /**
     * Constructor.
     */
    public AbstractSystemException() {
    }

    /**
     * Constructor.
     * @param message the exception message
     */
    public AbstractSystemException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param cause the exception cause
     */
    public AbstractSystemException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * @param message the exception message
     * @param cause the exception cause
     */
    public AbstractSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
