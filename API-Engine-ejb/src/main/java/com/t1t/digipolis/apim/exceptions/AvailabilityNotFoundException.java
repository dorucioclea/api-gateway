package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class AvailabilityNotFoundException extends AbstractNotFoundException {
    @Override
    public int getErrorCode() {
        return ErrorCodes.SCOPE_NOT_FOUND;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}