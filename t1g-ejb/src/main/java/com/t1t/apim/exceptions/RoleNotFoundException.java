package com.t1t.apim.exceptions;


/**
 * Thrown when a request is sent for a role that does not exist.
 */
public class RoleNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -5416700206708610404L;

    /**
     * Constructor.
     */
    public RoleNotFoundException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public RoleNotFoundException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.ROLE_NOT_FOUND;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.ROLE_NOT_FOUND_INFO;
    }

}
