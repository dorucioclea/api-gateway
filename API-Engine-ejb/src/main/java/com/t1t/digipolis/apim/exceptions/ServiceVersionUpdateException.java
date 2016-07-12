package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceVersionUpdateException extends AbstractInvalidInputException {

    public ServiceVersionUpdateException() {
    }

    public ServiceVersionUpdateException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_UPDATE_VALUES_INVALID;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_UPDATE_VALUES_INVALID_INFO;
    }
}