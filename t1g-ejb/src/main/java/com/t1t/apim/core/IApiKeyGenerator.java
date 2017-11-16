package com.t1t.apim.core;

/**
 * Service responsible for generating unique API keys whenever a contract
 * is created between an Application and a Service it wishes to invoke via
 * a specific plan.
 */
public interface IApiKeyGenerator {

    /**
     * Generates a new API key.
     *
     * @return a new API key
     */
    public String generate();

}
