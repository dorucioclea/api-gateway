package com.t1t.digipolis.apim.facades;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.rest.GatewayClient;
import com.t1t.digipolis.kong.model.KongPluginACLResponse;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.ServiceConventionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MigrationToACL {

    private static final Logger _LOG = LoggerFactory.getLogger(MigrationToACL.class);

    @Inject
    private IStorage storage;
    @Inject
    private IStorageQuery query;
    @Inject
    private GatewayFacade gatewayFacade;
    @Inject
    private SearchFacade searchFacade;
    @Inject
    private OrganizationFacade orgFacade;


    public MigrationToACL() {}


    public void migrate() {
        List<ServiceVersionBean> publishedServices = searchFacade.searchServicesByStatus(ServiceStatus.Published);
        enableAclOnPublishedServices(publishedServices);
        enableAclOnApplications();
    }
    /**
     * Enables ACL plugin on every published service
     */
    @SuppressWarnings("Duplicates")
    private void enableAclOnPublishedServices (List<ServiceVersionBean> publishedServices) {
        try {
            GatewayClient gateway = (GatewayClient) gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            List<ManagedApplicationBean> marketplaces = query.getMarketplaces();
            for (ServiceVersionBean versionBean : publishedServices) {
                Service gatewaySvc = new Service();
                gatewaySvc.setOrganizationId(versionBean.getService().getOrganization().getId());
                gatewaySvc.setServiceId(versionBean.getService().getId());
                gatewaySvc.setVersion(versionBean.getVersion());
                //Create plugin on Kong api
                _LOG.info("ACL plugin:{}", gateway.createACLPlugin(gatewaySvc));
                //Add marketplaces to Service ACL
                for (ManagedApplicationBean market : marketplaces) {
                    KongPluginACLResponse response = gateway.addConsumerToACL(
                            ConsumerConventionUtil.createManagedApplicationConsumerName(market),
                            ServiceConventionUtil.generateServiceUniqueName(gatewaySvc));
                    _LOG.info("Marketplace ACL:{}", response);
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(Policies.ACL.name());
                    npb.setConfiguration(new Gson().toJson(response));
                    npb.setKongPluginId(response.getId());
                    _LOG.info("Marketplace policy:{}", orgFacade.createManagedApplicationPolicy(market, npb));
                }
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private void enableAclOnApplications() {
        try {
            GatewayClient gateway = (GatewayClient) gatewayFacade.createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            List<ApplicationVersionBean> applications = query.findAllApplicationVersions();
            for (ApplicationVersionBean appVersion : applications) {
                String applicationName = ConsumerConventionUtil.createAppUniqueId(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion());
                List<ContractSummaryBean> contractSummaries = query.getApplicationContracts(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion());
                for (ContractSummaryBean summary : contractSummaries) {
                    String serviceName = ServiceConventionUtil.generateServiceUniqueName(summary.getServiceOrganizationId(), summary.getServiceId(), summary.getServiceVersion());
                    KongPluginACLResponse response = gateway.addConsumerToACL(applicationName, serviceName);
                    //Persist the unique Kong plugin id in a new policy associated with the app.
                    NewPolicyBean npb = new NewPolicyBean();
                    KongPluginACLResponse conf = new KongPluginACLResponse().withGroup(response.getGroup());
                    npb.setDefinitionId(Policies.ACL.name());
                    npb.setKongPluginId(response.getId());
                    npb.setContractId(summary.getContractId());
                    npb.setConfiguration(new Gson().toJson(conf));
                    _LOG.info("Policy " + applicationName + ":{}", orgFacade.doCreatePolicy(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion(), npb, PolicyType.Application));
                }
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }
}