package com.t1t.digipolis.apim.core;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import java.util.UUID;

/**
 * A simple api key generator using java UUIDs.
 *
 */
@ApplicationScoped @Alternative
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
