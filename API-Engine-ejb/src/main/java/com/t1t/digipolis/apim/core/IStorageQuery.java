package com.t1t.digipolis.apim.core;

import com.t1t.digipolis.apim.beans.announcements.AnnouncementBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.audit.AuditEntityType;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.digipolis.apim.beans.authorization.OAuthAppBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.events.EventType;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.idp.IDPBean;
import com.t1t.digipolis.apim.beans.idp.KeyMappingBean;
import com.t1t.digipolis.apim.beans.idp.KeystoreBean;
import com.t1t.digipolis.apim.beans.iprestriction.BlacklistBean;
import com.t1t.digipolis.apim.beans.iprestriction.WhitelistBean;
import com.t1t.digipolis.apim.beans.mail.MailProviderBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.digipolis.apim.beans.operation.OperatingBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.plans.PlanBean;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.ServiceBean;
import com.t1t.digipolis.apim.beans.services.ServiceStatus;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.*;
import com.t1t.digipolis.apim.beans.support.SupportBean;
import com.t1t.digipolis.apim.beans.support.SupportComment;
import com.t1t.digipolis.apim.core.exceptions.StorageException;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Specific querying of the storage layer.
 *
 */
public interface IStorageQuery {

    /**
     * Lists all of the Plugins.
     *
     * @return list of plugins
     * @throws com.t1t.digipolis.apim.core.exceptions.StorageException if a storage problem occurs while storing a bean.
     */
    public List<PluginSummaryBean> listPlugins() throws StorageException;

    /**
     * Lists all of the Gateways.
     *
     * @return list of gateways
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<GatewaySummaryBean> listGateways() throws StorageException;

    /**
     * Returns the default gateway, in case there is only one gateway.
     * This is a really stupid implementation, returning the first in a list.
     *
     * @return
     * @throws StorageException
     */
    public GatewayBean getDefaultGateway() throws StorageException;

    /**
     * List all the gatewaybeans with all available information.
     *
     * @return
     * @throws StorageException
     */
    public List<GatewayBean> listGatewayBeans() throws StorageException;

    /**
     * Finds organizations by the provided criteria.
     *
     * @param criteria search criteria search criteria
     * @return found orgs
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public SearchResultsBean<OrganizationSummaryBean> findOrganizations(SearchCriteriaBean criteria) throws StorageException;

    /**
     * Finds applications by the provided criteria.
     *
     * @param criteria search criteria
     * @return found applications
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public SearchResultsBean<ApplicationSummaryBean> findApplications(SearchCriteriaBean criteria) throws StorageException;

    /**
     * Finds services by the provided criteria.
     *
     * @param criteria search criteria
     * @return found services
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public SearchResultsBean<ServiceSummaryBean> findServices(SearchCriteriaBean criteria) throws StorageException;

    /**
     * Find published service versions by service name
     *
     * @param name
     * @return found service versions
     * @throws StorageException
     */
    public List<ServiceVersionBean> findPublishedServiceVersionsByServiceName(String name) throws StorageException;

    /**
     * Find services in given status.
     *
     * @param status
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> findServiceByStatus(ServiceStatus status) throws StorageException;

    /**
     * Find the service versions which are available on the gateway(s)
     *
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> findGatewayServiceVersions() throws StorageException;

    /**
     * Returns all categories in a list.
     *
     * @return
     * @throws StorageException
     */
    public Set<String> findAllUniqueCategories() throws StorageException;

    /**
     * Returns all categories that are set on PUBLISHED service versions in a list.
     *
     * @return
     * @throws StorageException
     */
    public Set<String> findAllUniquePublishedCategories() throws StorageException;

