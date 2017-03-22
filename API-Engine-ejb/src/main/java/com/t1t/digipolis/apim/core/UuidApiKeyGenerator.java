package com.t1t.digipolis.apim.core;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.util.UUID;

/**
 * A simple api key generator using java UUIDs.
 *
 */
@ApplicationScoped @Default
public class UuidApiKeyGenerator implements IApiKeyGenerator {
    
    /**
     * Constructor.
     */
    public UuidApiKeyGenerator() {
    }

    /**
     * @see com.t1t.digipolis.apim.core.IApiKeyGenerator#generate()
     */
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

}
