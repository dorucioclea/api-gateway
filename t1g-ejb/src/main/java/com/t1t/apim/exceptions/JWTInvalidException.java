package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class JWTInvalidException extends AbstractUserException {

    public JWTInvalidException(String message) {
        super(message);
    }

    public JWTInvalidException() {
        super();
    }

    public JWTInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_INPUT;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.JWT_INVALID;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.JWT_INVALID_INFO;
    }
}