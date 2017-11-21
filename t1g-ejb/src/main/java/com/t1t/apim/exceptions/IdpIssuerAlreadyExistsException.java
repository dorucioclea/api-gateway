package com.t1t.apim.exceptions;



/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IdpIssuerAlreadyExistsException extends AbstractAlreadyExistsException {

    public IdpIssuerAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ISSUER_ALREADY_EXISTS_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.ISSUER_ALREADY_EXISTS_INFO;
    }
}