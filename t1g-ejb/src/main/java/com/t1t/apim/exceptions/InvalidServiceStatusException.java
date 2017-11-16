package com.t1t.apim.exceptions;

/**
 * Thrown when the user attempts some action on the service when it is
 * not in an appropriate state/status.
 */
public class InvalidServiceStatusException extends AbstractUserException {

    private static final long serialVersionUID = -380215244728992680L;

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public InvalidServiceStatusException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_STATUS_ERROR;
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
        return ErrorCodes.SERVICE_STATUS_ERROR_INFO;
    }

}
