package com.t1t.digipolis.apim.security.impl;

import com.t1t.digipolis.apim.beans.idm.PermissionBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.apim.security.i18n.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

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
