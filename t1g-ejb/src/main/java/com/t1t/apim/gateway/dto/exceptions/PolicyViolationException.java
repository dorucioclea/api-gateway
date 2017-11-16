package com.t1t.apim.gateway.dto.exceptions;

/**
 * Exception thrown when a policy is violated.  This happens during processing
 * of an inbound request to a managed service.  The policies associated with
 * the managed service are evaluated against the inbound request (and potentially
 * current state information).  If any policy is violated, an error of this type
 * will be raised.
 */
public class PolicyViolationException extends AbstractEngineException {

    private static final long serialVersionUID = -456431090386043376L;

    /**
     * Constructor.
     *
     * @param message an error message
     */
    public PolicyViolationException(String message) {
        super(message);
    }

}
