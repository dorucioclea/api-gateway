package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when trying to create an Service that already exists.
 */
public class AlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = 9210041761070127463L;

    /**
     * Constructor.
     */
    public AlreadyExistsException() {
    }

    /**
     * Constructor.
     * @param message the exception message
     */
    public AlreadyExistsException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_VERSION_ALREADY_EXISTS;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_VERSION_ALREADY_EXISTS_INFO;
    }

}
