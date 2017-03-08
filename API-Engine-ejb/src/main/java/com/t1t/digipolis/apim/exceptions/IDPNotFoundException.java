package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class IDPNotFoundException extends AbstractNotFoundException {

    public IDPNotFoundException() {
        super();
    }

    public IDPNotFoundException(String message) {
        super(message);
    }

    public IDPNotFoundException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.IDP_NOT_FOUND;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.IDP_NOT_FOUND_INFO;
    }
}