    /**
     * Returns all published services within given categories.
     *
     * @param categories
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> findAllServicesWithCategory(List<String> categories) throws StorageException;

    /**
     * Finds plans (within an organization) with the given criteria.
     *
     * @param organizationId the organization id
     * @param criteria       search criteria
     * @return found plans
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public SearchResultsBean<PlanSummaryBean> findPlans(String organizationId, SearchCriteriaBean criteria) throws StorageException;

    /**
     * Gets the audit log for an entity.
     *
     * @param organizationId the organization id
     * @param entityId       the entity id
     * @param entityVersion  the entity version
     * @param type           the type
     * @param paging         the paging specification
     * @return audit entity
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public <T> SearchResultsBean<AuditEntryBean> auditEntity(String organizationId, String entityId, String entityVersion, Class<T> type, PagingBean paging) throws StorageException;

    /**
     * Gets the audit log for a user.
     *
     * @param userId the user id
     * @param paging the paging specification
     * @return audit user
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public <T> SearchResultsBean<AuditEntryBean> auditUser(String userId, PagingBean paging) throws StorageException;

    /**
     * Returns summary info for all organizations in the given set.
     *
     * @param organizationIds the organization ids
     * @return list of orgs
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<OrganizationSummaryBean> getOrgs(Set<String> organizationIds) throws StorageException;

    /**
     * Returns summary info for all applications in all organizations in the given set.
     *
     * @param organizationIds the organization ids
     * @return list of applications in orgs
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ApplicationSummaryBean> getApplicationsInOrgs(Set<String> organizationIds) throws StorageException;

    /**
     * Returns summary info for all applications in the given organization.
     *
     * @param organizationId the organization id
     * @return list of applications
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ApplicationSummaryBean> getApplicationsInOrg(String organizationId) throws StorageException;

    /**
     * Returns all application versions for a given app.
     *
     * @param organizationId the organization id
     * @param applicationId  the application id
     * @return list of application versions
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ApplicationVersionSummaryBean> getApplicationVersions(String organizationId, String applicationId)
            throws StorageException;

    /**
     * Returns all Contracts for the application.
     *
     * @param organizationId the organization id
     * @param applicationId  the application id
     * @param version        the version
     * @return list of application contracts
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ContractSummaryBean> getApplicationContracts(String organizationId, String applicationId, String version)
            throws StorageException;

    /**
     * Returns the application version based on the OAuth2 client_id and client_secret.
     *
     * @param appOAuthId
     * @param appOAuthSecret
     * @return
     * @throws StorageException
     */
    public ApplicationVersionBean getApplicationForOAuth(String appOAuthId, String appOAuthSecret) throws StorageException;


    /**
     * Returns the application version based on the OAuth client_id
     *
     * @param clientId
     * @return
     * @throws StorageException
     */
    public ApplicationVersionBean getApplicationForOAuth(String clientId) throws StorageException;

    /**
     * Returns the api registry for the given application.
     *
     * @param organizationId the organization id
     * @param applicationId  the application id
     * @param version        the version
     * @return the registry bean
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public ApiRegistryBean getApiRegistry(String organizationId, String applicationId, String version)
            throws StorageException;

    /**
     * Returns summary info for all services in all organizations in the given set.
     *
     * @param organizationIds the organization ids
     * @return services in orgs
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ServiceSummaryBean> getServicesInOrgs(Set<String> organizationIds) throws StorageException;

    /**
     * Returns summary info for all services in the given organization.
     *
     * @param organizationId the organization id
     * @return list of services in org
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ServiceSummaryBean> getServicesInOrg(String organizationId) throws StorageException;

    /**
     * Returns all service versions for a given service.
     *
     * @param organizationId the organization id
     * @param serviceId      the service id
     * @return list of service versions
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ServiceVersionSummaryBean> getServiceVersions(String organizationId, String serviceId) throws StorageException;

    /**
     * Returns the service plans configured for the given service version.
     *
     * @param organizationId the organization id
     * @param serviceId      the service id
     * @param version        the version
     * @return list of service plans
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ServicePlanSummaryBean> getServiceVersionPlans(String organizationId, String serviceId, String version) throws StorageException;

    /**
     * Returns summary info for all plans in all organizations in the given set.
     *
     * @param organizationIds the organization ids
     * @return list of plans in orgs
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<PlanSummaryBean> getPlansInOrgs(Set<String> organizationIds) throws StorageException;

    /**
     * Returns summary info for all plans in the given organization.
     *
     * @param organizationId the organization id
     * @return list of plans in org
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<PlanSummaryBean> getPlansInOrg(String organizationId) throws StorageException;

    /**
     * Returns all plan versions for a given plan.
     *
     * @param organizationId the organization id
     * @param planId         the plan id
     * @return list of plan versions
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<PlanVersionSummaryBean> getPlanVersions(String organizationId, String planId)
            throws StorageException;

    /**
     * Returns all policies of the given type for the given entity/version.  This could be
     * any of Application, Plan, Service.
     *
     * @param organizationId the organization id
     * @param entityId       the entity id
     * @param version        the version
     * @param type           the type
     * @return list of policies
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<PolicySummaryBean> getPolicies(String organizationId, String entityId, String version, PolicyType type) throws StorageException;

    /**
     * Lists the policy definitions in the system.
     *
     * @return list of policy definitions
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<PolicyDefinitionSummaryBean> listPolicyDefinitions() throws StorageException;

    /**
     * Gets a list of contracts for the given service.  This is paged.
     *
     * @param organizationId the organization id
     * @param serviceId      the service id
     * @param version        the version
     * @param page           the page
     * @param pageSize       the paging size
     * @return list of service contracts
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ContractSummaryBean> getServiceContracts(String organizationId, String serviceId, String version, int page, int pageSize) throws StorageException;

    /**
     * Gets a list of contracts for the given service.  This is not paged.
     *
     * @param organizationId the organization id
     * @param serviceId      the service id
     * @param version        the version
     * @return list of service contracts
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<ContractBean> getServiceContracts(String organizationId, String serviceId, String version) throws StorageException;

    /**
     * Gets a list of contracts for the given plan version. This is not paged.
     *
     * @param planVersionId
     * @return
     * @throws StorageException
     */
    public List<ContractBean> getPlanVersionContracts(Long planVersionId) throws StorageException;

