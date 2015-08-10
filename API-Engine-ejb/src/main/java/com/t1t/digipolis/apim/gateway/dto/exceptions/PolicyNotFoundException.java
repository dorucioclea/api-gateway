package com.t1t.digipolis.apim.gateway.dto.exceptions;

/**
 * Exception thrown when trying to get a policy from the policy factory.
 *
 */
public class PolicyNotFoundException extends AbstractEngineException {

    private static final long serialVersionUID = -5297100732954334470L;

    /**
     * Constructor.
     * @param componentType the component type
     */
    public PolicyNotFoundException(String componentType) {
        super("Policy not found: " + componentType); //$NON-NLS-1$
    }

    /**
     * Constructor.
     * @param componentType the component type
     * @param cause the exception cause the root cause
     */
    public PolicyNotFoundException(String componentType, Throwable cause) {
        super("Policy not found: " + componentType, cause); //$NON-NLS-1$
    }
}
