package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MaintenanceException extends AbstractSystemException {

    private Integer errorCode;

    public MaintenanceException(String message) {
        super(message);
    }

    public MaintenanceException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_UNAVAILABLE;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode == null ? ErrorCodes.SYSTEM_MAINTENANCE : this.errorCode;
    }

    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SYSTEM_MAINTENANCE_INFO;
    }
}