    /**
     * Returns the largest order index value for the policies assigned to the
     * given entity.
     *
     * @param organizationId the organization id
     * @param entityId       the entity id
     * @param entityVersion  the entity version
     * @param type           the type
     * @return largest order index
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public int getMaxPolicyOrderIndex(String organizationId, String entityId, String entityVersion, PolicyType type) throws StorageException;

    /**
     * Lists all of the policy definitions contributed via a particular plugin.
     *
     * @param pluginId the plugin id
     * @return list of plugin policy defs
     * @throws StorageException if a storage problem occurs while storing a bean.
     */
    public List<PolicyDefinitionSummaryBean> listPluginPolicyDefs(Long pluginId) throws StorageException;

    /**
     * Lists all OAuth credentials associated to an application version.
     * The record contains information for the service version assigned with the credentials.
     *
     * @param appVersionId
     * @return
     * @throws StorageException
     */
    public List<OAuthAppBean> listApplicationOAuthCredentials(Long appVersionId) throws StorageException;

    /**
     * Lists all announcement related to a service in a given organization.
     *
     * @param organizationId
     * @param serviceId
     * @return
     * @throws StorageException
     */
    public List<AnnouncementBean> listServiceAnnouncements(String organizationId, String serviceId) throws StorageException;

    /**
     * Lists all support tickets available for a given service.
     *
     * @param organizationId
     * @param serviceId
     * @return
     * @throws StorageException
     */
    public List<SupportBean> listServiceSupportTickets(String organizationId, String serviceId) throws StorageException;

    /**
     * Lists all support ticket comments for a given service ticket.
     *
     * @param supportBeanId
     * @return
     * @throws StorageException
     */
    public List<SupportComment> listServiceSupportComment(Long supportBeanId) throws StorageException;

    /**
     * List all available marketplaces for this environment.
     * The list is needed in the publisher to set the Service version's visibility throughout supported marketplaces.
     *
     * @return
     * @throws StorageException
     */
    public List<ManagedApplicationBean> listAvailableMarkets() throws StorageException;

    /**
     * Returns the default whitelist records. This can be used for an implicit IP Restriction policy.
     * This is the case for example when exposing services, that are not visible in the API marketplace, but should
     * be exposed to the outside world with IP restrictions.
     *
     * @return
     * @throws StorageException
     */
    public List<WhitelistBean> listWhitelistRecords() throws StorageException;

    /**
     * Returns the default blacklist records. This can be used for an implicit IP Restriction policy.
     * This is the case for example when exposing services, that are not visible in the API marketplace, but should
     * be exposed to the outside world with IP restrictions.
     *
     * @return
     * @throws StorageException
     */
    public List<BlacklistBean> listBlacklistRecords() throws StorageException;

