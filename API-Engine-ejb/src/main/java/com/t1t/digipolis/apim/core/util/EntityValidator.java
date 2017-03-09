package com.t1t.digipolis.apim.core.util;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.core.IApplicationValidator;
import com.t1t.digipolis.apim.core.IServiceValidator;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

/**
 * Validates the state of various entities, including services and applications.
 *
 */
@ApplicationScoped
@Default
public class EntityValidator implements IServiceValidator, IApplicationValidator {

    @Inject
    private IStorageQuery storageQuery;

    /**
     * @see com.t1t.digipolis.apim.core.IApplicationValidator#isReady(ApplicationVersionBean)
     */
    @Override
    public boolean isReady(ApplicationVersionBean application) {
        boolean ready = true;

        try {
            List<ContractSummaryBean> contracts = storageQuery.getApplicationContracts(application.getApplication().getOrganization().getId(), application.getApplication().getId(), application.getVersion());
            if (contracts.isEmpty()) {
                ready = false;
            }
            //We no longer need to check if the oauth credentials are correctly filled in, they're automatically created now
            /*else {
                boolean requiresCallback = false;
                for (ContractSummaryBean summary : contracts) {
                    if (!storageQuery.getEntityPoliciesByDefinitionId(summary.getServiceOrganizationId(), summary.getServiceId(), summary.getServiceVersion(), PolicyType.Service, Policies.OAUTH2).isEmpty()) {
                        requiresCallback = true;
                    }
                }
                if (requiresCallback) {
                    ready = !StringUtils.isEmpty(application.getoAuthClientId()) && !application.getOauthClientRedirects().isEmpty();
                }
            }*/
        }
        catch (StorageException ex) {
            throw ExceptionFactory.systemErrorException(ex);
        }

        return isReady(application, ready);
    }

    /**
     * @see com.t1t.digipolis.apim.core.IApplicationValidator#isReady(ApplicationVersionBean, boolean)
     */
    @Override
    public boolean isReady(ApplicationVersionBean application, boolean hasContracts) {
        boolean ready = hasContracts;
        return ready;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IApplicationValidator#isReady(ApplicationVersionBean)
     */
    @Override
    public boolean isReady(ServiceVersionBean service) {
        boolean ready = true;
        if (service.getEndpoint() == null || service.getEndpoint().trim().length() == 0) {
            ready = false;
        }
        if (service.getEndpointType() == null) {
            ready = false;
        }
        if (!service.isPublicService() && (service.getPlans() == null || service.getPlans().isEmpty())) {
            ready = false;
        }
        if (service.getGateways() == null || service.getGateways().isEmpty()) {
            ready = false;
        }
        if(service.getVisibility() == null || service.getVisibility().isEmpty()){
            ready = false;
        }
        if (!service.getAutoAcceptContracts() && StringUtils.isEmpty(service.getReadme())) {
            ready = false;
        }
        return ready;
    }

    /**
     * @return the storageQuery
     */
    public IStorageQuery getStorageQuery() {
        return storageQuery;
    }

    /**
     * @param storageQuery the storageQuery to set
     */
    public void setStorageQuery(IStorageQuery storageQuery) {
        this.storageQuery = storageQuery;
    }

}
