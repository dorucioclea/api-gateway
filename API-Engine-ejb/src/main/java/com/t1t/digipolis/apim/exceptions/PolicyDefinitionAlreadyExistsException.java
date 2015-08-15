package com.t1t.digipolis.apim.exceptions;


/**
 * Thrown when trying to create a Policy Definition that already exists.
 */
public class PolicyDefinitionAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = -6073501704605940260L;

    /**
     * Constructor.
     */
    public PolicyDefinitionAlreadyExistsException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public PolicyDefinitionAlreadyExistsException(String message) {
        super(message);
    }
    
    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.POLICY_DEF_ALREADY_EXISTS;
    }
    
    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.POLICY_DEF_ALREADY_EXISTS_INFO;
    }

}
