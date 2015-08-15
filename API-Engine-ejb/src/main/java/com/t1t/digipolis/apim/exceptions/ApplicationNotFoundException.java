package com.t1t.digipolis.apim.exceptions;



/**
 * Thrown when trying to get, update, or remove an application that does not exist.
 *
 */
public class ApplicationNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = 2885339519882791087L;

    /**
     * Constructor.
     */
    public ApplicationNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the message
     */
    public ApplicationNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.APP_NOT_FOUND;
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.APP_NOT_FOUND_INFO;
    }

}
