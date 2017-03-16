package com.t1t.apim.exceptions;

/**
 * Thrown when trying to get a member of an organization.
 *
 */
public class ExternalUserNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = 2785823316258685639L;

    /**
     * Constructor.
     */
    public ExternalUserNotFoundException() {
    }

    /**
     * Constructor.
     * @param message the exception message
     */
    public ExternalUserNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.MEMBER_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.MEMBER_NOT_FOUND_INFO;
    }

}
