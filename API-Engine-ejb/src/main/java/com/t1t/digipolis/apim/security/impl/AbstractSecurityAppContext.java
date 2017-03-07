package com.t1t.digipolis.apim.security.impl;

import com.t1t.digipolis.apim.security.ISecurityAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for security context implementations.
 *
 */
public abstract class AbstractSecurityAppContext implements ISecurityAppContext {

    private static Logger logger = LoggerFactory.getLogger(AbstractSecurityAppContext.class);
    /**
     * Constructor.
     */
    public AbstractSecurityAppContext() {
    }
}
