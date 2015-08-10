package com.t1t.digipolis.apim.core.logging;

public interface IDelegateFactory {

    /**
     * Create a logger by name
     *
     * @param name the name
     * @return the logger
     */
    IApimanLogger createLogger(String name);

    /**
     * Create a logger by class
     *
     * @param klazz the class
     * @return the logger
     */
    IApimanLogger createLogger(Class<?> klazz);
}
