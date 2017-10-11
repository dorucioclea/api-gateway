package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class UnauthorizedIssuerException extends AbstractUserException {

    public UnauthorizedIssuerException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_UNAUTHORIZED;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.UNAUTHORIZED_ISSUER;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }

}