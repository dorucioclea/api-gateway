package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IdpIssuerNotFoundException extends AbstractNotFoundException {

    public IdpIssuerNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ISSUER_NOT_FOUND_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.ISSUER_NOT_FOUND_INFO;
    }
}