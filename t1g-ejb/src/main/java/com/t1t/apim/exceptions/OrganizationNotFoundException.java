package com.t1t.apim.exceptions;


/**
 * Thrown when trying to get, update, or remove an organization that does not exist.
 */
public class OrganizationNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -6377298317341796900L;

    /**
     * Constructor.
     */
    public OrganizationNotFoundException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public OrganizationNotFoundException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.ORG_NOT_FOUND;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.ORG_NOT_FOUND_INFO;
    }

}
