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
import com.t1t.apim.beans.idm.IdpIssuerBean;
import com.t1t.apim.beans.mail.MailTemplateBean;
import com.t1t.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.apim.beans.operation.OperatingBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.beans.plans.PlanBean;
import com.t1t.apim.beans.plans.PlanVersionBean;
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
 */
public interface IStorage {
    /*
     * Various creation methods.  These are called by the REST layer to create stuff.
     */

    void createOrganization(OrganizationBean organization) throws StorageException;

    void createApplication(ApplicationBean application) throws StorageException;

    void createApplicationVersion(ApplicationVersionBean version) throws StorageException;

    void createContract(ContractBean contract) throws StorageException;

    void createService(ServiceBean service) throws StorageException;

    void createServiceVersion(ServiceVersionBean version) throws StorageException;

    void createPlan(PlanBean plan) throws StorageException;

    void createPlanVersion(PlanVersionBean version) throws StorageException;

    void createPolicy(PolicyBean policy) throws StorageException;

    void createGateway(GatewayBean gateway) throws StorageException;

    void createPolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException;

    void createAuditEntry(AuditEntryBean entry) throws StorageException;

    void createServiceAnnouncement(AnnouncementBean announcement) throws StorageException;

    void createServiceSupport(SupportBean supportBean) throws StorageException;

    void createServiceSupportComment(SupportComment commentBean) throws StorageException;

    void createEvent(EventBean eventBean) throws StorageException;

    void createMailTemplate(MailTemplateBean mailTemplateBean) throws Exception;

    void createManagedApplication(ManagedApplicationBean manapp) throws Exception;

    void createDefaults(DefaultsBean defaultsBean) throws StorageException;

    void createConfig(ConfigBean config) throws StorageException;

    void createBranding(ServiceBrandingBean branding) throws StorageException;

    void createOAuth2Token(OAuth2TokenBean token) throws StorageException;

    void createIdpIssuer(IdpIssuerBean idpIssuer) throws StorageException;

    /*
     * Various update methods.  These are called by the REST layer to update stuff.
     */

    void updateOrganization(OrganizationBean organization) throws StorageException;

    void updateApplication(ApplicationBean application) throws StorageException;

    void updateApplicationVersion(ApplicationVersionBean version) throws StorageException;

    void updateService(ServiceBean service) throws StorageException;

    void updateServiceVersion(ServiceVersionBean version) throws StorageException;

    void updateServiceDefinition(ServiceVersionBean version, InputStream definitionStream) throws StorageException;

    void updatePlan(PlanBean plan) throws StorageException;

    void updatePlanVersion(PlanVersionBean version) throws StorageException;

    void updatePolicy(PolicyBean policy) throws StorageException;

    void updateGateway(GatewayBean gateway) throws StorageException;

    void updatePolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException;

    void updateServiceAnnouncement(AnnouncementBean announcement) throws StorageException;

    void updateServiceSupport(SupportBean supportBean) throws StorageException;

    void updateServiceSupportComment(SupportComment commentBean) throws StorageException;

    void updateContract(ContractBean contractBean) throws StorageException;

    void updateMailTemplate(MailTemplateBean mailTemplateBean) throws StorageException;

    void updateEvent(EventBean event) throws StorageException;

    void updateManagedApplication(ManagedApplicationBean manapp) throws StorageException;

    void updateAuditEntry(AuditEntryBean audit) throws StorageException;

    void updateDefaults(DefaultsBean defaultsBean) throws StorageException;

    void updateConfig(ConfigBean config) throws StorageException;

    void updateBranding(ServiceBrandingBean branding) throws StorageException;

    void updateOperatingBean(OperatingBean operatingBean) throws StorageException;

    void updateOAuth2TokenBean(OAuth2TokenBean token) throws StorageException;

    void updateIdpIssuer(IdpIssuerBean idpIssuer) throws StorageException;

    /*
     * Various delete methods.  These are called by the REST layer to delete stuff.
     */

    void deleteOrganization(OrganizationBean organization) throws StorageException;

    void deleteApplication(ApplicationBean application) throws StorageException;

