package com.t1t.apim.exceptions;

/**
 * Created by michallispashidis on 29/04/16.
 */
public class MailServiceException extends AbstractSystemException {
    public MailServiceException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_SYSTEM_ERROR;
    }

    @Override
    public int getErrorCode() {
        return -1;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}
