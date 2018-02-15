package com.t1t.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
public class DomainInUseException extends AbstractInvalidStateException {

    public DomainInUseException() {
    }

    public DomainInUseException(String message) {
        super(message);
    }

    public DomainInUseException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.ERROR_DOMAIN_ALREADY_IN_USE;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.INFO_DOMAIN_ALREADY_IN_USE;
    }
}