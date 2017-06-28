package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class EventNotFoundException extends AbstractNotFoundException {
    @Override
    public int getErrorCode() {
        return ErrorCodes.EVENT_NOT_FOUND_ERROR;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}