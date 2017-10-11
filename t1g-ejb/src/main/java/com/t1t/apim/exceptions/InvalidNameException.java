package com.t1t.apim.exceptions;

/**
 * Thrown when the user attempts to create an entity with an invalid name.
 */
public class InvalidNameException extends AbstractInvalidInputException {

    private static final long serialVersionUID = 1318922157199658825L;

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public InvalidNameException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.NAME_INVALID;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.NAME_INVALID_INFO;
    }

}
