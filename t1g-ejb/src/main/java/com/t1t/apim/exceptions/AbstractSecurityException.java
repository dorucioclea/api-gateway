package com.t1t.apim.exceptions;

/**
 * Base class for all system exceptions.  A system exception is one that happens
 * because something went wrong on the server.  Examples might include an error
 * connecting to a backend storage system, running out of memory, etc.
 */
public abstract class AbstractSecurityException extends AbstractRestException {

    private static final long serialVersionUID = 2309997591141385467L;

    /**
     * Constructor.
     */
    public AbstractSecurityException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public AbstractSecurityException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause the exception cause
     */
    public AbstractSecurityException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     * @param cause   the exception cause
     */
    public AbstractSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public final int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_UNAUTHORIZED;
    }
}
