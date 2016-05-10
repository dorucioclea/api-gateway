package com.t1t.digipolis.apim.facades;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.apps.ApplicationStatus;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionWithMarketInfoBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongConsumerList;
import com.t1t.digipolis.kong.model.KongPluginACLResponse;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.ServiceConventionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MigrationFacade {

    private static final Logger _LOG = LoggerFactory.getLogger(MigrationFacade.class);
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
    @Inject
    private IGatewayLinkFactory gatewayLinkFactory;

    public MigrationFacade() {}

    public void migrateToAcl() {
        List<ServiceVersionWithMarketInfoBean> publishedServices = searchFacade.searchServicesByStatus(ServiceStatus.Published);
        enableAclOnPublishedServices(publishedServices);
        enableAclOnApplications();
        _LOG.info("Migration ACL finished");
    }

    public void renameApplicationCustomIds() {
        try {
            IGatewayLink gateway = createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            gateway.getConsumers().getData().forEach(consumer -> {
                if (!StringUtils.isEmpty(consumer.getUsername())) {
                    if (!consumer.getUsername().equals(consumer.getCustomId())) {
                        consumer.setCustomId(consumer.getUsername());
                        gateway.updateOrCreateConsumer(consumer);
                    }
                }
            });
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    /**
     * Enables ACL plugin on every published service
     */
    @SuppressWarnings("Duplicates")
    private void enableAclOnPublishedServices (List<ServiceVersionWithMarketInfoBean> publishedServices) {
        try {
            IGatewayLink gateway = createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            List<ManagedApplicationBean> managedApps = query.getManagedApps();
            for (ServiceVersionBean versionBean : publishedServices) {
                Service gatewaySvc = new Service();
                gatewaySvc.setOrganizationId(versionBean.getService().getOrganization().getId());
                gatewaySvc.setServiceId(versionBean.getService().getId());
                gatewaySvc.setVersion(versionBean.getVersion());
                //Create plugin on Kong api
                try {
                    _LOG.info("ACL plugin:{}", gateway.createACLPlugin(gatewaySvc));
                }catch(Exception ex){
                    ;//ignore
                }
                //Add marketplaces to Service ACL
                for (ManagedApplicationBean managedApp : managedApps) {
                    try{
                        KongPluginACLResponse response = gateway.addConsumerToACL(
                                ConsumerConventionUtil.createManagedApplicationConsumerName(managedApp),
                                ServiceConventionUtil.generateServiceUniqueName(gatewaySvc));
                        _LOG.info("Marketplace ACL:{}", response);
                        NewPolicyBean npb = new NewPolicyBean();
                        npb.setDefinitionId(Policies.ACL.name());
                        npb.setConfiguration(new Gson().toJson(response));
                        npb.setKongPluginId(response.getId());
                        _LOG.info("Marketplace policy:{}", orgFacade.createManagedApplicationPolicy(managedApp, npb));
                    }catch(Exception ex){
                        ;//ignore
                    }
                }
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private void enableAclOnApplications() {
        try {
            IGatewayLink gateway = createGatewayLink(gatewayFacade.getDefaultGateway().getId());
            List<ApplicationVersionBean> applications = query.findAllApplicationVersions();
            for (ApplicationVersionBean appVersion : applications) {
                if (appVersion.getStatus() != ApplicationStatus.Retired) {
                    String applicationName = ConsumerConventionUtil.createAppUniqueId(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion());
                    List<ContractSummaryBean> contractSummaries = query.getApplicationContracts(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion());
                    for (ContractSummaryBean summary : contractSummaries) {
                        String serviceName = ServiceConventionUtil.generateServiceUniqueName(summary.getServiceOrganizationId(), summary.getServiceId(), summary.getServiceVersion());
                        try{
                            KongPluginACLResponse response = gateway.addConsumerToACL(applicationName, serviceName);
                            //Persist the unique Kong plugin id in a new policy associated with the app.
                            NewPolicyBean npb = new NewPolicyBean();
                            KongPluginACLResponse conf = new KongPluginACLResponse().withGroup(response.getGroup());
                            npb.setDefinitionId(Policies.ACL.name());
                            npb.setKongPluginId(response.getId());
                            npb.setContractId(summary.getContractId());
                            npb.setConfiguration(new Gson().toJson(conf));
                            _LOG.info("Policy " + applicationName + ":{}", orgFacade.doCreatePolicy(appVersion.getApplication().getOrganization().getId(), appVersion.getApplication().getId(), appVersion.getVersion(), npb, PolicyType.Application));
                        }catch(Exception ex){
                            ;//ignore
                        }
                    }
                }
            }
        }
        catch (StorageException ex) {
            throw new SystemErrorException(ex);
        }
    }

    private IGatewayLink createGatewayLink(String gatewayId) throws PublishingException {
        try {
            GatewayBean gateway = storage.getGateway(gatewayId);
            if (gateway == null) {
                throw new GatewayNotFoundException();
            }
            IGatewayLink link = gatewayLinkFactory.create(gateway);
            return link;
        } catch (GatewayNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PublishingException(e.getMessage(), e);
        }
    }
}