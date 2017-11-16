package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class JwtInvalidException extends AbstractSecurityException {

    public JwtInvalidException(String message) {
        super(message);
    }

    public JwtInvalidException() {
        super();
    }

    public JwtInvalidException(String message, Throwable cause) {
        super(message, cause);
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