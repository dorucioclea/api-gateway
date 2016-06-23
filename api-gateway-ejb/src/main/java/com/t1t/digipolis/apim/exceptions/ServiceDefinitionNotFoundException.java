package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when trying to get, update, or remove a service version that does not
 * exist.
 */
public class ServiceDefinitionNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -3416638354483369172L;

    /**
     * Constructor.
     */
    public ServiceDefinitionNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public ServiceDefinitionNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.SERVICE_DEFINITION_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SERVICE_DEFINITION_NOT_FOUND_INFO;
    }

}
