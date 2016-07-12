package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceVersionNotAvailableException extends AbstractNotFoundException {

    public ServiceVersionNotAvailableException() {
    }

    public ServiceVersionNotAvailableException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_VERSION_NOT_AVAILABLE;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_VERSION_NOT_AVAILABLE_INFO;
    }
}