package com.t1t.digipolis.apim.exceptions;

/**
 * @author Maarten Casteels
 * @since 2016
 */
public class EncryptionFailedException extends AbstractSystemException {

    public EncryptionFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_SYSTEM_ERROR;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ENCRYPTION_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return "";
    }
}
