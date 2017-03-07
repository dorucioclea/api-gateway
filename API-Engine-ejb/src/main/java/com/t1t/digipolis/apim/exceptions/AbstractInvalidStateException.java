package com.t1t.digipolis.apim.exceptions;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public abstract class AbstractInvalidStateException extends AbstractSystemException {

    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_INVALID_STATE;
    }

    public AbstractInvalidStateException() {
        super();
    }

    public AbstractInvalidStateException(String message) {
        super(message);
    }

    public AbstractInvalidStateException(Throwable cause) {
        super(cause);
    }
}