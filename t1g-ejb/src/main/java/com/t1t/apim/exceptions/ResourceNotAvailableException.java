package com.t1t.apim.exceptions;


/**
 * Thrown when the user attempts to do or see something that they
 * are not authorized (do not have permission) to.
 *
 */
public class ResourceNotAvailableException extends AbstractUserException {

    private static final long serialVersionUID = 5447085523881661547L;

    /**
     * Constructor.
     * @param message the exception message
     */
    public ResourceNotAvailableException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getHttpCode()
     */
    @Override
    public int getHttpCode() {
        return ErrorCodes.HTTP_STATUS_CODE_UNAVAILABLE;
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return -1;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return null;
    }

}
