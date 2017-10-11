package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class TokenNotVerifiedException extends AbstractUserException {

    public TokenNotVerifiedException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_NOT_FOUND;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.PUB_KEY_RETRIEVAL_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PUB_KEY_RETRIEVAL_ERROR_INFO;
    }
}