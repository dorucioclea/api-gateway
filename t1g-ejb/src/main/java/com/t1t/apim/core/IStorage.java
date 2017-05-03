package com.t1t.apim.core;

import com.t1t.apim.beans.announcements.AnnouncementBean;
import com.t1t.apim.beans.apps.AppIdentifier;
import com.t1t.apim.beans.apps.ApplicationBean;
import com.t1t.apim.beans.apps.ApplicationVersionBean;
import com.t1t.apim.beans.audit.AuditEntryBean;
import com.t1t.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.apim.beans.config.ConfigBean;
import com.t1t.apim.beans.contracts.ContractBean;
import com.t1t.apim.beans.defaults.DefaultsBean;
import com.t1t.apim.beans.events.EventBean;
import com.t1t.apim.beans.gateways.GatewayBean;
import com.t1t.apim.beans.idp.IDPBean;
import com.t1t.apim.beans.idp.KeyMappingBean;
import com.t1t.apim.beans.idp.KeystoreBean;
import com.t1t.apim.beans.iprestriction.BlacklistBean;
import com.t1t.apim.beans.iprestriction.WhitelistBean;
import com.t1t.apim.beans.mail.MailProviderBean;
import com.t1t.apim.beans.mail.MailTemplateBean;
import com.t1t.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.apim.beans.operation.OperatingBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.beans.plans.PlanBean;
import com.t1t.apim.beans.plans.PlanVersionBean;
import com.t1t.apim.beans.plugins.PluginBean;
import com.t1t.apim.beans.policies.PolicyBean;
import com.t1t.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.apim.beans.policies.PolicyType;
import com.t1t.apim.beans.services.ServiceBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.support.SupportBean;
import com.t1t.apim.beans.support.SupportComment;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.mail.MailTopic;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

/**
 * Represents the persistent storage interface for Apiman DT.
 *
 */
public interface IStorage {
    /*
     * Various creation methods.  These are called by the REST layer to create stuff.
     */
    
    public void createOrganization(OrganizationBean organization) throws StorageException;
    public void createApplication(ApplicationBean application) throws StorageException;
    public void createApplicationVersion(ApplicationVersionBean version) throws StorageException;
    public void createContract(ContractBean contract) throws StorageException;
    public void createService(ServiceBean service) throws StorageException;
    public void createServiceVersion(ServiceVersionBean version) throws StorageException;
    public void createPlan(PlanBean plan) throws StorageException;
    public void createPlanVersion(PlanVersionBean version) throws StorageException;
    public void createPolicy(PolicyBean policy) throws StorageException;
    public void createGateway(GatewayBean gateway) throws StorageException;
    public void createPlugin(PluginBean plugin) throws StorageException;
    public void createPolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException;
    public void createAuditEntry(AuditEntryBean entry) throws StorageException;
    public void createServiceAnnouncement(AnnouncementBean announcement) throws StorageException;
    public void createServiceSupport(SupportBean supportBean) throws StorageException;
    public void createServiceSupportComment(SupportComment commentBean) throws StorageException;
    public void createWhilelistRecord(WhitelistBean whitelistBean)throws StorageException;
    public void createBlacklistRecord(BlacklistBean blacklistBean)throws StorageException;
    public void createEvent(EventBean eventBean) throws StorageException;
    public void createMailTemplate(MailTemplateBean mailTemplateBean) throws Exception;
    public void createManagedApplication(ManagedApplicationBean manapp)throws Exception;
    public void createKeyMapping(KeyMappingBean keyMappingBean) throws Exception;
    public void createDefaults(DefaultsBean defaultsBean) throws StorageException;
    public void createConfig(ConfigBean config) throws StorageException;
    public void createBranding(ServiceBrandingBean branding) throws StorageException;
    public void createOAuth2Token(OAuth2TokenBean token) throws StorageException;
    public void createIDP(IDPBean idp) throws StorageException;
    public void createKeystore(KeystoreBean keystore) throws StorageException;
    public void createMailProvider(MailProviderBean mailProvider) throws StorageException;

