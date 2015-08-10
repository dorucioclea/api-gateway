package com.t1t.digipolis.apim.rest.resources.exceptions;

/**
 * Base class for "already exists" exceptions.
 *
 */
public abstract class AbstractAlreadyExistsException extends AbstractUserException {
    
    private static final long serialVersionUID = 1345772129352225376L;

    /**
     * Constructor.
     */
    public AbstractAlreadyExistsException() {
    }

    /**
     * Constructor.
     * @param message the message
     */
    public AbstractAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public final int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_ALREADY_EXISTS;
    }

}
