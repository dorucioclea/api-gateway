package com.t1t.apim.exceptions;


/**
 * Thrown when trying to create an Plan that already exists.
 */
public class PlanVersionAlreadyExistsException extends AbstractAlreadyExistsException {

    private static final long serialVersionUID = 4811955945896932570L;

    /**
     * Constructor.
     */
    public PlanVersionAlreadyExistsException() {
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public PlanVersionAlreadyExistsException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLAN_VERSION_ALREADY_EXISTS;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLAN_VERSION_ALREADY_EXISTS_INFO;
    }

}