    /*
     * Various update methods.  These are called by the REST layer to update stuff.
     */

    public void updateOrganization(OrganizationBean organization) throws StorageException;
    public void updateApplication(ApplicationBean application) throws StorageException;
    public void updateApplicationVersion(ApplicationVersionBean version) throws StorageException;
    public void updateService(ServiceBean service) throws StorageException;
    public void updateServiceVersion(ServiceVersionBean version) throws StorageException;
    public void updateServiceDefinition(ServiceVersionBean version, InputStream definitionStream) throws StorageException;
    public void updatePlan(PlanBean plan) throws StorageException;
    public void updatePlanVersion(PlanVersionBean version) throws StorageException;
    public void updatePolicy(PolicyBean policy) throws StorageException;
    public void updateGateway(GatewayBean gateway) throws StorageException;
    public void updatePolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException;
    public void updateServiceAnnouncement(AnnouncementBean announcement) throws StorageException;
    public void updateServiceSupport(SupportBean supportBean) throws StorageException;
    public void updateServiceSupportComment(SupportComment commentBean) throws StorageException;
    public void updateContract(ContractBean contractBean) throws StorageException;
    public void updateMailTemplate(MailTemplateBean mailTemplateBean)throws StorageException;
    public void updateEvent(EventBean event)throws StorageException;
    public void updateManagedApplication(ManagedApplicationBean manapp) throws StorageException;
    public void updateAuditEntry(AuditEntryBean audit) throws StorageException;
    public void updateKeyMapping(KeyMappingBean keyMappingBean) throws StorageException;
    public void updateDefaults(DefaultsBean defaultsBean) throws StorageException;
    public void updateConfig(ConfigBean config) throws StorageException;
    public void updateBranding(ServiceBrandingBean branding) throws StorageException;
    public void updateOperatingBean(OperatingBean operatingBean) throws StorageException;
    public void updateOAuth2TokenBean(OAuth2TokenBean token) throws StorageException;
    public void updateIDPBean(IDPBean idp) throws StorageException;
    public void updateKeystoreBean(KeystoreBean keystore) throws StorageException;
    public void updateMailProviderBean(MailProviderBean mailProvider) throws StorageException;

    /*
     * Various delete methods.  These are called by the REST layer to delete stuff.
     */

    public void deleteOrganization(OrganizationBean organization) throws StorageException;
    public void deleteApplication(ApplicationBean application) throws StorageException;
    public void deleteApplicationVersion(ApplicationVersionBean version) throws StorageException;
    public void deleteContract(ContractBean contract) throws StorageException;
    public void deleteService(ServiceBean service) throws StorageException;
    public void deleteServiceVersion(ServiceVersionBean version) throws StorageException;
    public void deleteServiceDefinition(ServiceVersionBean version) throws StorageException;
    public void deletePlan(PlanBean plan) throws StorageException;
    public void deletePlanVersion(PlanVersionBean version) throws StorageException;
    public void deletePolicy(PolicyBean policy) throws StorageException;
    public void deleteGateway(GatewayBean gateway) throws StorageException;
    public void deletePlugin(PluginBean plugin) throws StorageException;
    public void deletePolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException;
    public void deleteServiceAnnouncement(AnnouncementBean announcement) throws StorageException;
    public void deleteServiceSupport(SupportBean supportBean) throws StorageException;
    public void deleteServiceSupportComment(SupportComment commentBean) throws StorageException;
    public void deleteWhitelistRecord(WhitelistBean whitelistBean) throws StorageException;
    public void deleteBalcklistRecord(BlacklistBean blacklistBean) throws StorageException;
    public void deleteEvent(EventBean eventBean) throws StorageException;
    public void deleteMailTemplate (MailTemplateBean mailTemplateBean) throws StorageException;
    public void deleteManagedApplication(ManagedApplicationBean manapp)throws StorageException;
    public void deleteKeyMapping(KeyMappingBean keyMappingBean) throws StorageException;
    public void deleteDefaults(DefaultsBean defaultsBean) throws StorageException;
    public void deleteConfig(ConfigBean configBean) throws StorageException;
    public void deleteBranding(ServiceBrandingBean branding) throws StorageException;
    public void deleteOAuth2Token(OAuth2TokenBean token) throws StorageException;
    public void deleteIDP(IDPBean idp) throws StorageException;
    public void deleteKeystore(KeystoreBean keystore) throws StorageException;
    public void deleteMailProvider(MailProviderBean mailProvider) throws StorageException;

