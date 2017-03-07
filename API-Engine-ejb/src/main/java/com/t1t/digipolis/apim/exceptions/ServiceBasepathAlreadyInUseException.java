package com.t1t.digipolis.apim.exceptions;

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