    /**
     * Returns an ACL policybean for a given application
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @return
     * @throws StorageException
     */
    public PolicyBean getApplicationACLPolicy(String organizationId, String applicationId, String version, Long contractId, String gatewayId) throws StorageException;

    /**
     * Returns a list of application policies corresponding to a contract
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @param contractId
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> getApplicationVersionContractPolicies(String organizationId, String applicationId, String version, Long contractId) throws StorageException;

    /**
     * Returns a list of all ManagedApplications of Marketplace type
     *
     * @return List of ManagedApplicationBeans
     * @throws StorageException
     */
    public List<ManagedApplicationBean> getManagedApps() throws StorageException;

    /**
     * Returns a List of ACL Policies associated with marketplaces
     *
     * @return List of Policies
     * @throws StorageException
     */
    public List<PolicyBean> getManagedAppACLPolicies(String organizationId, String serviceId, String version) throws StorageException;

    /**
     * Returns a list of all non-retired service versions
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> getAllNonRetiredServiceVersions() throws StorageException;

    /**
     * Returns all application versions
     *
     * @return List of ApplicationVersionBeans
     * @throws StorageException
     */
    public List<ApplicationVersionBean> findAllApplicationVersions() throws StorageException;

    /**
     * Returns all applications
     *
     * @return
     * @throws StorageException
     */
    public List<ApplicationBean> findAllApplications() throws StorageException;

    /**
     * Returns all plans (plan version containers) for a given organization.
     *
     * @param organizationId
     * @return
     * @throws StorageException
     */
    public List<PlanBean> findAllPlans(String organizationId) throws StorageException;

    /**
     * Returns all planversions for a given organization and given plan.
     * A plan is a collections of planversion objects.
     *
     * @param organizationId
     * @param planId
     * @return
     * @throws StorageException
     */
    public List<PlanVersionBean> findAllPlanVersionBeans(String organizationId, String planId) throws StorageException;

    /**
     * Returns service version beans for a specific availability
     *
     * @param
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> findServiceVersionsByAvailability(String prefix) throws StorageException;

    /**
     * Returns a unique event by origin, destination and type - ignoring the id
     * As unique key is applied on this triplet.
     *
     * @param eventBean
     * @return
     * @throws StorageException
     */
    public EventBean getUniqueEvent(EventBean eventBean) throws StorageException;

    /**
     * Returns all incoming events for given destination
     *
     * @param destination
     * @return
     * @throws StorageException
     */
    public List<EventBean> getAllIncomingEvents(String destination) throws StorageException;

    /**
     * Returns all outgoing events for given origin
     *
     * @param origin
     * @return
     * @throws StorageException
     */
    public List<EventBean> getAllOutgoingEvents(String origin) throws StorageException;

    /**
     * Returns incoming events by type
     *
     * @param destination
     * @param type
     * @return
     * @throws StorageException
     */
    public List<EventBean> getIncomingEventsByType(String destination, EventType type) throws StorageException;

    /**
     * Returns outgoing events by type
     *
     * @param origin
     * @param type
     * @return
     * @throws StorageException
     */
    public List<EventBean> getOutgoingEventsByType(String origin, EventType type) throws StorageException;

    /**
     * Returns event by given origin, destination and type.
     *
     * @param origin
     * @param destination
     * @param type
     * @return
     * @throws StorageException
     */
    public EventBean getEventByOriginDestinationAndType(String origin, String destination, EventType type) throws StorageException;

    /**
     * Returns the count for published service for a given organization.
     *
     * @param orgId
     * @return
     * @throws StorageException
     */
    public Integer getPublishedServiceCountForOrg(String orgId) throws StorageException;

    /**
     * Returns the count for locked plans for a given organization.
     *
     * @param orgId
     * @return
     * @throws StorageException
     */
    public Integer getLockedPlanCountForOrg(String orgId) throws StorageException;

    /**
     * Returns the count for registered applications for a given organization.
     *
     * @param orgId
     * @return
     * @throws StorageException
     */
    public Integer getRegisteredApplicationCountForOrg(String orgId) throws StorageException;

    /**
     * Return the count for member for a given organization.
     *
     * @param orgId
     * @return
     * @throws StorageException
     */
    public Integer getMemberCountForOrg(String orgId) throws StorageException;

