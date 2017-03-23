package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class KeystoreNotFoundException extends AbstractNotFoundException {

    public KeystoreNotFoundException() {
    }

    public KeystoreNotFoundException(String message) {
        super(message);
    }

    public KeystoreNotFoundException(Throwable cause) {
        super(cause);
    }

    public KeystoreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.KEYSTORE_NOT_FOUND;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.KEYSTORE_NOT_FOUND_INFO;
    }
}