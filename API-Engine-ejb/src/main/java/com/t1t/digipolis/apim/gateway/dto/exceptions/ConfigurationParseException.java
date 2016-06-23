package com.t1t.digipolis.apim.gateway.dto.exceptions;

/**
 * Exception thrown when a policy fails to parse the configuration information
 * sent it.
 *
 */
public class ConfigurationParseException extends AbstractEngineException {

    private static final long serialVersionUID = -1265213011525200681L;

    /**
     * Constructor.
     * @param message an error message
     */
    public ConfigurationParseException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param message an error message
     * @param cause the exception cause the underlying cause
     */
    public ConfigurationParseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     * @param cause the exception cause
     */
    public ConfigurationParseException(Throwable cause) {
        super(cause);
    }
}
