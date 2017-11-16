package com.t1t.apim.exceptions;


/**
 * Thrown when trying to add a policy definition that is found to be invalid in some way.
 */
public class PolicyDefinitionInvalidException extends AbstractNotFoundException {

    private static final long serialVersionUID = -4260177488116158192L;

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public PolicyDefinitionInvalidException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.POLICY_DEF_INVALID;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.POLICY_DEF_INVALID_INFO;
    }

}
