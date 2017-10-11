package com.t1t.apim.exceptions;

/**
 * Thrown when trying to create an Organization that already exists.
 */
public class OrganizationCannotBeDeletedException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -1444829046948798598L;

    /**
     * Constructor.
     */
    public OrganizationCannotBeDeletedException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public OrganizationCannotBeDeletedException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.ORG_CANNOT_BE_DELETED;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.ORG_CANNOT_BE_DELETED_INFO;
    }

}
