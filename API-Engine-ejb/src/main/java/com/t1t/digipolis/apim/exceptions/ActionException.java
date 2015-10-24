package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when an action is performed but an error occurs during processing.
 *
 */
public class ActionException extends AbstractUserException {

    private static final long serialVersionUID = -5626995900681339688L;

    /**
     * Constructor.
     */
    public ActionException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public ActionException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message the exception message
     * @param cause the exception cause
     */
    public ActionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.ACTION_ERROR;
    }
    
    /**
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.ACTION_ERROR_INFO;
    }

    /**
     */
    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_SYSTEM_ERROR;
    }

}
