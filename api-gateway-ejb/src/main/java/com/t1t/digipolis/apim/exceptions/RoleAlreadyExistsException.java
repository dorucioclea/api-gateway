package com.t1t.digipolis.apim.exceptions;



/**
 * Thrown when trying to create a Role that already exists.
 */
public class RoleAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -688580326437962778L;

    /**
     * Constructor.
     */
    public RoleAlreadyExistsException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public RoleAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.ROLE_ALREADY_EXISTS;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.ROLE_ALREADY_EXISTS_INFO;
    }

}
