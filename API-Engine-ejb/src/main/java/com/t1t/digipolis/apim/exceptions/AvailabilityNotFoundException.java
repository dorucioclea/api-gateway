package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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