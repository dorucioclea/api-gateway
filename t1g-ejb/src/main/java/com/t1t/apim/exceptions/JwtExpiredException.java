package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class JwtExpiredException extends AbstractSecurityException {

    public JwtExpiredException() {
    }

    public JwtExpiredException(String message) {
        super(message);
    }

    public JwtExpiredException(Throwable cause) {
        super(cause);
    }

    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.JWT_EXPIRED_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.JWT_EXPIRED_INFO;
    }
}