    void deleteApplicationVersion(ApplicationVersionBean version) throws StorageException;

    void deleteContract(ContractBean contract) throws StorageException;

    void deleteService(ServiceBean service) throws StorageException;

    void deleteServiceVersion(ServiceVersionBean version) throws StorageException;

    void deleteServiceDefinition(ServiceVersionBean version) throws StorageException;

    void deletePlan(PlanBean plan) throws StorageException;

    void deletePlanVersion(PlanVersionBean version) throws StorageException;

    void deletePolicy(PolicyBean policy) throws StorageException;

    void deleteGateway(GatewayBean gateway) throws StorageException;

    void deletePolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException;

    void deleteServiceAnnouncement(AnnouncementBean announcement) throws StorageException;

    void deleteServiceSupport(SupportBean supportBean) throws StorageException;

    void deleteServiceSupportComment(SupportComment commentBean) throws StorageException;

    void deleteEvent(EventBean eventBean) throws StorageException;

    void deleteMailTemplate(MailTemplateBean mailTemplateBean) throws StorageException;

    void deleteManagedApplication(ManagedApplicationBean manapp) throws StorageException;

    void deleteDefaults(DefaultsBean defaultsBean) throws StorageException;

    void deleteConfig(ConfigBean configBean) throws StorageException;

    void deleteBranding(ServiceBrandingBean branding) throws StorageException;

    void deleteOAuth2Token(OAuth2TokenBean token) throws StorageException;

    void deleteIdpIssuer(IdpIssuerBean idpIssuer) throws StorageException;

    /*
     * Various get methods.  These are called by the REST layer to get stuff.
     */

    OrganizationBean getOrganization(String id) throws StorageException;

    ApplicationBean getApplication(String organizationId, String id) throws StorageException;

    ApplicationVersionBean getApplicationVersion(String organizationId, String applicationId, String version) throws StorageException;

    ContractBean getContract(Long id) throws StorageException;

    ServiceBean getService(String organizationId, String id) throws StorageException;

    ServiceVersionBean getServiceVersion(String organizationId, String serviceId, String version) throws StorageException;

    InputStream getServiceDefinition(ServiceVersionBean serviceVersion) throws StorageException;

    PlanBean getPlan(String organizationId, String id) throws StorageException;

    PlanVersionBean getPlanVersion(String organizationId, String planId, String version) throws StorageException;

    PolicyBean getPolicy(PolicyType type, String organizationId, String entityId, String version, Long id) throws StorageException;

    GatewayBean getGateway(String id) throws StorageException;

    PolicyDefinitionBean getPolicyDefinition(String id) throws StorageException;

    AnnouncementBean getServiceAnnouncement(Long id) throws StorageException;

    SupportBean getServiceSupport(Long id) throws StorageException;

    SupportComment getServiceSupportComment(Long id) throws StorageException;

    EventBean getEvent(Long id) throws StorageException;

    MailTemplateBean getMailTemplate(MailTopic mailTopic) throws StorageException;

    ManagedApplicationBean getManagedApplicationBean(Long id) throws StorageException;

    ManagedApplicationBean getManagedApplicationBean(AppIdentifier app) throws StorageException;

    DefaultsBean getDefaults(String id) throws StorageException;

    List<ConfigBean> getDefaultConfig() throws StorageException;

    ServiceBrandingBean getBranding(String id) throws StorageException;

    IdpIssuerBean getIdpIssuer(String id) throws StorageException;

    /*
     * Anything that doesn't fall into the above categories!
     */
    void reorderPolicies(PolicyType type, String organizationId, String entityId, String entityVersion, List<Long> newOrder) throws StorageException;

    /**
     * Returns all organizations, this method is exceptional list of orgs for logged-in admin.
     * In other cases we use the currentuser or search endpoints.
     *
     * @return
     * @throws StorageException
     */
    Set<String> getAllOrganizations() throws StorageException;

    /**
     * Returns all brandings
     *
     * @return a set of ServiceBrandingBeans
     * @throws StorageException
     */
    Set<ServiceBrandingBean> getAllBrandings() throws StorageException;
}
