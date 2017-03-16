package com.t1t.apim.exceptions;

/**
 * Thrown when trying to create an Organization that already exists.
 *
 */
public class UserAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -1444829046948798598L;

    /**
     * Constructor.
     */
    public UserAlreadyExistsException() {
    }

    /**
     * Constructor.
     * @param message the exception message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.USER_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.USER_ALREADY_EXISTS_INFO;
    }

}
