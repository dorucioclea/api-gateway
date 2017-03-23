package com.t1t.apim.exceptions;

/**
 * Created by michallispashidis on 31/07/16.
 */
public class JWTPubKeyException extends AbstractRestException {

    public JWTPubKeyException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_SYSTEM_ERROR;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.JWT_PUB_KEY_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.JWT_PUB_KEY_INFO;
    }
}
