package com.t1t.digipolis.apim.exceptions;

import org.apache.log4j.spi.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceBasepathAlreadyInUseException extends AbstractAlreadyExistsException {

    public ServiceBasepathAlreadyInUseException() {
    }

    public ServiceBasepathAlreadyInUseException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_BASEPATH_ALREADY_EXISTS;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}