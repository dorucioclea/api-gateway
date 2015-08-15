package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by michallispashidis on 15/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AuditFacade extends AbstractFacade<AuditEntryBean>{
    @Inject
    @APIEngineContext private Logger log;
    @Inject @APIEngineContext private EntityManager em;
    @Inject private ISecurityContext securityContext;

    //Default constructor
    public AuditFacade() {
        super(AuditEntryBean.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
