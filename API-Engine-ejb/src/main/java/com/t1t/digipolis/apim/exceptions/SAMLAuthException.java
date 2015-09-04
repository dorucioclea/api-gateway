package com.t1t.digipolis.apim.exceptions;

/**
 * Created by michallispashidis on 4/09/15.
 */
public class SAMLAuthException extends AbstractUserException {
    /**
     * Constructor.
     * @param message the exception message
     */
    public SAMLAuthException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_SYSTEM_ERROR;
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return -1;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}
