package com.t1t.apim.exceptions;


/**
 * Thrown when trying to get, update, or remove a plan that does not exist.
 */
public class PlanCannotBeDeletedException extends AbstractNotFoundException {

    private static final long serialVersionUID = 6770692745475536788L;

    /**
     * Constructor.
     */
    public PlanCannotBeDeletedException() {
    }

    /**
     * Constructor.
     *
     * @param message exception message
     */
    public PlanCannotBeDeletedException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.PLAN_CANNOT_BE_DELETED;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.PLAN_CANNOT_BE_DELETED_INFO;
    }

}
