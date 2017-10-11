package com.t1t.apim.facades;

import com.t1t.apim.beans.BeanUtils;
import com.t1t.apim.beans.audit.data.EntityUpdatedData;
import com.t1t.apim.beans.brandings.NewServiceBrandingBean;
import com.t1t.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.apim.beans.services.ServiceBean;
import com.t1t.apim.beans.services.ServiceStatus;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.facades.audit.AuditUtils;
import com.t1t.apim.security.ISecurityContext;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BrandingFacade {

    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private ISecurityContext security;

    public Set<ServiceBrandingBean> getAllServiceBrandings() {
        try {

            return storage.getAllBrandings();
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public ServiceBrandingBean getServiceBranding(String id) {
        try {
            ServiceBrandingBean rval = storage.getBranding(id);
            if (rval == null) {
                throw ExceptionFactory.brandingNotFoundException(id);
            }
            return rval;
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public ServiceBrandingBean createServiceBranding(NewServiceBrandingBean nsbb) {
        String id = BeanUtils.idFromName(nsbb.getName());
        try {
            if (storage.getOrganization(id) != null) {
                throw ExceptionFactory.brandingNotAvailableException("BrandingNameExistsAsOrg", nsbb.getName());
            }
            if (storage.getBranding(id) != null) {
                throw ExceptionFactory.brandingNotAvailableException("BrandingNameAlreadyExists", nsbb.getName());
            }
            ServiceBrandingBean newBranding = new ServiceBrandingBean();
            newBranding.setId(id);
            newBranding.setName(nsbb.getName());
            storage.createBranding(newBranding);
            return newBranding;
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }

    public void deleteServiceBranding(String id) {
        ServiceBrandingBean branding = getServiceBranding(id);
        try {
            for (ServiceBean service : branding.getServices()) {
                if (!query.getServiceVersionByStatusForService(new HashSet<>(Arrays.asList(ServiceStatus.Deprecated, ServiceStatus.Published)), service).isEmpty()) {
                    throw ExceptionFactory.brandingCannotBeDeletedException(id);
                }
                String originalBrandings = service.getBrandings().toString();
                service.getBrandings().remove(branding);
                EntityUpdatedData data = new EntityUpdatedData();
                data.addChange("brandings", originalBrandings, service.getBrandings().toString());
                storage.updateService(service);
                storage.createAuditEntry(AuditUtils.serviceUpdated(service, data, security));
            }
            storage.deleteBranding(branding);
        } catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }
    }
}