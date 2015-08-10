package com.t1t.digipolis.apim.rest.resources.exceptions;

/**
 * Thrown when the search criteria is not valid (when invoking any of the 
 * various search methods).
 *
 */
public class InvalidSearchCriteriaException extends AbstractInvalidInputException {

    private static final long serialVersionUID = -166126446625739289L;
    
    /**
     * Constructor.
     * 
     * @param message the exception message
     */
    public InvalidSearchCriteriaException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.SEARCH_CRITERIA_INVALID;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.SEARCH_CRITERIA_INVALID_INFO;
    }

}
