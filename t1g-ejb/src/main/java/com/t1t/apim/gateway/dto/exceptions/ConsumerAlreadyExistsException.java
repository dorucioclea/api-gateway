package com.t1t.apim.gateway.dto.exceptions;

/**
 * Created by michallispashidis on 07/09/15.
 */
public class ConsumerAlreadyExistsException extends AbstractEngineException {
    /**
     * Constructor.
     *
     * @param componentType the component type
     */
    public ConsumerAlreadyExistsException(String componentType) {
        super("Consumer already exists: " + componentType); //$NON-NLS-1$
    }

    /**
     * Constructor.
     *
     * @param componentType the component type
     * @param cause         the exception cause the root cause
     */
    public ConsumerAlreadyExistsException(String componentType, Throwable cause) {
        super("Consumer already exists: " + componentType, cause); //$NON-NLS-1$
    }
}
