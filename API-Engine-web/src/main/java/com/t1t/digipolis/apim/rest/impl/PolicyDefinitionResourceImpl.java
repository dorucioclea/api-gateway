package com.t1t.digipolis.apim.rest.impl;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.policies.UpdatePolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicyFormType;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.rest.impl.i18n.Messages;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.resources.IPolicyDefinitionResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.*;
import com.t1t.digipolis.apim.security.ISecurityContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of the PolicyDefinition API.
 */
@ApplicationScoped
public class PolicyDefinitionResourceImpl implements IPolicyDefinitionResource {

    @Inject IStorage storage;
    @Inject IStorageQuery query;
    @Inject ISecurityContext securityContext;
    
    /**
     * Constructor.
     */
    public PolicyDefinitionResourceImpl() {
    }
    
    /**
     * @see IPolicyDefinitionResource#list()
     */
    @Override
    public List<PolicyDefinitionSummaryBean> list() throws NotAuthorizedException {
        try {
            return query.listPolicyDefinitions();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IPolicyDefinitionResource#create(com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean)
     */
    @Override
    public PolicyDefinitionBean create(PolicyDefinitionBean bean) throws PolicyDefinitionAlreadyExistsException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        // Auto-generate an ID if one isn't provided.
        if (bean.getId() == null || bean.getId().trim().isEmpty()) {
            bean.setId(BeanUtils.idFromName(bean.getName()));
        } else {
            bean.setId(BeanUtils.idFromName(bean.getId()));
        }
        try {
            storage.beginTx();
            if (storage.getPolicyDefinition(bean.getId()) != null) {
                throw ExceptionFactory.policyDefAlreadyExistsException(bean.getName());
            }
            if (bean.getFormType() == null) {
                bean.setFormType(PolicyFormType.Default);
            }
            // Store/persist the new policyDef
            storage.createPolicyDefinition(bean);
            storage.commitTx();
            return bean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IPolicyDefinitionResource#get(String)
     */
    @Override
    public PolicyDefinitionBean get(String policyDefinitionId) throws PolicyDefinitionNotFoundException, NotAuthorizedException {
        try {
            storage.beginTx();
            PolicyDefinitionBean bean = storage.getPolicyDefinition(policyDefinitionId);
            if (bean == null) {
                throw ExceptionFactory.policyDefNotFoundException(policyDefinitionId);
            }
            storage.commitTx();
            return bean;
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IPolicyDefinitionResource#update(String, com.t1t.digipolis.apim.beans.policies.UpdatePolicyDefinitionBean)
     */
    @Override
    public void update(String policyDefinitionId, UpdatePolicyDefinitionBean bean)
            throws PolicyDefinitionNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            PolicyDefinitionBean pdb = storage.getPolicyDefinition(policyDefinitionId);
            if (pdb == null) {
                throw ExceptionFactory.policyDefNotFoundException(policyDefinitionId);
            }
            if (pdb.getPluginId() != null) {
                throw new SystemErrorException(Messages.i18n.format("CannotUpdatePluginPolicyDef")); //$NON-NLS-1$
            }
            if (bean.getName() != null)
                pdb.setName(bean.getName());
            if (bean.getDescription() != null)
                pdb.setDescription(bean.getDescription());
            if (bean.getIcon() != null)
                pdb.setIcon(bean.getIcon());
            storage.updatePolicyDefinition(pdb);
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @see IPolicyDefinitionResource#delete(String)
     */
    @Override
    public void delete(String policyDefinitionId) throws PolicyDefinitionNotFoundException,
            NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        try {
            storage.beginTx();
            PolicyDefinitionBean pdb = storage.getPolicyDefinition(policyDefinitionId);
            if (pdb == null) {
                throw ExceptionFactory.policyDefNotFoundException(policyDefinitionId);
            }
            if (pdb.getPluginId() != null) {
                throw new SystemErrorException(Messages.i18n.format("CannotDeletePluginPolicyDef")); //$NON-NLS-1$
            }
            storage.deletePolicyDefinition(pdb);
            storage.commitTx();
        } catch (AbstractRestException e) {
            storage.rollbackTx();
            throw e;
        } catch (Exception e) {
            storage.rollbackTx();
            throw new SystemErrorException(e);
        }
    }

    /**
     * @return the storage
     */
    public IStorage getStorage() {
        return storage;
    }

    /**
     * @param storage the storage to set
     */
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }

    /**
     * @return the securityContext
     */
    public ISecurityContext getSecurityContext() {
        return securityContext;
    }

    /**
     * @param securityContext the securityContext to set
     */
    public void setSecurityContext(ISecurityContext securityContext) {
        this.securityContext = securityContext;
    }
    
}
