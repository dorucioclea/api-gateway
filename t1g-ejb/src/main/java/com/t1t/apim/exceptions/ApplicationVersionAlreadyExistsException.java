package com.t1t.apim.exceptions;


/**
 * Thrown when trying to create an Application that already exists.
 *
 */
public class ApplicationVersionAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -7790690590728305808L;

    /**
     * Constructor.
     */
    public ApplicationVersionAlreadyExistsException() {
    }

    /**
     * Constructor.
     * @param message the message
     */
    public ApplicationVersionAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.APP_VERSION_ALREADY_EXISTS;
    }

    /**
     * @see io.apiman.manager.api.rest.contract.exceptions.AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.APP_VERSION_ALREADY_EXISTS_INFO;
    }

}