    /**
     * Return the event count for a given organization.
     *
     * @param orgId
     * @return
     * @throws StorageException
     */
    public Integer getEventCountForOrg(String orgId) throws StorageException;

    /**
     * Get all incoming non-action events
     *
     * @param destination
     * @return
     * @throws StorageException
     */
    public List<EventBean> getAllIncomingNonActionEvents(String destination) throws StorageException;

    /**
     * Get all incoming action events
     *
     * @param destination
     * @return
     * @throws StorageException
     */
    public List<EventBean> getAllIncomingActionEvents(String destination) throws StorageException;

    /**
     * Delete all events for an entity
     *
     * @param entityId
     * @throws StorageException
     */
    public void deleteAllEventsForEntity(String entityId) throws StorageException;

    /**
     * Find latest service versions by status
     *
     * @param status
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> findLatestServiceVersionByStatus(ServiceStatus status) throws StorageException;

    /**
     * Find latest service versions by status and servicename
     *
     * @param serviceName
     * @param status
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> findLatestServiceVersionByStatusAndServiceName(String serviceName, ServiceStatus status) throws StorageException;

    /**
     * Find latest service versions by category
     *
     * @param categories
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> findLatestServicesWithCategory(List<String> categories) throws StorageException;

    /**
     * Resolves a managed application by its apikey.
     * This is used as a fallback scenario in request filters (in order to set the app context).
     *
     * @param apiKey
     * @return
     * @throws StorageException
     */
    public ManagedApplicationBean resolveManagedApplicationByAPIKey(String apiKey) throws StorageException;

    /**
     * Returns the managed applciation by given prefix.
     * A prefix is the context defined by a managed applications, this is a unique value.
     * The unique value is used for applications and services to define their context.
     * Prefixes are used in order to scope/determine visibility.
     *
     * @param prefix
     * @return
     * @throws StorageException
     */
    public ManagedApplicationBean findManagedApplication(String prefix) throws StorageException;

    /**
     * Find all managed applications by type. A type can be InternalMarketplace.
     * When this is the case, all managed applications will be returned that are interal marketplaces.
     *
     * @param type
     * @return
     * @throws StorageException
     */
    public List<ManagedApplicationBean> findManagedApplication(ManagedApplicationTypes type) throws StorageException;

    /**
     * Returns all managed applications without filtering.
     *
     * @return
     * @throws StorageException
     */
    public List<ManagedApplicationBean> findManagedApplications() throws StorageException;

    /**
     * Return organizations that hold a contract with a service
     *
     * @param service
     * @return
     * @throws StorageException
     */
    public Set<OrganizationBean> getServiceContractHolders(ServiceBean service) throws StorageException;

    /**
     * Get service based on its base path
     *
     * @param organizationId
     * @param basepath
     * @return
     * @throws StorageException
     */
    public ServiceBean getServiceByBasepath(String organizationId, String basepath) throws StorageException;

    /**
     * Resolve an API key to the application it belongs to
     *
     * @param apiKey
     * @return
     * @throws StorageException
     */
    public ApplicationVersionSummaryBean resolveApplicationVersionByAPIKey(String apiKey) throws StorageException;

    /**
     * Delete all ACL policies
     *
     * @throws StorageException
     */
    public void deleteAclPolicies() throws StorageException;

    /**
     * Delete all contract policies
     *
     * @throws StorageException
     */
    public void deleteContractPolicies() throws StorageException;

    /**
     * Update an application's API key
     *
     * @param avb
     * @param apiKey
     * @throws StorageException
     */
    public void updateApplicationVersionApiKey(ApplicationVersionBean avb, String apiKey) throws StorageException;

    /**
     * Get all non-retired application versions
     *
     * @return
     * @throws StorageException
     */
    public List<ApplicationVersionBean> getAllNonRetiredApplicationVersions() throws StorageException;

    /**
     * Get all events related to an organization. Used in the process of organization deletion
     *
     * @param orgId
     * @return
     * @throws StorageException
     */
    public List<EventBean> getAllEventsRelatedToOrganization(String orgId) throws StorageException;

    /**
     * Get all contracts for a service
     *
     * @param service
     * @return
     * @throws StorageException
     */
    public List<ContractBean> getServiceContracts(ServiceBean service) throws StorageException;

