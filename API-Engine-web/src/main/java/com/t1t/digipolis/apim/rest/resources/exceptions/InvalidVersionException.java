package com.t1t.digipolis.apim.rest.resources.exceptions;

/**
 * Thrown when the user attempts to create an entity with an invalid version.
 */
public class InvalidVersionException extends AbstractInvalidInputException {

    private static final long serialVersionUID = -3249257829332496342L;

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public InvalidVersionException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.VERSION_INVALID;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.VERSION_INVALID_INFO;
    }

}
