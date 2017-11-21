package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class JwtEncodingException extends AbstractSecurityException {

    public JwtEncodingException() {
    }

    public JwtEncodingException(String message) {
        super(message);
    }

    public JwtEncodingException(Throwable cause) {
        super(cause);
    }

    public JwtEncodingException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.JWT_ENCODING_FAILED;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.JWT_ENCODING_FAILED_INFO;
    }
}