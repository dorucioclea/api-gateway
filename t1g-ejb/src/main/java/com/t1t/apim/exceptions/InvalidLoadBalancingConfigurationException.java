package com.t1t.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class InvalidLoadBalancingConfigurationException extends AbstractInvalidStateException {

    public InvalidLoadBalancingConfigurationException() {
    }

    public InvalidLoadBalancingConfigurationException(String message) {
        super(message);
    }

    public InvalidLoadBalancingConfigurationException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_LOAD_BALANCING_INVALID;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_LOAD_BALANCING_INVALID_INFO;
    }
}