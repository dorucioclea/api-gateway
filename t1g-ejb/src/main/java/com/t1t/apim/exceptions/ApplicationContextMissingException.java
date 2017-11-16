package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ApplicationContextMissingException extends AbstractSecurityException {

    public ApplicationContextMissingException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.MISSING_APP_CONTEXT_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.MISSING_APP_CONTEXT_INFO;
    }
}