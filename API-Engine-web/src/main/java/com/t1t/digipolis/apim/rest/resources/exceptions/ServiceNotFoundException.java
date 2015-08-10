package com.t1t.digipolis.apim.rest.resources.exceptions;


/**
 * Thrown when trying to get, update, or delete an service that does not exist.
 */
public class ServiceNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -7689616296874695474L;

    /**
     * Constructor.
     */
    public ServiceNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public ServiceNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_NOT_FOUND_INFO;
    }

}
