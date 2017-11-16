package com.t1t.apim.exceptions;

/**
 * Thrown when the user attempts some action on the application when it is
 * not in an appropriate state/status.
 */
public class InvalidApplicationStatusException extends AbstractUserException {

    private static final long serialVersionUID = -7586934307005501785L;

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public InvalidApplicationStatusException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.APP_STATUS_ERROR;
    }

    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_STATE;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.APP_STATUS_ERROR_INFO;
    }

}
