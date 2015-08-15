package com.t1t.digipolis.apim.exceptions;

/**
 * Base class for any exception that indicates "invalid input".
 *
 */
public abstract class AbstractInvalidInputException extends AbstractUserException {

    private static final long serialVersionUID = -8851909147205592784L;

    /**
     * Constructor.
     */
    public AbstractInvalidInputException() {
    }

    /**
     * Constructor.
     * @param message the exception message
     */
    public AbstractInvalidInputException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param cause the exception cause the exception cause
     */
    public AbstractInvalidInputException(Throwable cause) {
        super(cause);
    }
    
    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public final int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT;
    }

}
