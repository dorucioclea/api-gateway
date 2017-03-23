package com.t1t.apim.exceptions;



/**
 * Thrown when trying to get, update, or remove a plan that does not exist.
 *
 */
public class PlanNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = 6770692745475536788L;

    /**
     * Constructor.
     */
    public PlanNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message exception message
     */
    public PlanNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLAN_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLAN_NOT_FOUND_INFO;
    }

}
