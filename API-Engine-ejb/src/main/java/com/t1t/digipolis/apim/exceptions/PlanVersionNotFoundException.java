package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when trying to get, update, or remove a plan version that does not
 * exist.
 *
 */
public class PlanVersionNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -6183358851293630631L;

    /**
     * Constructor.
     */
    public PlanVersionNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public PlanVersionNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLAN_VERSION_NOT_FOUND;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLAN_VERSION_NOT_FOUND_INFO;
    }

}
