package com.t1t.digipolis.apim.gateway.dto.exceptions;

/**
 * Exception thrown when a policy tries to get a component from the context
 * but the component doesn't exist or is otherwise not available.
 *
 */
public class ComponentNotFoundException extends AbstractEngineException {

    private static final long serialVersionUID = 8430298328831765033L;

    /**
     * Constructor.
     * @param componentType the component type
     */
    public ComponentNotFoundException(String componentType) {
        super("Component not found: " + componentType); //$NON-NLS-1$
    }

    /**
     * Constructor.
     * @param componentType the component type
     * @param cause the exception cause the root cause
     */
    public ComponentNotFoundException(String componentType, Throwable cause) {
        super("Component not found: " + componentType, cause); //$NON-NLS-1$
    }
}