    /**
     * Get service versions in an organization by status
     *
     * @param organizationId
     * @param status
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> getServiceVersionsInOrgByStatus(String organizationId, ServiceStatus status) throws StorageException;

    /**
     * Return policy beans for an entity
     *
     * @param organizationId
     * @param entityId
     * @param version
     * @param type
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> listPoliciesForEntity(String organizationId, String entityId, String version, PolicyType type) throws StorageException;

    /**
     * Return all audit entries for entity
     *
     * @param organizationId
     * @param entityId
     * @param version
     * @param type
     * @return
     * @throws StorageException
     */
    public List<AuditEntryBean> listAuditEntriesForEntity(String organizationId, String entityId, String version, AuditEntityType type) throws StorageException;

    /**
     * Return all organizations
     *
     * @return
     * @throws StorageException
     */
    public List<OrganizationBean> getAllOrgs() throws StorageException;

    /**
     * Returns all key mapping available for all registered specifications
     *
     * @return
     * @throws StorageException
     */
    public List<KeyMappingBean> getAllKeyMapping() throws StorageException;

    /**
     * Returns key mapping collection for given fromSpec, toSpec.
     * This returns for example all key/claim mappings needed from SAML to JWT.
     *
     * @param fromSpec
     * @param toSpec
     * @return
     * @throws StorageException
     */
    public List<KeyMappingBean> getKeyMapping(String fromSpec, String toSpec) throws StorageException;

    /**
     * Return prefixes for managed applications of given types
     *
     * @param types
     * @return
     * @throws StorageException
     */
    public Set<String> getManagedAppPrefixesForTypes(List<ManagedApplicationTypes> types) throws StorageException;

    /**
     * Returns managed applications for give types
     * @param types
     * @return
     * @throws StorageException
     */
    public List<ManagedApplicationBean> getManagedAppForTypes(List<ManagedApplicationTypes> types) throws StorageException;

    /**
     * Returns all gateways
     *
     * @return
     * @throws StorageException
     */
    public List<GatewayBean> getAllGateways() throws StorageException;

    /**
     * Get policies for an entity based on definition id
     *
     * @param organizationId
     * @param entityId
     * @param version
     * @param type
     * @param definitionId
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> getEntityPoliciesByDefinitionId(String organizationId, String entityId, String version, PolicyType type, Policies definitionId) throws StorageException;

    /**
     * Delete all events related to an announcement
     *
     * @param announcementId
     * @throws StorageException
     */
    public void deleteAllEventsForAnnouncement(Long announcementId) throws StorageException;

    /**
     * Retrieve all published service versions for a service
     *
     * @param service
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> getServiceVersionsByServiceAndStatus(ServiceBean service, ServiceStatus status) throws StorageException;

    /**
     * Retrieve a service by name
     *
     * @param name
     * @return
     * @throws StorageException
     */
    public ServiceBean getServiceByName(String name) throws StorageException;

    /**
     * Retrieve Managed Applications by Type
     *
     * @param type
     * @return
     * @throws StorageException
     */
    public Set<ManagedApplicationBean> getManagedApplicationsByType(ManagedApplicationTypes type) throws StorageException;

    /**
     * Get policy definitions that should be applied by default
     *
     * @return
     * @throws StorageException
     */
    public Set<PolicyDefinitionBean> getDefaultServicePolicyDefs() throws StorageException;

    /**
     * Get policy definitions that are service scoped
     *
     * @return
     * @throws StorageException
     */
    public Set<PolicyDefinitionBean> getServiceScopedPolicyDefs() throws StorageException;

    /**
     * Get policy definitions that are plan scoped
     *
     * @return
     * @throws StorageException
     */
    public Set<PolicyDefinitionBean> getplanScopedPolicyDefs() throws StorageException;

    /**
     * Get all application versions that hold a contract with a particular service version
     *
     * @param svb
     * @return
     * @throws StorageException
     */
    public Set<ApplicationVersionBean> getAppVersionContractHoldersForServiceVersion(ServiceVersionBean svb) throws StorageException;

    /**
     * Get all ACL policies that are related to a Service's own ACL policy
     *
     * @param svb
     * @return
     * @throws StorageException
     */
    public Set<PolicyBean> getNonServiceACLPoliciesForServiceVersion(ServiceVersionBean svb) throws StorageException;

