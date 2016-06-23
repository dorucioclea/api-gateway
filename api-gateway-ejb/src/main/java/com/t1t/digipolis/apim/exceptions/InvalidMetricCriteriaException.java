package com.t1t.digipolis.apim.exceptions;

/**
 * Thrown when the metric criteria is not valid.  For example, when asking
 * for an invalid date range, or a date range and interval combination that
 * would result in too many items.
 */
public class InvalidMetricCriteriaException extends AbstractInvalidInputException {

    private static final long serialVersionUID = 1398262976721863828L;

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public InvalidMetricCriteriaException(String message) {
        super(message);
    }

    /**
     * @see AbstractRestException#getErrorCode()
     */
    @Override
    public int getErrorCode() {
        return ErrorCodes.METRIC_CRITERIA_INVALID;
    }

    /**
     * @see AbstractRestException#getMoreInfoUrl()
     */
    @Override
    public String getMoreInfoUrl() {
        return ErrorCodes.METRIC_CRITERIA_INVALID_INFO;
    }

}
