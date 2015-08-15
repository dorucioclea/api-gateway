package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when trying to get, update, or remove a application version that does not
 * exist.
 *
 */
public class ApplicationVersionNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -7120965195175475680L;

    /**
     * Constructor.
     */
    public ApplicationVersionNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the message
     */
    public ApplicationVersionNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.APP_VERSION_NOT_FOUND;
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.APP_VERSION_NOT_FOUND_INFO;
    }

}
