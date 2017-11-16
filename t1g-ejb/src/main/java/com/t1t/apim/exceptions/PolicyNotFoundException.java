package com.t1t.apim.exceptions;


/**
 * Thrown when trying to get, update, or remove a policy that does not exist.
 */
public class PolicyNotFoundException extends AbstractNotFoundException {

    private static final long serialVersionUID = -4168188371956258549L;

    /**
     * Constructor.
     */
    public PolicyNotFoundException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public PolicyNotFoundException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.POLICY_NOT_FOUND;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.POLICY_NOT_FOUND_INFO;
    }

}
