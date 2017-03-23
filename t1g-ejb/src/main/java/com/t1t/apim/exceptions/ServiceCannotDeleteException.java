package com.t1t.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class ServiceCannotDeleteException extends AbstractRestException {

    public ServiceCannotDeleteException(String message) {
        super(message);
    }

    public ServiceCannotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceCannotDeleteException(Throwable cause) {
        super(cause);
    }

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_STATE;
    }

    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_STILL_HAS_CONTRACTS;
    }

    @Override
    public String getMoreInfoUrl() {
        return "";
    }
}