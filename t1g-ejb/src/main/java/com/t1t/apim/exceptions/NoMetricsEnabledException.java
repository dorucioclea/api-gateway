package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2018
 */
public class NoMetricsEnabledException extends AbstractSystemException {

    public NoMetricsEnabledException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_NOT_FOUND;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.NO_METRICS_ENABLED;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.NO_METRICS_ENABLED_INFO;
    }
}