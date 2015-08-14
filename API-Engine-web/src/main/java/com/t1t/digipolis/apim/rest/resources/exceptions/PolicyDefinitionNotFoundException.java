package com.t1t.digipolis.apim.rest.resources.exceptions;


/**
 * Thrown when trying to get, update, or remove a policy definition that does not exist.
 */
public class PolicyDefinitionNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -2991993331373394098L;

    /**
     * Constructor.
     */
    public PolicyDefinitionNotFoundException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public PolicyDefinitionNotFoundException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.POLICY_DEF_NOT_FOUND;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.POLICY_DEF_NOT_FOUND_INFO;
    }

}
