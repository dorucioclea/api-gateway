package com.t1t.digipolis.apim.rest.resources.exceptions;

/**
 * Thrown when a request is sent for a user who does not exist.
 */
public class UserNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = 8937297365588151067L;
    
    /**
     * Constructor.
     */
    public UserNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public UserNotFoundException(String message) {
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
        return ErrorCodes.USER_NOT_FOUND_INFO;
    }

}
