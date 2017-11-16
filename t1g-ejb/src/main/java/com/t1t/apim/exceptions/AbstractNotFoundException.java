package com.t1t.apim.exceptions;

/**
 * Base class for "not found" exceptions.
 */
public abstract class AbstractNotFoundException extends AbstractUserException {

    private static final long serialVersionUID = -196398343525920762L;

    /**
     * Constructor.
     */
    public AbstractNotFoundException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public AbstractNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause the exception cause
     */
    public AbstractNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     * @param cause   the exception cause
     */
    public AbstractNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public final int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_NOT_FOUND;
    }

}