    /**
     * Get all contracts for an application version
     *
     * @param avb
     * @return
     * @throws StorageException
     */
    public List<ContractBean> getApplicationVersionContracts(ApplicationVersionBean avb) throws StorageException;

    /**
     * Get service versions for a specific service that match the proviced service status
     *
     * @param status
     * @param service
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> getServiceVersionByStatusForService(Set<ServiceStatus> status, ServiceBean service) throws StorageException;

    /**
     * Get contract if it exists between service version and application version by service and application oauth client id
     *
     * @param orgId
     * @param serviceId
     * @param version
     * @param clientId
     * @return
     * @throws StorageException
     */
    public ContractBean getContractByServiceVersionAndOAuthClientId(String orgId, String serviceId, String version, String clientId) throws StorageException;

    /**
     * Returns a bean with the maintenance mode status and message
     *
     * @return
     * @throws StorageException
     */
    public OperatingBean getMaintenanceModeStatus() throws StorageException;

    /**
     * Returns a list of all unpublished policies (i.e. policies that do not have a kong plugin id value)
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> getDefaultUnpublishedPolicies() throws StorageException;

    /**
     * Delete all tokens that are backed up
     * @throws StorageException
     */
    public void deleteAllOAuthTokens() throws StorageException;

    /**
     * Retrieves all oauth tokens
     * @throws StorageException
     */
    public List<OAuth2TokenBean> getAllOAuthTokens() throws StorageException;

    /**
     * Returns a list of services that are either published or deprecated
     * @return
     * @throws StorageException
     */
    public List<ServiceVersionBean> getPublishedServiceVersions() throws StorageException;

    /**
     * Retrieves a list of acl policies for consent apps
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> getConsentACLPolicies() throws StorageException;

    /**
     * Returns a list of gateways with services  where an application
     * @param avb
     * @return
     * @throws StorageException
     */
    public Set<String> getGatewayIdsForApplicationVersionContracts(ApplicationVersionBean avb) throws StorageException;

    /**
     * Retrieves all policies for a given type
     * @param type
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> getAllPoliciesByType(PolicyType type) throws StorageException;

    /**
     * Retrieve policies associated with a contract
     * @param contractId
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> getContractPolicies(Long contractId) throws StorageException;

    /**
     * Retrieve a list of contract policies that correspond to a plan policy
     * @param planPolicyId
     * @param policyDefinition
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> getContractPoliciesForPlanPolicy(Long planPolicyId, Policies policyDefinition) throws StorageException;

    /**
     * Retrtieve a map of plan versions and the contracts that use the plan
     * @return
     * @throws StorageException
     */
    public Map<PlanVersionBean, List<ContractBean>> getPlanVersionContractMap() throws StorageException;

    /**
     * Get all policies for a given plan
     * @param pvb
     * @return
     * @throws StorageException
     */
    public List<PolicyBean> getPlanPolicies(PlanVersionBean pvb) throws StorageException;

    /**
     * Returns a list of all contracts
     * @return
     * @throws StorageException
     */
    public List<ContractBean> getAllContracts() throws StorageException;

    /**
     * Get a policy for an entity based on contract id and definition
     * @param organizationId
     * @param entityId
     * @param version
     * @param polDef
     * @param contractId
     * @param gatewayId
     * @return
     * @throws StorageException
     */
    public PolicyBean getPolicyByContractIdAndDefinitionForEntity(String organizationId, String entityId, String version, String polDef, Long contractId, String gatewayId) throws StorageException;

    /**
     * Get policy based in the Kong plugin ID
     * @param kongPluginId
     * @return
     * @throws StorageException
     */
    public PolicyBean getPolicyByKongPluginId(String kongPluginId) throws StorageException;

    /**
     * Get a count for the number of tokens that are backed up
     * @return
     * @throws StorageException
     */
    public Long getOAuth2TokenCount() throws StorageException;

    /**
     * Returns the default IDP
     * @return
     * @throws StorageException
     */
    public IDPBean getDefaultIdp() throws StorageException;

    /**
     * Returns the default keystore
     * @return
     * @throws StorageException
     */
    public KeystoreBean getDefaultKeystore()throws StorageException;

    /**
     * Returns the default mail provider
     * @return
     * @throws StorageException
     */
    public MailProviderBean getDefaultMailProvider() throws StorageException;
}