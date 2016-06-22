package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when trying to create an Plan that already exists.
 */
public class PlanAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -4947377705681860671L;

    /**
     * Constructor.
     */
    public PlanAlreadyExistsException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public PlanAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLAN_ALREADY_EXISTS;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLAN_ALREADY_EXISTS_INFO;
    }

}
