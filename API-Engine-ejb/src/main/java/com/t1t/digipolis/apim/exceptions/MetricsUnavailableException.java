package com.t1t.digipolis.apim.exceptions;

import org.hibernate.jdbc.ReturningWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsUnavailableException extends AbstractSystemException {

    public MetricsUnavailableException() {
        super();
    }

    public MetricsUnavailableException(String message) {
        super(message);
    }

    public MetricsUnavailableException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getHttpCode() {
        return 200;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.METRIC_UNAVAILABLE;
    }

    @Override
    public String getMoreInfoUrl() {
        return "";
    }
}