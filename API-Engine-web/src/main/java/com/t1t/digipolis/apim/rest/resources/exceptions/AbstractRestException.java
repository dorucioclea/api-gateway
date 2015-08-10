package com.t1t.digipolis.apim.rest.resources.exceptions;


/**
 * Base class for all APIMan errors coming out of the REST layer.
 *
 */
public abstract class AbstractRestException extends RuntimeException {

    private static final long serialVersionUID = -2406210413693314452L;
    
    // The stacktrace is only set on the client/UI side
    private transient String serverStack;
    
    /**
     * Constructor.
     */
    public AbstractRestException() {
    }
    
    /**
     * Constructor.
     * @param message the exception message
     */
    public AbstractRestException(String message) {
        super(message);
    }
    
    /**
     * Constructor.
     * @param cause the exception cause
     */
    public AbstractRestException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * @param message the exception message
     * @param cause the exception cause
     */
    public AbstractRestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @return the stacktrace
     */
    public String getServerStack() {
        return serverStack;
    }

    /**
     * @param stacktrace the stacktrace to set
     */
    public void setServerStack(String stacktrace) {
        this.serverStack = stacktrace;
    }

    /**
     * @return the httpCode
     */
    public abstract int getHttpCode();

    /**
     * @return the errorCode
     */
    public abstract int getErrorCode();

    /**
     * @return the moreInfo
     */
    public abstract String getMoreInfoUrl();

}
