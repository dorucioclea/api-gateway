package com.t1t.apim.exceptions;

/**
 * Base class for all user exceptions.  A user exception happens when the user
 * does something that is problematic, such as try to create an Organization
 * that already exists.
 *
 */
public abstract class AbstractUserException extends AbstractRestException {

    private static final long serialVersionUID = 8254519224298006332L;

    /**
     * Constructor.
     */
    public AbstractUserException() {
    }

    /**
     * Constructor.
     * @param message the exception message
     */
    public AbstractUserException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param cause the exception cause
     */
    public AbstractUserException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * @param message the exception message
     * @param cause the exception cause
     */
    public AbstractUserException(String message, Throwable cause) {
        super(message, cause);
    }

}
