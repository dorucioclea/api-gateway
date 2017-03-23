package com.t1t.apim.exceptions;

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
        return ErrorCodes.HTTP_STATUS_CODE_UNAVAILABLE;
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