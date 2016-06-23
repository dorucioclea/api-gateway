package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when trying to create an Application that already exists.
 *
 */
public class ApplicationAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = 3643381549889270663L;

    /**
     * Constructor.
     */
    public ApplicationAlreadyExistsException() {
    }
    
    /**
     * Constructor.
     * @param message the message
     */
    public ApplicationAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.APP_ALREADY_EXISTS;
    }
    
    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.APP_ALREADY_EXISTS_INFO;
    }

}
