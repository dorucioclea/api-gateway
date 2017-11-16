package com.t1t.apim.gateway.dto.exceptions;

/**
 * Created by michallispashidis on 07/09/15.
 */
public class ConsumerException extends AbstractEngineException {
    /**
     * Constructor.
     *
     * @param componentType the component type
     */
    public ConsumerException(String componentType) {
        super("Consumer not found: " + componentType); //$NON-NLS-1$
    }

    /**
     * Constructor.
     *
     * @param componentType the component type
     * @param cause         the exception cause the root cause
     */
    public ConsumerException(String componentType, Throwable cause) {
        super("Consumer not found: " + componentType, cause); //$NON-NLS-1$
    }
}
