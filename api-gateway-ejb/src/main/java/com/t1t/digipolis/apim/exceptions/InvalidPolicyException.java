package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class InvalidPolicyException extends AbstractInvalidInputException {

    public InvalidPolicyException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.POLICY_INVALID;
    }

    @Override
    public String getMoreInfoUrl() {
        return null;
    }
}