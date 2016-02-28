package com.t1t.digipolis.apim.exceptions;

/**
 * Thrown when a request is sent for a user who does not exist.
 */
public class UserCannotDeleteException extends AbstractNotFoundException {

    private static final long serialVersionUID = 8937297365588151067L;

    /**
     * Constructor.
     */
    public UserCannotDeleteException() {
    }

    /**
     * Constructor.
     * @param message the exception message
     */
    public UserCannotDeleteException(String message) {
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
