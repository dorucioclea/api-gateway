package com.t1t.apim.exceptions;

/**
 * Thrown when trying to create an Organization that already exists.
 */
public class OrganizationAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -1444829046948798598L;

    /**
     * Constructor.
     */
    public OrganizationAlreadyExistsException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public OrganizationAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.ORG_ALREADY_EXISTS;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.ORG_ALREADY_EXISTS_INFO;
    }

}
