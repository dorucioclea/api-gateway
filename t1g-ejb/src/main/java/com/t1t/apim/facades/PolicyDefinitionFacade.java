package com.t1t.apim.facades;

import com.t1t.apim.beans.BeanUtils;
import com.t1t.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.apim.beans.policies.UpdatePolicyDefinitionBean;
import com.t1t.apim.beans.summary.PolicyDefinitionSummaryBean;
import com.t1t.apim.beans.summary.PolicyFormType;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.AbstractRestException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.exceptions.i18n.Messages;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by michallispashidis on 17/08/15.
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PolicyDefinitionFacade {
    @Inject private IStorage storage;
    @Inject private IStorageQuery query;

    public List<PolicyDefinitionSummaryBean> list(){
        try {
            return query.listPolicyDefinitions();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    public PolicyDefinitionBean create(PolicyDefinitionBean bean){
        // Auto-generate an ID if one isn't provided.
        if (bean.getId() == null || bean.getId().trim().isEmpty()) {
            bean.setId(BeanUtils.idFromName(bean.getName()));
        } else {
            bean.setId(BeanUtils.idFromName(bean.getId()));
        }
        try {
            if (storage.getPolicyDefinition(bean.getId()) != null) {
                throw ExceptionFactory.policyDefAlreadyExistsException(bean.getName());
            }
            if (bean.getFormType() == null) {
                bean.setFormType(PolicyFormType.Default);
            }
            // Store/persist the new policyDef
            storage.createPolicyDefinition(bean);
            return bean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public PolicyDefinitionBean get(String policyDefinitionId){
        try {
            PolicyDefinitionBean bean = storage.getPolicyDefinition(policyDefinitionId);
            if (bean == null) {
                throw ExceptionFactory.policyDefNotFoundException(policyDefinitionId);
            }
            return bean;
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void update (String policyDefinitionId, UpdatePolicyDefinitionBean bean){
        try {
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
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }

    public void delete(String policyDefinitionId){
        try {
            PolicyDefinitionBean pdb = storage.getPolicyDefinition(policyDefinitionId);
            if (pdb == null) {
                throw ExceptionFactory.policyDefNotFoundException(policyDefinitionId);
            }
            if (pdb.getPluginId() != null) {
                throw new SystemErrorException(Messages.i18n.format("CannotDeletePluginPolicyDef")); //$NON-NLS-1$
            }
            storage.deletePolicyDefinition(pdb);
        } catch (AbstractRestException e) {
            throw e;
        } catch (Exception e) {
            throw new SystemErrorException(e);
        }
    }
}
