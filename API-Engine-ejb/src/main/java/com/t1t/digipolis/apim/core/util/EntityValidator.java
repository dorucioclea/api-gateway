package com.t1t.digipolis.apim.core.util;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.core.IApplicationValidator;
import com.t1t.digipolis.apim.core.IServiceValidator;
import com.t1t.digipolis.apim.core.IStorageQuery;

import javax.inject.Inject;
import java.util.List;

/**
 * Validates the state of various entities, including services and applications.
 *
 */
public class EntityValidator implements IServiceValidator, IApplicationValidator {

    @Inject
    private IStorageQuery storageQuery;

    /**
     * Constructor.
     */
    public EntityValidator() {
    }

    /**
     * @see com.t1t.digipolis.apim.core.IApplicationValidator#isReady(ApplicationVersionBean)
     */
    @Override
    public boolean isReady(ApplicationVersionBean application) throws Exception {
        boolean hasContracts = true;

        List<ContractSummaryBean> contracts = storageQuery.getApplicationContracts(application.getApplication().getOrganization().getId(), application
                .getApplication().getId(), application.getVersion());
        if (contracts.isEmpty()) {
            hasContracts = false;
        }

        return isReady(application, hasContracts);
    }

    /**
     * @see com.t1t.digipolis.apim.core.IApplicationValidator#isReady(ApplicationVersionBean, boolean)
     */
    @Override
    public boolean isReady(ApplicationVersionBean application, boolean hasContracts) throws Exception {
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
        if (!service.isPublicService()) {
            if (service.getPlans() == null || service.getPlans().isEmpty()) {
                ready = false;
            }
        }
        if (service.getGateways() == null || service.getGateways().isEmpty()) {
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
