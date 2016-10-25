package com.t1t.digipolis.apim.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MaintenanceException extends AbstractSystemException {

    public MaintenanceException(String message) {
        super(message);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_UNAVAILABLE;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SYSTEM_MAINTENANCE;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SYSTEM_MAINTENANCE_INFO;
    }
}