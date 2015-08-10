package com.t1t.digipolis.apim.rest.resources.exceptions;


/**
 * Thrown when trying to create an Service that already exists.
 */
public class ServiceAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -2001544127622583292L;

    /**
     * Constructor.
     */
    public ServiceAlreadyExistsException() {
    }

    /**
     * Constructor.
     * @param message the exception message
     */
    public ServiceAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_ALREADY_EXISTS;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_ALREADY_EXISTS_INFO;
    }

}
