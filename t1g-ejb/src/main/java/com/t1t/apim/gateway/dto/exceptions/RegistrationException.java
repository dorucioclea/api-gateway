package com.t1t.apim.gateway.dto.exceptions;

/**
 * Exception thrown when an error happens during registration of an application.
 */
public class RegistrationException extends AbstractEngineException {

    private static final long serialVersionUID = -8704997957184049457L;

    /**
     * Constructor.
     *
     * @param message an error message
     */
    public RegistrationException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message an error message
     * @param cause   the exception cause the underlying cause
     */
    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     *
     * @param cause the exception cause the underlying cause
     */
    public RegistrationException(Exception cause) {
        super(cause);
    }

}
