package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
public class MetricsQueryTimeOutException extends AbstractSystemException {

    public MetricsQueryTimeOutException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_REQUEST_TIMEOUT;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.METRIC_TIMEOUT;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.METRIC_TIMEOUT_INFO;
    }
}