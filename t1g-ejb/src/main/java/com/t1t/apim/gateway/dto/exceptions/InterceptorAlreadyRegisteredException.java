package com.t1t.apim.gateway.dto.exceptions;

/**
 * Thrown when a Service Connection Interceptor has already been registered in
 * the IPolicyContext
 *
 *
 */
public class InterceptorAlreadyRegisteredException extends AbstractEngineException {

    private static final long serialVersionUID = -4134086341668627517L;

    /**
     * Constructor.
     * @param interceptorClass the interceptor class
     */
    @SuppressWarnings("nls")
    public InterceptorAlreadyRegisteredException(Class<?> interceptorClass) {
        super("An Interceptor of type " + interceptorClass + " was already registered in the context");
    }
    
}
