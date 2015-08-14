package com.t1t.digipolis.apim.rest.resources.exceptions;


/**
 * Thrown when trying to get, update, or remove a service version that does not
 * exist.
 */
public class ServiceVersionNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -7120965195175475680L;

    /**
     * Constructor.
     */
    public ServiceVersionNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public ServiceVersionNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_VERSION_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_VERSION_NOT_FOUND_INFO;
    }

}