    /*
     * Various get methods.  These are called by the REST layer to get stuff.
     */

    public OrganizationBean getOrganization(String id) throws StorageException;
    public ApplicationBean getApplication(String organizationId, String id) throws StorageException;
    public ApplicationVersionBean getApplicationVersion(String organizationId, String applicationId, String version) throws StorageException;
    public ContractBean getContract(Long id) throws StorageException;
    public ServiceBean getService(String organizationId, String id) throws StorageException;
    public ServiceVersionBean getServiceVersion(String organizationId, String serviceId, String version) throws StorageException;
    public InputStream getServiceDefinition(ServiceVersionBean serviceVersion) throws StorageException;
    public PlanBean getPlan(String organizationId, String id) throws StorageException;
    public PlanVersionBean getPlanVersion(String organizationId, String planId, String version) throws StorageException;
    public PolicyBean getPolicy(PolicyType type, String organizationId, String entityId, String version, Long id) throws StorageException;
    public GatewayBean getGateway(String id) throws StorageException;
    public PluginBean getPlugin(long id) throws StorageException;
    public PluginBean getPlugin(String groupId, String artifactId) throws StorageException;
    public PolicyDefinitionBean getPolicyDefinition(String id) throws StorageException;
    public AnnouncementBean getServiceAnnouncement(Long id) throws StorageException;
    public SupportBean getServiceSupport(Long id) throws StorageException;
    public SupportComment getServiceSupportComment(Long id) throws StorageException;
    public WhitelistBean getWhitelistRecord(String id) throws StorageException;
    public BlacklistBean getBlacklistRecord(String id) throws StorageException;
    public EventBean getEvent(Long id) throws StorageException;
    public MailTemplateBean getMailTemplate (MailTopic mailTopic) throws StorageException;
    public ManagedApplicationBean getManagedApplicationBean(Long id) throws StorageException;
    public ManagedApplicationBean getManagedApplicationBean (AppIdentifier app) throws StorageException;
    public KeyMappingBean getKeyMappingBean(String fromSpecType, String toSpecType, String fromSpecClaim)throws StorageException;
    public DefaultsBean getDefaults(String id) throws StorageException;
    public List<ConfigBean> getDefaultConfig() throws StorageException;
    public ServiceBrandingBean getBranding(String id) throws StorageException;
    public IDPBean getIDP(String id) throws StorageException;
    public KeystoreBean getKeystore(String kid) throws StorageException;
    public MailProviderBean getMailProvider(Long id) throws StorageException;

    /*
     * Anything that doesn't fall into the above categories!
     */
    public void reorderPolicies(PolicyType type, String organizationId, String entityId, String entityVersion, List<Long> newOrder) throws StorageException;

    /**
     * Returns all organizations, this method is exceptional list of orgs for logged-in admin.
     * In other cases we use the currentuser or search endpoints.
     *
     * @return
     * @throws StorageException
     */
    public Set<String> getAllOrganizations()throws StorageException;

    /**
     * Returns all brandings
     *
     * @return a set of ServiceBrandingBeans
     * @throws StorageException
     */
    public Set<ServiceBrandingBean> getAllBrandings() throws StorageException;

    /**
     * Get or create methods
     */

    public IDPBean getOrCreateIDP(IDPBean idp) throws StorageException;
}
