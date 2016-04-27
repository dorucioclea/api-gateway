package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class InvalidEventStatusException extends AbstractInvalidInputException {

    public InvalidEventStatusException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return 0;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}