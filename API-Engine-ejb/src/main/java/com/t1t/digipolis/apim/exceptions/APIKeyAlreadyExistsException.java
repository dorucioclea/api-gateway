package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class APIKeyAlreadyExistsException extends AbstractAlreadyExistsException {

    public APIKeyAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.GATEWAY_API_KEY_ALREADY_EXISTS;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}