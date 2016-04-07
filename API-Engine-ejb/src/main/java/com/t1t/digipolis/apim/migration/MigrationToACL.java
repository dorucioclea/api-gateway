package com.t1t.digipolis.apim.migration;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.facades.GatewayFacade;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.facades.SearchFacade;
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
                gateway.createACLPlugin(gatewaySvc);
                //Add marketplaces to Service ACL
                for (ManagedApplicationBean market : marketplaces) {
                    KongPluginACLResponse response = gateway.addConsumerToACL(
                            ConsumerConventionUtil.createManagedApplicationConsumerName(market),
                            ServiceConventionUtil.generateServiceUniqueName(gatewaySvc));
                    NewPolicyBean npb = new NewPolicyBean();
                    npb.setDefinitionId(Policies.ACL.name());
                    npb.setConfiguration(new Gson().toJson(response));
                    npb.setKongPluginId(response.getId());
                    orgFacade.createManagedApplicationPolicy(market, npb);
                }
                //Get service contracts and apply ACL
                List<ContractSummaryBean> contracts = query.getServiceContracts(gatewaySvc.getOrganizationId(), gatewaySvc.getServiceId(), gatewaySvc.getVersion(), 1, 10000);
                for (ContractSummaryBean contract : contracts) {
                    String appConsumerName = ConsumerConventionUtil.createAppUniqueId(contract.getAppOrganizationName(), contract.getAppId(), contract.getAppVersion());
                    /*
                    //Add ACL group membership by default on gateway
                    KongPluginACLResponse response = gateway.addConsumerToACL(appConsumerName,
                            ServiceConventionUtil.generateServiceUniqueName(bean.getServiceOrgId(), bean.getServiceId(), bean.getServiceVersion()));
                    //Persist the unique Kong plugin id in a new policy associated with the app.
                    NewPolicyBean npb = new NewPolicyBean();
                    KongPluginACLResponse conf = new KongPluginACLResponse().withGroup(response.getGroup());
                    npb.setDefinitionId(Policies.ACL.name());
                    npb.setKongPluginId(response.getId());
                    npb.setContractId(contract.getId());
                    npb.setConfiguration(new Gson().toJson(conf));
                    createAppPolicy(organizationId, applicationId, version, npb);
                    */
                }
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private void addMarketPlaceToServiceACL(Service gatewaySvc, List<ManagedApplicationBean> marketplaces) {

    }
}