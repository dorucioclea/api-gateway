package com.t1t.digipolis.apim.facades;

import com.google.gson.Gson;
import com.t1t.digipolis.apim.beans.apps.ApplicationStatus;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.services.ServiceGatewayBean;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionWithMarketInfoBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationVersionSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.i18n.Messages;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Contract;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.kong.model.KongConsumer;
import com.t1t.digipolis.kong.model.KongConsumerList;
import com.t1t.digipolis.kong.model.KongPluginACLResponse;
import com.t1t.digipolis.util.ConsumerConventionUtil;
import com.t1t.digipolis.util.ServiceConventionUtil;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import java.util.*;

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

    private IGatewayLink createGatewayLink(String gatewayId) throws AbstractRestException {
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

    /**
     * TODO this first implementation will sync all apikeys for all applications.
     */
    public void syncBusinessModel() throws AbstractRestException {
        //Sync Services
        //Sync Applications
        //Sync apikeys
        try {
            final List<ApplicationVersionBean> applicationVersions = query.findAllApplicationVersions();
            _LOG.info("=== SYNC::Applications Apikeys("+applicationVersions.size()+")");
            for(ApplicationVersionBean avb:applicationVersions){
                //Keep track of service gateways
                Map<String, IGatewayLink> links = new HashMap<>();
                //Get contracts
                final List<ContractSummaryBean> applicationContracts = query.getApplicationContracts(avb.getApplication().getOrganization().getId(), avb.getApplication().getId(), avb.getVersion());
                String appApiKey = null;
                for(ContractSummaryBean contractSummaryBean:applicationContracts){
                    //all apikeys should be the same
                    if(StringUtils.isEmpty(appApiKey)&&StringUtils.isNotEmpty(contractSummaryBean.getApikey())){
                        appApiKey = contractSummaryBean.getApikey();
                    }
                    //get all gateways where application should be registered
                    ServiceVersionBean svb = storage.getServiceVersion(contractSummaryBean.getServiceOrganizationId(), contractSummaryBean.getServiceId(), contractSummaryBean.getServiceVersion());
                    Set<ServiceGatewayBean> gateways = svb.getGateways();
                    for (ServiceGatewayBean serviceGatewayBean : gateways) {
                        if (!links.containsKey(serviceGatewayBean.getGatewayId())) {
                            IGatewayLink gatewayLink = createGatewayLink(serviceGatewayBean.getGatewayId());
                            links.put(serviceGatewayBean.getGatewayId(), gatewayLink);
                        }
                    }
                }
                //log info
                _LOG.info("Sync application orgId:"+avb.getApplication().getOrganization().getId());
                _LOG.info("Sync application appId:"+avb.getApplication().getId());
                _LOG.info("Sync application version:"+avb.getVersion());
                _LOG.info("===> apikey:"+appApiKey);
                _LOG.info("===> gateways:"+links);
                //sync apikey
                if(StringUtils.isNotEmpty(appApiKey)){
                    for (IGatewayLink gatewayLink : links.values()) {
                        // Validate that the application has a key-auth apikey available on the gateway - fallback scenario
                        try {
                            String appConsumerName = ConsumerConventionUtil.createAppUniqueId(avb.getApplication().getOrganization().getId(), avb.getApplication().getId(), avb.getVersion());
                            gatewayLink.addConsumerKeyAuth(appConsumerName, appApiKey);
                        } catch (Exception e) {
                            //apikey for consumer already exists
                        }
                        gatewayLink.close();
                    }
                }
            }
            _LOG.info("=== FINISHED SYNC::Applications Apikeys("+applicationVersions.size()+")");
        } catch (StorageException e) {
            throw new SystemErrorException("Error accessing datastore");
        }
    }


    //TODO: execute init script
    //sync users
    //republish services
    //initialize unregistered consumer
    //register consumers
    //TODO: impact service request events?
    public void rebuildGtw() {
        _LOG.info("====MIGRATION-START====");
        syncUsers();
        republishServices();
        initUnregisteredApps();
        registerApps();
        _LOG.info("====MIGRATION-END======");
    }

    /**
     * Synchronizes existing users from db to gateway.
     * No plugins are applied on users
     * All users must have JWT credentials generated.
     */
    private void syncUsers() {
        _LOG.info("Synchronize Users::START");
        _LOG.info("Synchronize Users::END");
    }

    /**
     * All published services must be provisioned to the gateway, we reuse the action facade for all know
     * services with state published.
     * We pay attention to:
     * - existing provisionkey for oauth plugin
     * - verification of default policies
     */
    private void republishServices() {
        _LOG.info("Publish Services::START");
        _LOG.info("Publish Services::END");
    }

    /**
     * Unregisterd applications are initialized with:
     * - applicable apikey (from contract)
     * - correct acl's (based on contracted services)
     * - jwt credentials (future jwt-up)
     * - oauth credentials + callback url (+ oauth name)
     */
    private void initUnregisteredApps() {
        _LOG.info("Initialize Unregistered Consumers::START");
        _LOG.info("Initialize Unregistered Consumers::END");
    }

    /**
     * Register application through actionfacade.
     */
    private void registerApps() {
        _LOG.info("Register Consumers::START");
        _LOG.info("Register Consumers::END");
    }
}