package com.t1t.digipolis.apim.jpa;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.announcements.AnnouncementBean;
import com.t1t.digipolis.apim.beans.apps.AppIdentifier;
import com.t1t.digipolis.apim.beans.apps.ApplicationBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationStatus;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.audit.AuditEntityType;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.authorization.OAuthAppBean;
import com.t1t.digipolis.apim.beans.config.ConfigBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.defaults.DefaultsBean;
import com.t1t.digipolis.apim.beans.events.EventBean;
import com.t1t.digipolis.apim.beans.events.EventType;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayType;
import com.t1t.digipolis.apim.beans.idp.KeyMappingBean;
import com.t1t.digipolis.apim.beans.iprestriction.BlacklistBean;
import com.t1t.digipolis.apim.beans.iprestriction.WhitelistBean;
import com.t1t.digipolis.apim.beans.mail.MailTemplateBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationTypes;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.plans.PlanBean;
import com.t1t.digipolis.apim.beans.plans.PlanStatus;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.plugins.PluginBean;
import com.t1t.digipolis.apim.beans.policies.Policies;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.search.*;
import com.t1t.digipolis.apim.beans.services.*;
import com.t1t.digipolis.apim.beans.summary.*;
import com.t1t.digipolis.apim.beans.support.SupportBean;
import com.t1t.digipolis.apim.beans.support.SupportComment;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.mail.MailTopic;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.util.ServiceConventionUtil;
import com.t1t.digipolis.util.ServiceScopeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A JPA implementation of the storage interface.
 */
@ApplicationScoped
@Default
public class JpaStorage extends AbstractJpaStorage implements IStorage, IStorageQuery {

    private static Logger logger = LoggerFactory.getLogger(JpaStorage.class);
    @Inject AppConfig config;
    @Inject ISecurityAppContext appContext;
    @Inject
    ISecurityContext security;

    /**
     * Constructor.
     */
    public JpaStorage() {
    }

    /**
     * @see IStorage#createApplication(ApplicationBean)
     */
    @Override

    public void createApplication(ApplicationBean application) throws StorageException {
        super.create(application);
    }

    /**
     * @see IStorage#createApplicationVersion(ApplicationVersionBean)
     */
    @Override

    public void createApplicationVersion(ApplicationVersionBean version) throws StorageException {
        super.create(version);
    }

    /**
     * @see IStorage#createContract(ContractBean)
     */
    @Override

    public void createContract(ContractBean contract) throws StorageException {
        super.create(contract);
    }

    /**
     * @see IStorage#createGateway(GatewayBean)
     */
    @Override

    public void createGateway(GatewayBean gateway) throws StorageException {
        super.create(gateway);
    }

    /**
     * @see IStorage#createPlugin(PluginBean)
     */
    @Override

    public void createPlugin(PluginBean plugin) throws StorageException {
        super.create(plugin);
    }

    /**
     * @see IStorage#createOrganization(OrganizationBean)
     */
    @Override

    public void createOrganization(OrganizationBean organization) throws StorageException {
        super.create(organization);
    }

    /**
     * @see IStorage#createPlan(PlanBean)
     */
    @Override

    public void createPlan(PlanBean plan) throws StorageException {
        super.create(plan);
    }

    /**
     * @see IStorage#createPlanVersion(PlanVersionBean)
     */
    @Override

    public void createPlanVersion(PlanVersionBean version) throws StorageException {
        super.create(version);
    }

    /**
     * @see IStorage#createPolicy(PolicyBean)
     */
    @Override

    public void createPolicy(PolicyBean policy) throws StorageException {
        super.create(policy);
    }

    /**
     * @see IStorage#createPolicyDefinition(PolicyDefinitionBean)
     */
    @Override

    public void createPolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException {
        super.create(policyDef);
    }

    /**
     * @see IStorage#createService(ServiceBean)
     */
    @Override

    public void createService(ServiceBean service) throws StorageException {
        super.create(service);
    }

    /**
     * @see IStorage#createServiceVersion(ServiceVersionBean)
     */
    @Override

    public void createServiceVersion(ServiceVersionBean version) throws StorageException {
        super.create(version);
    }

    /**
     * @see IStorage#updateApplication(ApplicationBean)
     */
    @Override

    public void updateApplication(ApplicationBean application) throws StorageException {
        super.update(application);
    }

    /**
     * @see IStorage#updateApplicationVersion(ApplicationVersionBean)
     */
    @Override

    public void updateApplicationVersion(ApplicationVersionBean version) throws StorageException {
        super.update(version);
    }

    /**
     * @see IStorage#updateGateway(GatewayBean)
     */
    @Override

    public void updateGateway(GatewayBean gateway) throws StorageException {
        super.update(gateway);
    }

    /**
     * @see IStorage#updateOrganization(OrganizationBean)
     */
    @Override

    public void updateOrganization(OrganizationBean organization) throws StorageException {
        super.update(organization);
    }

    /**
     * @see IStorage#updatePlan(PlanBean)
     */
    @Override

    public void updatePlan(PlanBean plan) throws StorageException {
        super.update(plan);
    }

    /**
     * @see IStorage#updatePlanVersion(PlanVersionBean)
     */
    @Override

    public void updatePlanVersion(PlanVersionBean version) throws StorageException {
        super.update(version);
    }

    /**
     * @see IStorage#updatePolicy(PolicyBean)
     */
    @Override

    public void updatePolicy(PolicyBean policy) throws StorageException {
        super.update(policy);
    }

    /**
     * @see IStorage#updatePolicyDefinition(PolicyDefinitionBean)
     */
    @Override

    public void updatePolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException {
        super.update(policyDef);
    }

    @Override
    public void updateApplicationOAuthCredentials(OAuthAppBean oAuthAppBean) throws StorageException {
        super.update(oAuthAppBean);
    }

    @Override
    public void updateServiceAnnouncement(AnnouncementBean announcement) throws StorageException {
        super.update(announcement);
    }

    @Override
    public void updateServiceSupport(SupportBean supportBean) throws StorageException {
        super.update(supportBean);
    }

    @Override
    public void updateServiceSupportComment(SupportComment commentBean) throws StorageException {
        super.update(commentBean);
    }

    @Override
    public void updateContract(ContractBean contractBean) throws StorageException {
        super.update(contractBean);
    }

    @Override
    public void updateMailTemplate(MailTemplateBean mailTemplateBean) throws StorageException {
        super.update(mailTemplateBean);
    }

    @Override
    public void updateEvent(EventBean event) throws StorageException {
        super.update(event);
    }

    @Override
    public void updateManagedApplication(ManagedApplicationBean manapp) throws StorageException {
        super.update(manapp);
    }

    @Override
    public void updateAuditEntry(AuditEntryBean audit) throws StorageException {
        super.update(audit);
    }

    @Override
    public void updateKeyMapping(KeyMappingBean keyMappingBean) throws StorageException {
        super.update(keyMappingBean);
    }

    /**
     * @see IStorage#updateService(ServiceBean)
     */
    @Override

    public void updateService(ServiceBean service) throws StorageException {
        super.update(service);
    }

    /**
     * @see IStorage#updateServiceVersion(ServiceVersionBean)
     */
    @Override

    public void updateServiceVersion(ServiceVersionBean version) throws StorageException {
        super.update(version);
    }

    /**
     * @see IStorage#updateServiceDefinition(ServiceVersionBean, InputStream)
     */
    @Override

    public void updateServiceDefinition(ServiceVersionBean version, InputStream definitionStream)
            throws StorageException {
        try {
            ServiceDefinitionBean bean = super.get(version.getId(), ServiceDefinitionBean.class);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            IOUtils.copy(definitionStream, baos);
            byte[] data = baos.toByteArray();
            if (bean != null) {
                bean.setData(data);
                super.update(bean);
            } else {
                bean = new ServiceDefinitionBean();
                bean.setId(version.getId());
                bean.setData(data);
                bean.setServiceVersion(version);
                super.create(bean);
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }
    }

    /**
     * @see IStorage#deleteOrganization(OrganizationBean)
     */
    @Override

    public void deleteOrganization(OrganizationBean organization) throws StorageException {
        super.delete(organization);
    }

    /**
     * @see IStorage#deleteApplication(ApplicationBean)
     */
    @Override

    public void deleteApplication(ApplicationBean application) throws StorageException {
        super.delete(application);
    }

    /**
     * @see IStorage#deleteApplicationVersion(ApplicationVersionBean)
     */
    @Override
    public void deleteApplicationVersion(ApplicationVersionBean version) throws StorageException {
        super.delete(version);
    }

    /**
     * @see IStorage#deleteContract(ContractBean)
     */
    @Override
    public void deleteContract(ContractBean contract) throws StorageException {
        super.delete(contract);
    }

    /**
     * @see IStorage#deleteService(ServiceBean)
     */
    @Override
    public void deleteService(ServiceBean service) throws StorageException {
        super.delete(service);
    }

    /**
     * @see IStorage#deleteServiceVersion(ServiceVersionBean)
     */
    @Override
    public void deleteServiceVersion(ServiceVersionBean version) throws StorageException {
        super.delete(version);
    }

    /**
     * @see IStorage#deleteServiceDefinition(ServiceVersionBean)
     */
    @Override
    public void deleteServiceDefinition(ServiceVersionBean version) throws StorageException {
        ServiceDefinitionBean bean = super.get(version.getId(), ServiceDefinitionBean.class);
        if (bean != null) {
            super.delete(bean);
        } else {
            throw new StorageException("No definition found."); //$NON-NLS-1$
        }
    }

    /**
     * @see IStorage#deletePlan(PlanBean)
     */
    @Override
    public void deletePlan(PlanBean plan) throws StorageException {
        super.delete(plan);
    }

    /**
     * @see IStorage#deletePlanVersion(PlanVersionBean)
     */
    @Override
    public void deletePlanVersion(PlanVersionBean version) throws StorageException {
        super.delete(version);
    }

    /**
     * @see IStorage#deletePolicy(PolicyBean)
     */
    @Override
    public void deletePolicy(PolicyBean policy) throws StorageException {
        super.delete(policy);
    }

    /**
     * @see IStorage#deleteGateway(GatewayBean)
     */
    @Override
    public void deleteGateway(GatewayBean gateway) throws StorageException {
        super.delete(gateway);
    }

    /**
     * @see IStorage#deletePlugin(PluginBean)
     */
    @Override
    public void deletePlugin(PluginBean plugin) throws StorageException {
        super.delete(plugin);
    }

    /**
     * @see IStorage#deletePolicyDefinition(PolicyDefinitionBean)
     */
    @Override
    public void deletePolicyDefinition(PolicyDefinitionBean policyDef) throws StorageException {
        super.delete(policyDef);
    }

    @Override
    public void deleteApplicationOAuthCredentials(OAuthAppBean oAuthAppBean) throws StorageException {
        super.delete(oAuthAppBean);
    }

    @Override
    public void deleteServiceAnnouncement(AnnouncementBean announcement) throws StorageException {
        super.delete(announcement);
    }

    @Override
    public void deleteServiceSupport(SupportBean supportBean) throws StorageException {
        super.delete(supportBean);
    }

    @Override
    public void deleteServiceSupportComment(SupportComment commentBean) throws StorageException {
        super.delete(commentBean);
    }

    @Override
    public void deleteWhitelistRecord(WhitelistBean whitelistBean) throws StorageException {
        super.delete(whitelistBean);
    }

    @Override
    public void deleteBalcklistRecord(BlacklistBean blacklistBean) throws StorageException {
        super.delete(blacklistBean);
    }

    @Override
    public void deleteEvent(EventBean eventBean) throws StorageException {
        super.delete(eventBean);
    }

    @Override
    public void deleteMailTemplate(MailTemplateBean mailTemplateBean) throws StorageException {
        super.delete(mailTemplateBean);
    }

    @Override
    public void deleteManagedApplication(ManagedApplicationBean manapp) throws StorageException {
        super.delete(manapp);
    }

    @Override
    public void deleteKeyMapping(KeyMappingBean keyMappingBean) throws StorageException {
        super.delete(keyMappingBean);
    }

    /**
     * @see IStorage#getOrganization(String)
     */
    @Override

    public OrganizationBean getOrganization(String id) throws StorageException {
        return super.get(id, OrganizationBean.class);
    }

    /**
     * @see IStorage#getApplication(String, String)
     */
    @Override
    public ApplicationBean getApplication(String organizationId, String id) throws StorageException {
        return super.get(organizationId, id, ApplicationBean.class);
    }

    /**
     * @see IStorage#getContract(Long)
     */
    @Override
    public ContractBean getContract(Long id) throws StorageException {
        return super.get(id, ContractBean.class);
    }

    /**
     * @see IStorage#getService(String, String)
     */
    @Override
    public ServiceBean getService(String organizationId, String id) throws StorageException {
        return super.get(organizationId, id, ServiceBean.class);
    }

    /**
     * @see IStorage#getPlan(String, String)
     */
    @Override
    public PlanBean getPlan(String organizationId, String id) throws StorageException {
        return super.get(organizationId, id, PlanBean.class);
    }

    /**
     * @see IStorage#getPolicy(PolicyType, String, String, String, Long)
     */
    @Override
    public PolicyBean getPolicy(PolicyType type, String organizationId, String entityId, String version,
                                Long id) throws StorageException {
        PolicyBean policyBean = super.get(id, PolicyBean.class);
        if (policyBean.getType() != type) {
            return null;
        }
        if (!policyBean.getOrganizationId().equals(organizationId)) {
            return null;
        }
        if (!policyBean.getEntityId().equals(entityId)) {
            return null;
        }
        if (!policyBean.getEntityVersion().equals(version)) {
            return null;
        }
        return policyBean;
    }

    /**
     * @see IStorage#getGateway(String)
     */
    @Override
    public GatewayBean getGateway(String id) throws StorageException {
        return super.get(id, GatewayBean.class);
    }

    /**
     * @see IStorage#getPlugin(long)
     */
    @Override
    public PluginBean getPlugin(long id) throws StorageException {
        return super.get(id, PluginBean.class);
    }

    /**
     * @see IStorage#getPlugin(String, String)
     */
    @Override
    public PluginBean getPlugin(String groupId, String artifactId) throws StorageException {
        try {
            EntityManager entityManager = getActiveEntityManager();

            @SuppressWarnings("nls")
            String sql =
                    "SELECT p.id, p.artifact_id, p.group_id, p.version, p.classifier, p.type, p.name, p.description, p.created_by, p.created_on" +
                            "  FROM plugins p" +
                            " WHERE p.group_id = ? AND p.artifact_id = ?";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, groupId);
            query.setParameter(2, artifactId);
            List<Object[]> rows = (List<Object[]>) query.getResultList();
            if (rows.size() > 0) {
                Object[] row = rows.get(0);
                PluginBean plugin = new PluginBean();
                plugin.setId(((Number) row[0]).longValue());
                plugin.setArtifactId(String.valueOf(row[1]));
                plugin.setGroupId(String.valueOf(row[2]));
                plugin.setVersion(String.valueOf(row[3]));
                plugin.setClassifier((String) row[4]);
                plugin.setType((String) row[5]);
                plugin.setName(String.valueOf(row[6]));
                plugin.setDescription(String.valueOf(row[7]));
                plugin.setCreatedBy(String.valueOf(row[8]));
                plugin.setCreatedOn((Date) row[9]);
                return plugin;
            } else {
                return null;
            }
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        }
    }

    /**
     * @see IStorage#getPolicyDefinition(String)
     */
    @Override
    public PolicyDefinitionBean getPolicyDefinition(String id) throws StorageException {
        return super.get(id, PolicyDefinitionBean.class);
    }

    @Override
    public OAuthAppBean getApplicationOAuthCredentials(String id) throws StorageException {
        return super.get(id, OAuthAppBean.class);
    }

    @Override
    public AnnouncementBean getServiceAnnouncement(Long id) throws StorageException {
        return super.get(id, AnnouncementBean.class);
    }

    @Override
    public SupportBean getServiceSupport(Long id) throws StorageException {
        return super.get(id, SupportBean.class);
    }

    @Override
    public SupportComment getServiceSupportComment(Long id) throws StorageException {
        return super.get(id, SupportComment.class);
    }

    @Override
    public WhitelistBean getWhitelistRecord(String id) throws StorageException {
        return super.get(id, WhitelistBean.class);
    }

    @Override
    public BlacklistBean getBlacklistRecord(String id) throws StorageException {
        return super.get(id, BlacklistBean.class);
    }

    /**
     * @see IStorage#reorderPolicies(PolicyType, String, String, String, List)
     */
    @Override
    public void reorderPolicies(PolicyType type, String organizationId, String entityId,
                                String entityVersion, List<Long> newOrder) throws StorageException {
        int orderIndex = 0;
        for (Long policyId : newOrder) {
            PolicyBean storedPolicy = getPolicy(type, organizationId, entityId, entityVersion, policyId);
            if (storedPolicy == null) {
                throw new StorageException("Invalid policy id: " + policyId); //$NON-NLS-1$
            }
            storedPolicy.setOrderIndex(orderIndex++);
            updatePolicy(storedPolicy);
        }
    }

    @Override
    public OrganizationBean getDefaultOrganizationForConsumers() throws StorageException {
        String defaultOrgId = config.getDefaultOrganization();
        return super.get(defaultOrgId, OrganizationBean.class);
    }

    @Override
    public Set<String> getAllOrganizations() throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT o FROM OrganizationBean o where o.context = :oContext";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("oContext",appContext.getApplicationPrefix());
        List<OrganizationBean> orgs = (List<OrganizationBean>) query.getResultList();
        logger.info("dborgs all:{}",orgs);
        Set<String> orgNames = new TreeSet<>();
        for(OrganizationBean org:orgs){
            orgNames.add(org.getId());
        }
        return orgNames;
    }

    /**
     * @see AbstractJpaStorage#find(SearchCriteriaBean, Class)
     */
    @Override
    protected <T> SearchResultsBean<T> find(SearchCriteriaBean criteria, Class<T> type) throws StorageException {
        SearchResultsBean<T> rval = super.find(criteria, type);
        return rval;
    }

    /**
     * @see IStorageQuery#findOrganizations(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<OrganizationSummaryBean> findOrganizations(SearchCriteriaBean criteria)
            throws StorageException {
        SearchResultsBean<OrganizationBean> orgs = find(criteria, OrganizationBean.class);
        SearchResultsBean<OrganizationSummaryBean> rval = new SearchResultsBean<>();
        rval.setTotalSize(orgs.getTotalSize());
        List<OrganizationBean> beans = orgs.getBeans();
        for (OrganizationBean bean : beans) {
            OrganizationSummaryBean osb = new OrganizationSummaryBean();
            osb.setId(bean.getId());
            osb.setFriendlyName(bean.getFriendlyName());
            osb.setName(bean.getName());
            osb.setDescription(bean.getDescription());
            osb.setOrganizationPrivate(bean.isOrganizationPrivate());
            rval.getBeans().add(osb);
        }
        return rval;
    }

    /**
     * @see IStorageQuery#findApplications(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<ApplicationSummaryBean> findApplications(SearchCriteriaBean criteria) throws StorageException {
        //filter Applications along scope
        SearchResultsBean<ApplicationBean> tempResult = find(criteria, ApplicationBean.class);
        SearchResultsBean<ApplicationBean> result = new SearchResultsBean<>();
        if(!StringUtils.isEmpty(appContext.getApplicationPrefix())){
            List<ApplicationBean> appBeans = tempResult.getBeans().stream().filter(a -> a.getContext().equalsIgnoreCase(appContext.getApplicationPrefix())).collect(Collectors.toList());
            result.setBeans(appBeans);
            result.setTotalSize(appBeans.size());
        }else {
            result = tempResult;
        }

        SearchResultsBean<ApplicationSummaryBean> rval = new SearchResultsBean<>();
        rval.setTotalSize(result.getTotalSize());
        List<ApplicationBean> beans = result.getBeans();
        rval.setBeans(new ArrayList<ApplicationSummaryBean>(beans.size()));
        for (ApplicationBean application : beans) {
            ApplicationSummaryBean summary = new ApplicationSummaryBean();
            OrganizationBean organization = application.getOrganization();
            summary.setId(application.getId());
            summary.setName(application.getName());
            summary.setDescription(application.getDescription());
            // TODO find the number of contracts - probably need native SQL for that
            summary.setNumContracts(0);
            summary.setOrganizationId(application.getOrganization().getId());
            summary.setOrganizationName(organization.getName());
            rval.getBeans().add(summary);
        }
        return rval;
    }

    /**
     * @see IStorageQuery#findServices(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<ServiceSummaryBean> findServices(SearchCriteriaBean criteria) throws StorageException {
        SearchResultsBean<ServiceBean> result = find(criteria, ServiceBean.class);

        SearchResultsBean<ServiceSummaryBean> rval = new SearchResultsBean<>();
        rval.setTotalSize(result.getTotalSize());
        List<ServiceBean> beans = result.getBeans();
        rval.setBeans(new ArrayList<ServiceSummaryBean>(beans.size()));
        for (ServiceBean service : beans) {
            ServiceSummaryBean summary = new ServiceSummaryBean();
            OrganizationBean organization = service.getOrganization();
            summary.setId(service.getId());
            summary.setName(service.getName());
            summary.setDescription(service.getDescription());
            summary.setCreatedOn(service.getCreatedOn());
            summary.setOrganizationId(service.getOrganization().getId());
            summary.setOrganizationName(organization.getName());
            rval.getBeans().add(summary);
        }
        return rval;
    }

    @Override
    public List<ServiceVersionBean> findPublishedServiceVersionsByServiceName(String name) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT s FROM ServiceVersionBean s WHERE LOWER(s.service.name) LIKE :name AND s.status = :status";
        List<ServiceVersionBean> rval = (List<ServiceVersionBean>) em.createQuery(jpql)
                .setParameter("name", name)
                .setParameter("status", ServiceStatus.Published)
                .getResultList();
        return  doNotFilterServices() ? rval : ServiceScopeUtil.resolveSVBScope(rval, appContext.getApplicationPrefix(), security.isAdmin());
    }

    public List<ServiceVersionBean> findServiceByStatus(ServiceStatus status) throws StorageException {
        List<ServiceVersionBean> allServicesByStatus = super.findAllServicesByStatus(status);
        return doNotFilterServices() ? allServicesByStatus : ServiceScopeUtil.resolveSVBScope(allServicesByStatus,appContext.getApplicationPrefix(), security.isAdmin());
    }

    @Override
    public List<ServiceVersionBean> findGatewayServiceVersions() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT s FROM ServiceVersionBean s WHERE s.status = :pub OR s.status = :dep";
        return em.createQuery(jpql)
                .setParameter("pub", ServiceStatus.Published)
                .setParameter("dep", ServiceStatus.Deprecated)
                .getResultList();
    }

    public List<ServiceVersionBean> findAllServicesWithCategory(List<String> categories) throws StorageException {
        List<ServiceVersionBean> allServicesForCat = findAllServiceVersionsInCategory(categories);
        return doNotFilterServices() ? allServicesForCat : ServiceScopeUtil.resolveSVBScope(allServicesForCat,appContext.getApplicationPrefix(), security.isAdmin());
    }

    public List<ServiceVersionBean> findLatestServicesWithCategory(List<String> categories) throws StorageException {
        return doNotFilterServices() ? findLatestPublishedServiceVersionsInCategory(categories) : ServiceScopeUtil.resolveSVBScope(findLatestPublishedServiceVersionsInCategory(categories), appContext.getApplicationPrefix(), security.isAdmin());
    }

    public Set<String> findAllUniqueCategories() throws StorageException {
        List<ServiceBean> services = new ArrayList<>();
        List<ServiceVersionBean> allServicesByStatus = super.findAllServicesByStatus(ServiceStatus.Published);
        List<ServiceVersionBean> allServicesFiltered = doNotFilterServices() ? allServicesByStatus : ServiceScopeUtil.resolveSVBScope(allServicesByStatus,appContext.getApplicationPrefix(), security.isAdmin());
        for(ServiceVersionBean svb:allServicesFiltered){
            services.add(svb.getService());
        }
        //extract all categories in a set
        Set<String> catSet = new TreeSet<>();
        for (ServiceBean sb : services) {
            for (String cat : sb.getCategories()) catSet.add(cat);
        }
        return catSet;
    }

    public Set<String> findAllUniquePublishedCategories() throws StorageException {
        List<ServiceBean> services = new ArrayList<>();
        List<ServiceVersionBean> allServicesByStatus = super.findAllServicesByStatus(ServiceStatus.Published);
        List<ServiceVersionBean> allServicesFiltered = doNotFilterServices() ? allServicesByStatus : ServiceScopeUtil.resolveSVBScope(allServicesByStatus,appContext.getApplicationPrefix(), security.isAdmin());
        for(ServiceVersionBean svb:allServicesFiltered){
            services.add(svb.getService());
        }
        Set<String> catSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (ServiceBean service : services) {
            catSet.addAll(service.getCategories());
        }
        return catSet;
    }

    /**
     * @see IStorageQuery#findPlans(String, SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<PlanSummaryBean> findPlans(String organizationId, SearchCriteriaBean criteria)
            throws StorageException {
        criteria.addFilter("organization.id", organizationId, SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        SearchResultsBean<PlanBean> result = find(criteria, PlanBean.class);
        SearchResultsBean<PlanSummaryBean> rval = new SearchResultsBean<>();
        rval.setTotalSize(result.getTotalSize());
        List<PlanBean> plans = result.getBeans();
        rval.setBeans(new ArrayList<PlanSummaryBean>(plans.size()));
        for (PlanBean plan : plans) {
            PlanSummaryBean summary = new PlanSummaryBean();
            OrganizationBean organization = plan.getOrganization();
            summary.setId(plan.getId());
            summary.setName(plan.getName());
            summary.setDescription(plan.getDescription());
            summary.setOrganizationId(plan.getOrganization().getId());
            summary.setOrganizationName(organization.getName());
            rval.getBeans().add(summary);
        }
        return rval;
    }

    /**
     * @see IStorage#createAuditEntry(AuditEntryBean)
     */
    @Override

    public void createAuditEntry(AuditEntryBean entry) throws StorageException {
        if (entry != null) {
            super.create(entry);
        }
    }

    @Override
    public void createApplicationOAuthCredentials(OAuthAppBean oAuthAppBean) throws StorageException {
        super.create(oAuthAppBean);
    }

    @Override
    public void createServiceAnnouncement(AnnouncementBean announcement) throws StorageException {
        super.create(announcement);
    }

    @Override
    public void createServiceSupport(SupportBean supportBean) throws StorageException {
        super.create(supportBean);
    }

    @Override
    public void createServiceSupportComment(SupportComment commentBean) throws StorageException {
        super.create(commentBean);
    }

    @Override
    public void createWhilelistRecord(WhitelistBean whitelistBean) throws StorageException {
        super.create(whitelistBean);
    }

    @Override
    public void createBlacklistRecord(BlacklistBean blacklistBean) throws StorageException {
        super.create(blacklistBean);
    }

    @Override
    public void createEvent(EventBean eventBean) throws StorageException {
        super.create(eventBean);
    }

    @Override
    public void createMailTemplate(MailTemplateBean mailTemplateBean) throws Exception {
        super.create(mailTemplateBean);
    }

    @Override
    public void createManagedApplication(ManagedApplicationBean manapp) throws Exception {
        super.create(manapp);
    }

    @Override
    public void createKeyMapping(KeyMappingBean keyMappingBean) throws Exception {
        super.create(keyMappingBean);
    }

    /**
     * @see IStorageQuery#auditEntity(String, String, String, Class, PagingBean)
     */
    @Override
    public <T> SearchResultsBean<AuditEntryBean> auditEntity(String organizationId, String entityId, String entityVersion,
                                                             Class<T> type, PagingBean paging) throws StorageException {
        SearchCriteriaBean criteria = new SearchCriteriaBean();
        if (paging != null) {
            criteria.setPaging(paging);
        } else {
            criteria.setPage(1);
            criteria.setPageSize(20);
        }
        criteria.setOrder("id", false); //$NON-NLS-1$
        if (organizationId != null) {
            criteria.addFilter("organizationId", organizationId, SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        }
        if (entityId != null) {
            criteria.addFilter("entityId", entityId, SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        }
        if (entityVersion != null) {
            criteria.addFilter("entityVersion", entityVersion, SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        }
        if (type != null) {
            AuditEntityType entityType = null;
            if (type == OrganizationBean.class) {
                entityType = AuditEntityType.Organization;
            } else if (type == ApplicationBean.class) {
                entityType = AuditEntityType.Application;
            } else if (type == ServiceBean.class) {
                entityType = AuditEntityType.Service;
            } else if (type == PlanBean.class) {
                entityType = AuditEntityType.Plan;
            }
            if (entityType != null) {
                criteria.addFilter("entityType", entityType.name(), SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
            }
        }

        return find(criteria, AuditEntryBean.class);
    }

    /**
     * @see IStorageQuery#auditUser(String, PagingBean)
     */
    @Override
    public <T> SearchResultsBean<AuditEntryBean> auditUser(String userId, PagingBean paging)
            throws StorageException {
        SearchCriteriaBean criteria = new SearchCriteriaBean();
        if (paging != null) {
            criteria.setPaging(paging);
        } else {
            criteria.setPage(1);
            criteria.setPageSize(20);
        }
        criteria.setOrder("createdOn", false); //$NON-NLS-1$
        if (userId != null) {
            criteria.addFilter("who", userId, SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        }

        return find(criteria, AuditEntryBean.class);
    }

    /**
     * @see IStorageQuery#listGateways()
     */
    @Override
    public List<GatewaySummaryBean> listGateways() throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        @SuppressWarnings("nls")
        String sql =
                "SELECT g.id, g.name, g.description, g.type" +
                        "  FROM gateways g" +
                        " ORDER BY g.name ASC";
        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> rows = (List<Object[]>) query.getResultList();
        List<GatewaySummaryBean> gateways = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            GatewaySummaryBean gateway = new GatewaySummaryBean();
            gateway.setId(String.valueOf(row[0]));
            gateway.setName(String.valueOf(row[1]));
            gateway.setDescription(String.valueOf(row[2]));
            gateway.setType(GatewayType.valueOf(String.valueOf(row[3])));
            gateways.add(gateway);
        }
        return gateways;
    }

    @Override
    public GatewayBean getDefaultGateway() throws StorageException {
        final List<GatewayBean> gatewayBeans = listGatewayBeans();
        if(gatewayBeans!=null && gatewayBeans.size()>0)return gatewayBeans.get(0);
        return null;
    }

    @Override
    public List<GatewayBean> listGatewayBeans() throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT g FROM GatewayBean g";
        Query query = entityManager.createQuery(jpql);
        List<GatewayBean> rows = query.getResultList();
        return rows;
    }

    /**
     * @see IStorageQuery#listPlugins()
     */
    @Override
    public List<PluginSummaryBean> listPlugins() throws StorageException {

        EntityManager entityManager = getActiveEntityManager();

        @SuppressWarnings("nls")
        String sql =
                "SELECT p.id, p.artifact_id, p.group_id, p.version, p.classifier, p.type, p.name, p.description, p.created_by, p.created_on" +
                        "  FROM plugins p" +
                        " ORDER BY p.name ASC";
        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> rows = (List<Object[]>) query.getResultList();
        List<PluginSummaryBean> plugins = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            PluginSummaryBean plugin = new PluginSummaryBean();
            plugin.setId(((Number) row[0]).longValue());
            plugin.setArtifactId(String.valueOf(row[1]));
            plugin.setGroupId(String.valueOf(row[2]));
            plugin.setVersion(String.valueOf(row[3]));
            plugin.setClassifier((String) row[4]);
            plugin.setType((String) row[5]);
            plugin.setName(String.valueOf(row[6]));
            plugin.setDescription(String.valueOf(row[7]));
            plugin.setCreatedBy(String.valueOf(row[8]));
            plugin.setCreatedOn((Date) row[9]);
            plugins.add(plugin);
        }
        return plugins;

    }

    /**
     * @see IStorageQuery#listPolicyDefinitions()
     */
    @Override
    public List<PolicyDefinitionSummaryBean> listPolicyDefinitions() throws StorageException {

        EntityManager entityManager = getActiveEntityManager();

        @SuppressWarnings("nls")
        String sql =
                "SELECT pd.id, pd.name, pd.description, pd.icon, pd.plugin_id, pd.form_type, pd.scope_service, pd.scope_plan, pd.scope_auto" +
                        "  FROM policydefs pd" +
                        " ORDER BY pd.name ASC";
        Query query = entityManager.createNativeQuery(sql);

        List<Object[]> rows = (List<Object[]>) query.getResultList();
        List<PolicyDefinitionSummaryBean> rval = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            PolicyDefinitionSummaryBean bean = new PolicyDefinitionSummaryBean();
            bean.setId(String.valueOf(row[0]));
            bean.setName(String.valueOf(row[1]));
            bean.setDescription(String.valueOf(row[2]));
            bean.setIcon(String.valueOf(row[3]));
            if (row[4] != null) {
                bean.setPluginId(((Number) row[4]).longValue());
            }
            if (row[5] != null) {
                bean.setFormType(PolicyFormType.valueOf(String.valueOf(row[5])));
            }
            bean.setScopeService(((Boolean) row[6]).booleanValue());
            bean.setScopePlan(((Boolean) row[7]).booleanValue());
            bean.setScopeAuto(((Boolean) row[8]).booleanValue());
            rval.add(bean);
        }
        return rval;

    }

    /**
     * @see IStorageQuery#getOrgs(Set)
     */
    public List<OrganizationSummaryBean> getOrgs(Set<String> orgIds) throws StorageException {
        List<OrganizationSummaryBean> orgs = new ArrayList<>();
        if (orgIds == null || orgIds.isEmpty()) {
            return orgs;
        }
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT o from OrganizationBean o WHERE o.id IN :orgs ORDER BY o.id ASC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgs", orgIds);
        List<OrganizationBean> qr = (List<OrganizationBean>) query.getResultList();
        for (OrganizationBean bean : qr) {
            OrganizationSummaryBean summary = new OrganizationSummaryBean();
            summary.setId(bean.getId());
            summary.setFriendlyName(bean.getFriendlyName());
            summary.setName(bean.getName());
            summary.setDescription(bean.getDescription());
            summary.setOrganizationPrivate(bean.isOrganizationPrivate());
            orgs.add(summary);
        }
        return orgs;

    }

    /**
     * @see IStorageQuery#getApplicationsInOrg(String)
     */
    @Override
    public List<ApplicationSummaryBean> getApplicationsInOrg(String orgId) throws StorageException {
        Set<String> orgIds = new HashSet<>();
        orgIds.add(orgId);
        return getApplicationsInOrgs(orgIds);
    }

    /**
     * @see IStorageQuery#getApplicationsInOrgs(Set)
     */
    @Override
    public List<ApplicationSummaryBean> getApplicationsInOrgs(Set<String> orgIds) throws StorageException {
        List<ApplicationSummaryBean> rval = new ArrayList<>();
        if(orgIds!=null && orgIds.size()>0){
            EntityManager entityManager = getActiveEntityManager();
            String jpql = "SELECT a FROM ApplicationBean a JOIN a.organization o WHERE o.id IN :orgs ORDER BY a.id ASC"; //$NON-NLS-1$
            Query query = entityManager.createQuery(jpql);
            query.setParameter("orgs", orgIds); //$NON-NLS-1$
            List resultList = query.getResultList();
            if(resultList.size()>0){
                List<ApplicationBean> qr = (List<ApplicationBean>) query.getResultList();
                List<ApplicationBean> qrFiltered = new ArrayList<>();
                if(!StringUtils.isEmpty(appContext.getApplicationPrefix())){
                    qrFiltered = qr.stream().filter(app -> app.getContext().equalsIgnoreCase(appContext.getApplicationPrefix())).collect(Collectors.toList());
                }else qrFiltered = qr;

                for (ApplicationBean bean : qrFiltered) {
                    ApplicationSummaryBean summary = new ApplicationSummaryBean();
                    summary.setId(bean.getId());
                    summary.setName(bean.getName());
                    summary.setBase64logo(bean.getBase64logo());
                    summary.setDescription(bean.getDescription());
                    // TODO find the number of contracts - probably need a native SQL query to pull that together
                    summary.setNumContracts(0);
                    OrganizationBean org = bean.getOrganization();
                    summary.setOrganizationId(org.getId());
                    summary.setOrganizationName(org.getName());
                    rval.add(summary);
                }
            }
        }
        return rval;
    }

    /**
     * @see IStorageQuery#getServicesInOrg(String)
     */
    @Override
    public List<ServiceSummaryBean> getServicesInOrg(String orgId) throws StorageException {
        Set<String> orgIds = new HashSet<>();
        orgIds.add(orgId);
        return getServicesInOrgs(orgIds);
    }

    /**
     * @see IStorageQuery#getServicesInOrgs(Set)
     */
    @Override
    public List<ServiceSummaryBean> getServicesInOrgs(Set<String> orgIds) throws StorageException {
        List<ServiceSummaryBean> rval = new ArrayList<>();

        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT s FROM ServiceBean s JOIN s.organization o WHERE o.id IN :orgs ORDER BY s.id ASC"; //$NON-NLS-1$
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgs", orgIds); //$NON-NLS-1$
        List<ServiceBean> qr = (List<ServiceBean>) query.getResultList();
        for (ServiceBean bean : qr) {
            ServiceSummaryBean summary = new ServiceSummaryBean();
            summary.setId(bean.getId());
            summary.setName(bean.getName());
            summary.setDescription(bean.getDescription());
            summary.setCreatedOn(bean.getCreatedOn());
            OrganizationBean org = bean.getOrganization();
            summary.setOrganizationId(org.getId());
            summary.setOrganizationName(org.getName());
            rval.add(summary);
        }
        return rval;

    }

    /**
     * @see IStorage#getServiceVersion(String, String, String)
     */
    @Override
    public ServiceVersionBean getServiceVersion(String orgId, String serviceId, String version)
            throws StorageException {
        try {
            EntityManager entityManager = getActiveEntityManager();
            String jpql = "SELECT v from ServiceVersionBean v JOIN v.service s JOIN s.organization o WHERE o.id = :orgId AND s.id = :serviceId AND v.version = :version"; //$NON-NLS-1$
            Query query = entityManager.createQuery(jpql);
            query.setParameter("orgId", orgId); //$NON-NLS-1$
            query.setParameter("serviceId", serviceId); //$NON-NLS-1$
            query.setParameter("version", version); //$NON-NLS-1$

            return (ServiceVersionBean) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        }
    }

    /**
     * @see IStorage#getServiceDefinition(ServiceVersionBean)
     */
    @Override
    public InputStream getServiceDefinition(ServiceVersionBean serviceVersion) throws StorageException {
        ServiceDefinitionBean bean = super.get(serviceVersion.getId(), ServiceDefinitionBean.class);
        if (bean == null) {
            return null;
        } else {
            return new ByteArrayInputStream(bean.getData());
        }
    }

    /**
     * @see IStorageQuery#getServiceVersions(String, String)
     */
    @Override
    public List<ServiceVersionSummaryBean> getServiceVersions(String orgId, String serviceId)
            throws StorageException {

        EntityManager entityManager = getActiveEntityManager();
        @SuppressWarnings("nls")
        String jpql =
                "SELECT v "
                        + "  FROM ServiceVersionBean v"
                        + "  JOIN v.service s"
                        + "  JOIN s.organization o"
                        + " WHERE o.id = :orgId"
                        + "  AND s.id = :serviceId"
                        + " ORDER BY v.id DESC";
        Query query = entityManager.createQuery(jpql);
        query.setMaxResults(500);
        query.setParameter("orgId", orgId); //$NON-NLS-1$
        query.setParameter("serviceId", serviceId); //$NON-NLS-1$

        List<ServiceVersionBean> serviceVersions = (List<ServiceVersionBean>) query.getResultList();
        List<ServiceVersionSummaryBean> rval = new ArrayList<>(serviceVersions.size());
        for (ServiceVersionBean serviceVersion : serviceVersions) {
            ServiceVersionSummaryBean svsb = new ServiceVersionSummaryBean();
            svsb.setOrganizationId(serviceVersion.getService().getOrganization().getId());
            svsb.setOrganizationName(serviceVersion.getService().getOrganization().getName());
            svsb.setId(serviceVersion.getService().getId());
            svsb.setName(serviceVersion.getService().getName());
            svsb.setDescription(serviceVersion.getService().getDescription());
            svsb.setVersion(serviceVersion.getVersion());
            svsb.setStatus(serviceVersion.getStatus());
            svsb.setPublicService(serviceVersion.isPublicService());
            rval.add(svsb);
        }
        return rval;

    }

    /**
     * @see IStorageQuery#getServiceVersionPlans(String, String, String)
     */
    @Override
    public List<ServicePlanSummaryBean> getServiceVersionPlans(String organizationId, String serviceId,
                                                               String version) throws StorageException {
        List<ServicePlanSummaryBean> plans = new ArrayList<>();

        ServiceVersionBean versionBean = getServiceVersion(organizationId, serviceId, version);
        Set<ServicePlanBean> servicePlans = versionBean.getPlans();
        if (servicePlans != null) {
            for (ServicePlanBean spb : servicePlans) {
                PlanVersionBean planVersion = getPlanVersion(organizationId, spb.getPlanId(), spb.getVersion());
                ServicePlanSummaryBean summary = new ServicePlanSummaryBean();
                summary.setPlanId(planVersion.getPlan().getId());
                summary.setPlanName(planVersion.getPlan().getName());
                summary.setPlanDescription(planVersion.getPlan().getDescription());
                summary.setVersion(spb.getVersion());
                plans.add(summary);
            }
        }
        return plans;

    }

    /**
     * @see IStorageQuery#getServiceContracts(String, String, String, int, int)
     */
    @Override
    public List<ContractSummaryBean> getServiceContracts(String organizationId, String serviceId,
                                                         String version, int page, int pageSize) throws StorageException {
        int start = (page - 1) * pageSize;

        EntityManager entityManager = getActiveEntityManager();
        @SuppressWarnings("nls")
        String jpql =
                "SELECT c from ContractBean c " +
                        "  JOIN c.service svcv " +
                        "  JOIN svcv.service svc " +
                        "  JOIN c.application appv " +
                        "  JOIN appv.application app " +
                        "  JOIN svc.organization sorg" +
                        "  JOIN app.organization aorg" +
                        " WHERE svc.id = :serviceId " +
                        "   AND sorg.id = :orgId " +
                        "   AND svcv.version = :version " +
                        " ORDER BY sorg.id, svc.id ASC"; //$NON-NLS-1$
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgId", organizationId); //$NON-NLS-1$
        query.setParameter("serviceId", serviceId); //$NON-NLS-1$
        query.setParameter("version", version); //$NON-NLS-1$
        query.setFirstResult(start);
        query.setMaxResults(pageSize);
        List<ContractBean> contracts = (List<ContractBean>) query.getResultList();
        List<ContractSummaryBean> rval = new ArrayList<>(contracts.size());
        for (ContractBean contractBean : contracts) {
            ApplicationBean application = contractBean.getApplication().getApplication();
            ServiceBean service = contractBean.getService().getService();
            PlanBean plan = contractBean.getPlan().getPlan();

            OrganizationBean appOrg = entityManager.find(OrganizationBean.class, application.getOrganization().getId());
            OrganizationBean svcOrg = entityManager.find(OrganizationBean.class, service.getOrganization().getId());

            ContractSummaryBean csb = new ContractSummaryBean();
            csb.setAppId(application.getId());
            csb.setApikey(contractBean.getApikey());
            csb.setAppOrganizationId(application.getOrganization().getId());
            csb.setAppOrganizationName(appOrg.getName());
            csb.setAppName(application.getName());
            csb.setAppVersion(contractBean.getApplication().getVersion());
            csb.setContractId(contractBean.getId());
            csb.setCreatedOn(contractBean.getCreatedOn());
            csb.setPlanId(plan.getId());
            csb.setPlanName(plan.getName());
            csb.setPlanVersion(contractBean.getPlan().getVersion());
            csb.setServiceDescription(service.getDescription());
            csb.setServiceId(service.getId());
            csb.setServiceName(service.getName());
            csb.setServiceOrganizationId(svcOrg.getId());
            csb.setServiceOrganizationName(svcOrg.getName());
            csb.setServiceVersion(contractBean.getService().getVersion());

            rval.add(csb);
        }
        return rval;
    }

    public List<ContractBean> getServiceContracts(String organizationId, String serviceId, String version) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql =
                "SELECT c from ContractBean c " +
                        "  JOIN c.service svcv " +
                        "  JOIN svcv.service svc " +
                        "  JOIN c.application appv " +
                        "  JOIN appv.application app " +
                        "  JOIN svc.organization sorg" +
                        "  JOIN app.organization aorg" +
                        " WHERE svc.id = :serviceId " +
                        "   AND sorg.id = :orgId " +
                        "   AND svcv.version = :version " +
                        " ORDER BY sorg.id, svc.id ASC"; //$NON-NLS-1$
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgId", organizationId); //$NON-NLS-1$
        query.setParameter("serviceId", serviceId); //$NON-NLS-1$
        query.setParameter("version", version); //$NON-NLS-1$
        return (List<ContractBean>) query.getResultList();
    }

    @Override
    public List<ContractBean> getPlanVersionContracts(Long planVersionId) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT c from ContractBean c JOIN c.plan pvs WHERE pvs.id = :planvId";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("planvId",planVersionId);
        return (List<ContractBean>) query.getResultList();
    }

    /**
     * @see IStorage#getApplicationVersion(String, String, String)
     */
    @Override
    public ApplicationVersionBean getApplicationVersion(String orgId, String applicationId, String version)
            throws StorageException {
        try {
            EntityManager entityManager = getActiveEntityManager();
            String jpql = "SELECT v from ApplicationVersionBean v JOIN v.application a JOIN a.organization o WHERE o.id = :orgId AND a.id = :applicationId AND v.version = :version"; //$NON-NLS-1$
            Query query = entityManager.createQuery(jpql);
            query.setParameter("orgId", orgId); //$NON-NLS-1$
            query.setParameter("applicationId", applicationId); //$NON-NLS-1$
            query.setParameter("version", version); //$NON-NLS-1$

            return (ApplicationVersionBean) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        }
    }

    /**
     * @see IStorageQuery#getApplicationVersions(String, String)
     */
    @Override
    public List<ApplicationVersionSummaryBean> getApplicationVersions(String orgId, String applicationId)
            throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        @SuppressWarnings("nls")
        String jpql =
                "SELECT v"
                        + "  FROM ApplicationVersionBean v"
                        + "  JOIN v.application a"
                        + "  JOIN a.organization o"
                        + " WHERE o.id = :orgId"
                        + "   AND a.id = :applicationId"
                        + " ORDER BY v.id DESC"; //$NON-NLS-1$
        Query query = entityManager.createQuery(jpql);
        query.setMaxResults(500);
        query.setParameter("orgId", orgId); //$NON-NLS-1$
        query.setParameter("applicationId", applicationId); //$NON-NLS-1$
        List<ApplicationVersionBean> appVersions = (List<ApplicationVersionBean>) query.getResultList();
        List<ApplicationVersionSummaryBean> rval = new ArrayList<>();
        for (ApplicationVersionBean appVersion : appVersions) {
            ApplicationVersionSummaryBean avsb = new ApplicationVersionSummaryBean();
            avsb.setOrganizationId(appVersion.getApplication().getOrganization().getId());
            avsb.setOrganizationName(appVersion.getApplication().getOrganization().getName());
            avsb.setId(appVersion.getApplication().getId());
            avsb.setName(appVersion.getApplication().getName());
            avsb.setDescription(appVersion.getApplication().getDescription());
            avsb.setVersion(appVersion.getVersion());
            avsb.setStatus(appVersion.getStatus());

            rval.add(avsb);
        }
        return rval;

    }

    /**
     * @see IStorageQuery#getApplicationContracts(String, String, String)
     */
    @Override
    public List<ContractSummaryBean> getApplicationContracts(String organizationId, String applicationId,
                                                             String version) throws StorageException {
        List<ContractSummaryBean> rval = new ArrayList<>();

        EntityManager entityManager = getActiveEntityManager();
        @SuppressWarnings("nls")
        String jpql =
                "SELECT c from ContractBean c " +
                        "  JOIN c.application appv " +
                        "  JOIN appv.application app " +
                        "  JOIN app.organization aorg" +
                        " WHERE app.id = :applicationId " +
                        "   AND aorg.id = :orgId " +
                        "   AND appv.version = :version " +
                        " ORDER BY aorg.id, app.id ASC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgId", organizationId); //$NON-NLS-1$
        query.setParameter("applicationId", applicationId); //$NON-NLS-1$
        query.setParameter("version", version); //$NON-NLS-1$
        List<ContractBean> contracts = (List<ContractBean>) query.getResultList();
        for (ContractBean contractBean : contracts) {
            ApplicationBean application = contractBean.getApplication().getApplication();
            ServiceBean service = contractBean.getService().getService();
            PlanBean plan = contractBean.getPlan().getPlan();

            OrganizationBean appOrg = entityManager.find(OrganizationBean.class, application.getOrganization().getId());
            OrganizationBean svcOrg = entityManager.find(OrganizationBean.class, service.getOrganization().getId());

            ContractSummaryBean csb = new ContractSummaryBean();
            csb.setAppId(application.getId());
            csb.setApikey(contractBean.getApikey());
            csb.setAppOrganizationId(application.getOrganization().getId());
            csb.setAppOrganizationName(appOrg.getName());
            csb.setAppName(application.getName());
            csb.setAppVersion(contractBean.getApplication().getVersion());
            csb.setContractId(contractBean.getId());
            csb.setCreatedOn(contractBean.getCreatedOn());
            csb.setPlanId(plan.getId());
            if (getManagedAppPrefixesForTypes(Collections.singletonList(ManagedApplicationTypes.Consent)).contains(appContext.getApplicationPrefix())) {
                csb.setProvisionKey(contractBean.getService().getProvisionKey());
            }
            csb.setPlanName(plan.getName());
            csb.setPlanVersion(contractBean.getPlan().getVersion());
            csb.setServiceDescription(service.getDescription());
            csb.setServiceId(service.getId());
            csb.setServiceName(service.getName());
            csb.setServiceOrganizationId(svcOrg.getId());
            csb.setServiceOrganizationName(svcOrg.getName());
            csb.setServiceVersion(contractBean.getService().getVersion());
            csb.setTermsAgreed(contractBean.getTermsAgreed());
            rval.add(csb);
        }

        return rval;
    }

    @Override
    public ApplicationVersionBean getApplicationForOAuth(String appOAuthId, String appOAuthSecret) throws StorageException {
        try {
            EntityManager entityManager = getActiveEntityManager();
            String jpql = "SELECT v from ApplicationVersionBean v WHERE v.oAuthClientId = :clientId AND v.oauthClientSecret = :clientSecret";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("clientId", appOAuthId); //$NON-NLS-1$
            query.setParameter("clientSecret", appOAuthSecret); //$NON-NLS-1$

            return (ApplicationVersionBean) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        }
    }

    /**
     * @see IStorageQuery#getApiRegistry(String, String, String)
     */
    @Override
    public ApiRegistryBean getApiRegistry(String organizationId, String applicationId, String version)
            throws StorageException {
        ApiRegistryBean rval = new ApiRegistryBean();

        EntityManager entityManager = getActiveEntityManager();
        @SuppressWarnings("nls")
        String jpql =
                "SELECT c from ContractBean c " +
                        "  JOIN c.application appv " +
                        "  JOIN appv.application app " +
                        "  JOIN app.organization aorg" +
                        " WHERE app.id = :applicationId " +
                        "   AND aorg.id = :orgId " +
                        "   AND appv.version = :version " +
                        " ORDER BY c.id ASC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgId", organizationId); //$NON-NLS-1$
        query.setParameter("applicationId", applicationId); //$NON-NLS-1$
        query.setParameter("version", version); //$NON-NLS-1$

        List<ContractBean> contracts = (List<ContractBean>) query.getResultList();
        for (ContractBean contractBean : contracts) {
            ServiceVersionBean svb = contractBean.getService();
            ServiceBean service = svb.getService();
            PlanBean plan = contractBean.getPlan().getPlan();

            OrganizationBean svcOrg = service.getOrganization();

            ApiEntryBean entry = new ApiEntryBean();
            entry.setServiceId(service.getId());
            entry.setServiceName(service.getName());
            entry.setServiceOrgId(svcOrg.getId());
            entry.setServiceOrgName(svcOrg.getName());
            entry.setServiceVersion(svb.getVersion());
            entry.setPlanId(plan.getId());
            entry.setPlanName(plan.getName());
            entry.setPlanVersion(contractBean.getPlan().getVersion());
            entry.setApiKey(contractBean.getApikey());

            Set<ServiceGatewayBean> gateways = svb.getGateways();
            if (gateways != null && gateways.size() > 0) {
                ServiceGatewayBean sgb = gateways.iterator().next();
                entry.setGatewayId(sgb.getGatewayId());
            }

            rval.getApis().add(entry);
        }

        return rval;
    }

    /**
     * @see IStorageQuery#getPlansInOrg(String)
     */
    @Override
    public List<PlanSummaryBean> getPlansInOrg(String orgId) throws StorageException {
        Set<String> orgIds = new HashSet<>();
        orgIds.add(orgId);
        return getPlansInOrgs(orgIds);
    }

    /**
     * @see IStorageQuery#getPlansInOrgs(Set)
     */
    @Override
    public List<PlanSummaryBean> getPlansInOrgs(Set<String> orgIds) throws StorageException {
        List<PlanSummaryBean> rval = new ArrayList<>();

        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT p FROM PlanBean p JOIN p.organization o WHERE o.id IN :orgs ORDER BY p.id ASC"; //$NON-NLS-1$
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgs", orgIds); //$NON-NLS-1$
        query.setMaxResults(500);

        List<PlanBean> qr = (List<PlanBean>) query.getResultList();
        for (PlanBean bean : qr) {
            PlanSummaryBean summary = new PlanSummaryBean();
            summary.setId(bean.getId());
            summary.setName(bean.getName());
            summary.setDescription(bean.getDescription());
            OrganizationBean org = bean.getOrganization();
            summary.setOrganizationId(org.getId());
            summary.setOrganizationName(org.getName());
            rval.add(summary);
        }
        return rval;

    }

    /**
     * @see IStorage#getPlanVersion(String, String, String)
     */
    @Override
    public PlanVersionBean getPlanVersion(String orgId, String planId, String version)
            throws StorageException {
        try {
            EntityManager entityManager = getActiveEntityManager();
            String jpql = "SELECT v from PlanVersionBean v JOIN v.plan p JOIN p.organization o WHERE o.id = :orgId AND p.id = :planId AND v.version = :version"; //$NON-NLS-1$
            Query query = entityManager.createQuery(jpql);
            query.setParameter("orgId", orgId); //$NON-NLS-1$
            query.setParameter("planId", planId); //$NON-NLS-1$
            query.setParameter("version", version); //$NON-NLS-1$

            return (PlanVersionBean) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        }
    }

    /**
     * @see IStorageQuery#getPlanVersions(String, String)
     */
    @Override
    public List<PlanVersionSummaryBean> getPlanVersions(String orgId, String planId) throws StorageException {

        EntityManager entityManager = getActiveEntityManager();
        @SuppressWarnings("nls")
        String jpql = "SELECT v from PlanVersionBean v" +
                "  JOIN v.plan p" +
                "  JOIN p.organization o" +
                " WHERE o.id = :orgId" +
                "   AND p.id = :planId" +
                " ORDER BY v.id DESC";
        Query query = entityManager.createQuery(jpql);
        query.setMaxResults(500);
        query.setParameter("orgId", orgId); //$NON-NLS-1$
        query.setParameter("planId", planId); //$NON-NLS-1$
        List<PlanVersionBean> planVersions = (List<PlanVersionBean>) query.getResultList();
        List<PlanVersionSummaryBean> rval = new ArrayList<>(planVersions.size());
        for (PlanVersionBean planVersion : planVersions) {
            PlanVersionSummaryBean pvsb = new PlanVersionSummaryBean();
            pvsb.setOrganizationId(planVersion.getPlan().getOrganization().getId());
            pvsb.setOrganizationName(planVersion.getPlan().getOrganization().getName());
            pvsb.setId(planVersion.getPlan().getId());
            pvsb.setName(planVersion.getPlan().getName());
            pvsb.setDescription(planVersion.getPlan().getDescription());
            pvsb.setVersion(planVersion.getVersion());
            pvsb.setStatus(planVersion.getStatus());
            rval.add(pvsb);
        }
        return rval;

    }

    /**
     * @see IStorageQuery#getPolicies(String, String, String, PolicyType)
     */
    @SuppressWarnings("nls")
    @Override
    public List<PolicySummaryBean> getPolicies(String organizationId, String entityId, String version,
                                               PolicyType type) throws StorageException {

        EntityManager entityManager = getActiveEntityManager();
        String jpql =
                "SELECT p from PolicyBean p "
                        + " WHERE p.organizationId = :orgId "
                        + "   AND p.entityId = :entityId "
                        + "   AND p.entityVersion = :entityVersion "
                        + "   AND p.type = :type"
                        + " ORDER BY p.orderIndex ASC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgId", organizationId);
        query.setParameter("entityId", entityId);
        query.setParameter("entityVersion", version);
        query.setParameter("type", type);

        List<PolicyBean> policyBeans = (List<PolicyBean>) query.getResultList();
        List<PolicySummaryBean> rval = new ArrayList<>(policyBeans.size());
        for (PolicyBean policyBean : policyBeans) {
/*                try {
                    PolicyTemplateUtil.generatePolicyDescription(policyBean);
                } catch (Exception e) {
                    throw new StorageException(e.getMessage());
                }*/
            PolicySummaryBean psb = new PolicySummaryBean();
            psb.setId(policyBean.getId());
            psb.setName(policyBean.getName());
            psb.setDescription(policyBean.getDescription());
            psb.setPolicyDefinitionId(policyBean.getDefinition().getId());
            psb.setIcon(policyBean.getDefinition().getIcon());
            psb.setCreatedBy(policyBean.getCreatedBy());
            psb.setCreatedOn(policyBean.getCreatedOn());
            rval.add(psb);
        }
        return rval;

    }

    /**
     * @see IStorageQuery#getMaxPolicyOrderIndex(String, String, String, PolicyType)
     */
    @Override
    public int getMaxPolicyOrderIndex(String organizationId, String entityId, String entityVersion,
                                      PolicyType type) throws StorageException {
        SearchCriteriaBean criteria = new SearchCriteriaBean();
        criteria.addFilter("organizationId", organizationId, SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        criteria.addFilter("entityId", entityId, SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        criteria.addFilter("entityVersion", entityVersion, SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        criteria.addFilter("type", type.name(), SearchCriteriaFilterOperator.eq); //$NON-NLS-1$
        criteria.setOrder("orderIndex", false); //$NON-NLS-1$
        criteria.setPage(1);
        criteria.setPageSize(1);
        SearchResultsBean<PolicyBean> resultsBean = find(criteria, PolicyBean.class);
        if (resultsBean.getBeans() == null || resultsBean.getBeans().isEmpty()) {
            return 0;
        } else {
            return resultsBean.getBeans().get(0).getOrderIndex();
        }
    }

    /**
     * @see IStorageQuery#listPluginPolicyDefs(Long)
     */
    @Override
    public List<PolicyDefinitionSummaryBean> listPluginPolicyDefs(Long pluginId) throws StorageException {

        EntityManager entityManager = getActiveEntityManager();

        @SuppressWarnings("nls")
        String sql =
                "SELECT pd.id, pd.name, pd.description, pd.icon, pd.plugin_id, pd.form_type" +
                        "  FROM policydefs pd" +
                        " WHERE pd.plugin_id = ?" +
                        " ORDER BY pd.name ASC";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, pluginId);

        List<Object[]> rows = (List<Object[]>) query.getResultList();
        List<PolicyDefinitionSummaryBean> beans = new ArrayList<>(rows.size());
        for (Object[] row : rows) {
            PolicyDefinitionSummaryBean bean = new PolicyDefinitionSummaryBean();
            bean.setId(String.valueOf(row[0]));
            bean.setName(String.valueOf(row[1]));
            bean.setDescription(String.valueOf(row[2]));
            bean.setIcon(String.valueOf(row[3]));
            if (row[4] != null) {
                bean.setPluginId(((Number) row[4]).longValue());
            }
            if (row[5] != null) {
                bean.setFormType(PolicyFormType.valueOf(String.valueOf(row[5])));
            }
            beans.add(bean);
        }
        return beans;
    }

    @Override
    public List<OAuthAppBean> listApplicationOAuthCredentials(Long appVersionId) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql =
                "SELECT oa from OAuthAppBean oa "
                        + " WHERE oa.app = :appVersionId "
                        + " ORDER BY oa.serviceVersion ASC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("appVersionId", appVersionId);

        List<OAuthAppBean> oauthCredentials = (List<OAuthAppBean>) query.getResultList();
        List<OAuthAppBean> rval = new ArrayList<>(oauthCredentials.size());
        for (OAuthAppBean cred : oauthCredentials) {
            OAuthAppBean res = new OAuthAppBean();
            res.setId(cred.getId());
            res.setServiceOrgId(cred.getServiceOrgId());
            res.setServiceId(cred.getServiceId());
            res.setServiceVersion(cred.getServiceVersion());
            res.setClientId(cred.getClientId());
            res.setClientSecret(cred.getClientSecret());
            res.setClientRedirect(cred.getClientRedirect());
            res.setApp(cred.getApp());
            rval.add(res);
        }
        return rval;
    }

    @Override
    public List<AnnouncementBean> listServiceAnnouncements(String organizationId, String serviceId) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql =
                "SELECT a from AnnouncementBean a "
                        + " WHERE a.organizationId = :orgId "
                        + "   AND a.serviceId = :serviceId "
                        + " ORDER BY a.createdOn DESC ";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgId", organizationId);
        query.setParameter("serviceId", serviceId);

        List<AnnouncementBean> announcementList = (List<AnnouncementBean>) query.getResultList();
        List<AnnouncementBean> rval = new ArrayList<>(announcementList.size());
        for (AnnouncementBean ann : announcementList) {
            AnnouncementBean res = new AnnouncementBean();
            res.setId(ann.getId());
            res.setOrganizationId(ann.getOrganizationId());
            res.setServiceId(ann.getServiceId());
            res.setTitle(ann.getTitle());
            res.setDescription(ann.getDescription());
            res.setCreatedBy(ann.getCreatedBy());
            res.setCreatedOn(ann.getCreatedOn());
            rval.add(res);
        }
        return rval;
    }

    @Override
    public List<SupportBean> listServiceSupportTickets(String organizationId, String serviceId) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql =
                "SELECT sb from SupportBean sb "
                        + " WHERE sb.organizationId = :orgId "
                        + "   AND sb.serviceId = :serviceId "
                        + " ORDER BY sb.createdOn DESC ";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("orgId", organizationId);
        query.setParameter("serviceId", serviceId);

        List<SupportBean> supportList = (List<SupportBean>) query.getResultList();
        List<SupportBean> rval = new ArrayList<>(supportList.size());
        for (SupportBean sup : supportList) {
            SupportBean res = new SupportBean();
            res.setId(sup.getId());
            res.setOrganizationId(sup.getOrganizationId());
            res.setServiceId(sup.getServiceId());
            res.setStatus(sup.getStatus());
            res.setTitle(sup.getTitle());
            res.setDescription(sup.getDescription());
            res.setCreatedBy(sup.getCreatedBy());
            res.setCreatedOn(sup.getCreatedOn());
            res.setTotalComments(sup.getTotalComments());
            rval.add(res);
        }
        return rval;
    }

    @Override
    public List<SupportComment> listServiceSupportComment(Long supportBeanId) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT sc from SupportComment sc "
                + " WHERE sc.supportId = :supportId "
                + " ORDER BY sc.createdOn DESC ";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("supportId", supportBeanId);

        List<SupportComment> supportList = (List<SupportComment>) query.getResultList();
        List<SupportComment> rval = new ArrayList<>(supportList.size());
        for (SupportComment sup : supportList) {
            SupportComment res = new SupportComment();
            res.setId(sup.getId());
            res.setSupportId(sup.getSupportId());
            res.setComment(sup.getComment());
            res.setSupportId(res.getSupportId());
            res.setCreatedBy(sup.getCreatedBy());
            res.setCreatedOn(sup.getCreatedOn());
            rval.add(res);
        }
        return rval;
    }

    @Override
    public List<ManagedApplicationBean> listAvailableMarkets() throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT m FROM ManagedApplicationBean m WHERE m.type = :appType OR m.type = :appType1 ORDER BY m.name ASC";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("appType", ManagedApplicationTypes.InternalMarketplace);
        query.setParameter("appType1", ManagedApplicationTypes.ExternalMarketplace);
        return query.getResultList();
    }

    @Override
    public List<WhitelistBean> listWhitelistRecords() throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT w FROM WhitelistBean w";
        Query query = entityManager.createQuery(jpql);
        List<WhitelistBean> rows = query.getResultList();
        return rows;
    }

    @Override
    public List<BlacklistBean> listBlacklistRecords() throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT b FROM BlacklistBean b";
        Query query = entityManager.createQuery(jpql);
        List<BlacklistBean> rows = query.getResultList();
        return rows;
    }

    @Override
    public PolicyBean getApplicationACLPolicy(String organizationId, String applicationId, String version, Long contractId, String gatewayId) throws StorageException {
        try {
            EntityManager entityManager = getActiveEntityManager();
            String jpql = "SELECT p from PolicyBean p JOIN p.definition d WHERE p.organizationId = :orgId AND p.entityId = :appId AND p.entityVersion = :version AND p.contractId = :contrId AND d.id = 'ACL' AND p.gatewayId = :gwId"; //$NON-NLS-1$
            Query query = entityManager.createQuery(jpql);
            query.setParameter("orgId", organizationId); //$NON-NLS-1$
            query.setParameter("appId", applicationId); //$NON-NLS-1$
            query.setParameter("version", version); //$NON-NLS-1$
            query.setParameter("contrId", contractId); //$NON-NLS-1$
            query.setParameter("gwId", gatewayId);
            return (PolicyBean) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        }
    }

    @Override
    public List<PolicyBean> getApplicationVersionContractPolicies(String organizationId, String applicationId, String version, Long contractId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT p FROM PolicyBean p WHERE p.organizationId = :orgId AND p.entityId = :appId AND p.entityVersion = :version AND p.contractId = :contractId";
        return em.createQuery(jpql)
                .setParameter("orgId", organizationId)
                .setParameter("appId", applicationId)
                .setParameter("version", version)
                .setParameter("contractId", contractId)
                .getResultList();
    }

    @Override
    public List<ManagedApplicationBean> getManagedApps() throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT m FROM ManagedApplicationBean m WHERE m.type = :appType OR m.type = :appType1 OR m.type = :appType2 OR m.type = :appType3";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("appType", ManagedApplicationTypes.InternalMarketplace);
        query.setParameter("appType1", ManagedApplicationTypes.ExternalMarketplace);
        query.setParameter("appType3", ManagedApplicationTypes.Consent);
        List<ManagedApplicationBean> rows = query.getResultList();
        return rows;
    }

    @Override
    public List<PolicyBean> getManagedAppACLPolicies(String organizationId, String serviceId, String version) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String content = new StringBuilder().append("%")
                .append(ServiceConventionUtil.generateServiceUniqueName(organizationId, serviceId, version))
                .append("%").toString();
        String jpql = "SELECT p FROM PolicyBean p WHERE (p.type = :polType OR p.type = :polType2) AND p.configuration LIKE :content";
        return entityManager.createQuery(jpql)
                .setParameter("polType", PolicyType.Marketplace)
                .setParameter("polType2", PolicyType.Consent)
                .setParameter("content", content)
                .getResultList();
    }

    @Override
    public List<ApplicationVersionBean> findAllApplicationVersions() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT a FROM ApplicationVersionBean a";
        return em.createQuery(jpql).getResultList();
    }

    @Override
    public List<ApplicationBean> findAllApplications() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT a FROM ApplicationBean a";
        return em.createQuery(jpql).getResultList();
    }

    @Override
    public List<PlanBean> findAllPlans(String organizationId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT p FROM PlanBean p WHERE organization = :orgId";
        return (List<PlanBean>) em.createQuery(jpql)
                .setParameter("orgId",organizationId)
                .getResultList();
    }

    @Override
    public List<PlanVersionBean> findAllPlanVersionBeans(String organizationId, String planId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT p FROM PlanVersionBean p WHERE plan_id = :planId AND plan_org_id = :orgId";
        return (List<PlanVersionBean>) em.createQuery(jpql)
                .setParameter("orgId",organizationId)
                .setParameter("planId",planId)
                .getResultList();
    }

    @Override
    public List<ServiceVersionBean> findServiceVersionsByAvailability(String prefix) throws StorageException {
        List<ServiceVersionBean> returnValue = new ArrayList<>();
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT s FROM ServiceVersionBean s WHERE s.status = :status";
        List<ServiceVersionBean> svbs = (List<ServiceVersionBean>) em.createQuery(jpql)
                .setParameter("status", ServiceStatus.Published)
                .getResultList();
        svbs.forEach(sv -> {
            sv.getVisibility().forEach(vis -> {
                if (vis.getCode().equals(prefix)) {
                    returnValue.add(sv);
                }
            });
        });
        return returnValue;
    }

    @Override
    public EventBean getEvent(Long id) throws StorageException {
        return super.get(id, EventBean.class);
    }

    @Override
    public MailTemplateBean getMailTemplate(MailTopic mailTopic) throws StorageException {
        return super.get(mailTopic.getTopicName(),MailTemplateBean.class);
    }

    @Override
    public ManagedApplicationBean getManagedApplicationBean(Long id) throws StorageException {
        return super.get(id, ManagedApplicationBean.class);
    }

    @Override
    public ManagedApplicationBean getManagedApplicationBean(AppIdentifier app) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        if(app!=null){
            String jpql = "SELECT m FROM ManagedApplicationBean m WHERE m.prefix = :appPrefix AND m.appId = :appId AND m.version = :appVersion";
            Query query = entityManager.createQuery(jpql);
            query.setParameter("appPrefix", app.getPrefix());
            query.setParameter("appId", app.getAppId());
            query.setParameter("appVersion", app.getVersion());
            List<ManagedApplicationBean> rows = query.getResultList();
            if(rows.size()>0)return rows.get(0);
            else return null;
        }else return null;
    }

    @Override
    public KeyMappingBean getKeyMappingBean(String fromSpecType, String toSpecType, String fromSpecClaim)throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT k FROM KeyMappingBean k WHERE k.fromSpecType = :fromSpecType AND k.toSpecType = :toSpecType AND k.fromSpecClaim = :fromSpecClaim";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("fromSpecType", fromSpecType);
        query.setParameter("toSpecType", toSpecType);
        query.setParameter("fromSpecClaim", fromSpecClaim);
        List<KeyMappingBean> rows = query.getResultList();
        if(rows.size()>0)return rows.get(0);
        else return null;
    }

    @Override
    public EventBean getEventByOriginDestinationAndType(String origin, String destination, EventType type) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT e FROM EventBean e WHERE e.originId = :origin AND e.destinationId = :destination AND e.type = :eventType";
        try {
            return (EventBean) em.createQuery(jpql)
                    .setParameter("origin", origin)
                    .setParameter("destination", destination)
                    .setParameter("eventType", type)
                    .getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public Integer getPublishedServiceCountForOrg(String orgId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT count(s.id) FROM ServiceVersionBean s JOIN s.service v WHERE v.organization.id = :orgId AND s.status = :status";
        Query query = em.createQuery(jpql);
        query.setParameter("orgId", orgId);
        query.setParameter("status", ServiceStatus.Published);
        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public Integer getLockedPlanCountForOrg(String orgId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT count(p.id) FROM PlanVersionBean p JOIN p.plan v WHERE v.organization.id = :orgId AND p.status = :status";
        Query query = em.createQuery(jpql);
        query.setParameter("orgId", orgId);
        query.setParameter("status", PlanStatus.Locked);
        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public Integer getMemberCountForOrg(String orgId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT count(m.id) FROM RoleMembershipBean m WHERE m.organizationId = :orgId";
        Query query = em.createQuery(jpql);
        query.setParameter("orgId", orgId);
        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public Integer getRegisteredApplicationCountForOrg(String orgId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT count(a.id) FROM ApplicationVersionBean a JOIN a.application v WHERE v.organization.id = :orgId AND a.status = :status AND v.context = :appContext";
        Query query = em.createQuery(jpql);
        query.setParameter("orgId", orgId);
        query.setParameter("status", ApplicationStatus.Registered);
        query.setParameter("appContext", appContext.getApplicationPrefix());
        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public Integer getEventCountForOrg(String orgId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT count(e.id) FROM EventBean e WHERE (e.destinationId LIKE :orgIdDelimited OR e.destinationId = :orgId) AND (e.type = :eventContractRQPending OR e.type = :eventMemberRQPending)";
        Query query = em.createQuery(jpql);
        query.setParameter("orgIdDelimited", orgId+".%");//orgid ends with '.'
        query.setParameter("orgId", orgId);
        query.setParameter("eventContractRQPending", EventType.CONTRACT_PENDING);
        query.setParameter("eventMemberRQPending", EventType.MEMBERSHIP_PENDING);
        return ((Long) query.getSingleResult()).intValue();
    }

    @Override
    public EventBean getUniqueEvent(EventBean bean) throws StorageException {
        try{
            EntityManager em = getActiveEntityManager();
            String jpql = "SELECT e FROM EventBean e WHERE e.destinationId = :destination AND e.originId = :origin AND e.type = :eventType";
            Object res = em.createQuery(jpql)
                    .setParameter("destination",bean.getDestinationId())
                    .setParameter("origin",bean.getOriginId())
                    .setParameter("eventType",bean.getType())
                    .getSingleResult();
            if(res!=null && res instanceof EventBean ) return (EventBean)res;
            else return null;
        }catch(NoResultException nre){
            //ignore
            return null;
        }
    }

    @Override
    public List<EventBean> getAllIncomingEvents(String destination) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT e FROM EventBean e WHERE e.destinationId LIKE :destination ORDER BY e.createdOn";
        return em.createQuery(jpql)
                .setParameter("destination", destination)
                .getResultList();
    }

    @Override
    public List<EventBean> getAllOutgoingEvents(String origin) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT e FROM EventBean e WHERE e.originId LIKE :origin ORDER BY e.createdOn";
        return em.createQuery(jpql)
                .setParameter("origin", origin)
                .getResultList();
    }

    @Override
    public List<EventBean> getIncomingEventsByType(String destination, EventType type) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT e FROM EventBean e WHERE e.destinationId LIKE :destination AND e.type = :eventType ORDER BY e.createdOn";
        return em.createQuery(jpql)
                .setParameter("destination", destination)
                .setParameter("eventType", type)
                .getResultList();
    }

    @Override
    public List<EventBean> getOutgoingEventsByType(String origin, EventType type) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT e FROM EventBean e WHERE e.originId LIKE :origin AND e.type = :eventType ORDER BY e.createdOn";
        return em.createQuery(jpql)
                .setParameter("origin", origin)
                .setParameter("eventType", type)
                .getResultList();
    }

    @Override
    public List<EventBean> getAllIncomingNonActionEvents(String destination) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT e FROM EventBean e WHERE e.destinationId LIKE :destination AND e.type <> :contrPending AND e.type <> :membershipPending ORDER BY e.createdOn";
        return em.createQuery(jpql)
                .setParameter("destination", destination)
                .setParameter("contrPending", EventType.CONTRACT_PENDING)
                .setParameter("membershipPending", EventType.MEMBERSHIP_PENDING)
                .getResultList();
    }

    @Override
    public List<EventBean> getAllIncomingActionEvents(String destination) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT e FROM EventBean e WHERE e.destinationId LIKE :destination AND (e.type = :contrPending OR e.type = :membershipPending) ORDER BY e.createdOn";
        return em.createQuery(jpql)
                .setParameter("destination", destination)
                .setParameter("contrPending", EventType.CONTRACT_PENDING)
                .setParameter("membershipPending", EventType.MEMBERSHIP_PENDING)
                .getResultList();
    }

    @Override
    public void deleteAllEventsForEntity(String entityId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "DELETE FROM EventBean e WHERE e.destinationId LIKE :eId OR e.originId LIKE :eId";
        em.createQuery(jpql)
                .setParameter("eId", entityId).executeUpdate();
    }

    @Override
    public List<ServiceVersionBean> findLatestServiceVersionByStatus(ServiceStatus status) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT s FROM ServiceVersionBean s WHERE s.createdOn IN (SELECT MAX(s2.createdOn) FROM ServiceVersionBean s2 WHERE s2.status = :status GROUP BY s2.service) ORDER BY s.service.name";
        List<ServiceVersionBean> rval = (List<ServiceVersionBean>) em.createQuery(jpql)
                .setParameter("status", status)
                .getResultList();
        return doNotFilterServices() ? rval : ServiceScopeUtil.resolveSVBScope(rval, appContext.getApplicationPrefix(), security.isAdmin());
    }

    @Override
    public List<ServiceVersionBean> findLatestServiceVersionByStatusAndServiceName(String serviceName, ServiceStatus status) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT s FROM ServiceVersionBean s WHERE s.createdOn IN (SELECT MAX(s2.createdOn) FROM ServiceVersionBean s2 WHERE s2.status = :status AND LOWER(s2.service.name) LIKE :name GROUP BY s2.service) ORDER BY s.service.name";
        List<ServiceVersionBean> rval = (List<ServiceVersionBean>) em.createQuery(jpql)
                .setParameter("status", status)
                .setParameter("name", serviceName.toLowerCase())
                .getResultList();
        return doNotFilterServices() ? rval : ServiceScopeUtil.resolveSVBScope(rval, appContext.getApplicationPrefix(), security.isAdmin());
    }

    @Override
    public ManagedApplicationBean resolveManagedApplicationByAPIKey(String apiKey) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT m FROM ManagedApplicationBean m WHERE m.apiKey = :apiKey";
        try {
            return (ManagedApplicationBean) em.createQuery(jpql)
                    .setParameter("apiKey", apiKey)
                    .getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ManagedApplicationBean findManagedApplication(String prefix) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT m FROM ManagedApplicationBean m WHERE m.prefix = :appPrefix";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("appPrefix", prefix);
        List<ManagedApplicationBean> rows = query.getResultList();
        if(rows.size()>0)return rows.get(0);
        else return null;
    }

    @Override
    public List<ManagedApplicationBean> findManagedApplication(ManagedApplicationTypes type) throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT m FROM ManagedApplicationBean m WHERE m.type = :mType";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("mType", type);
        return query.getResultList();
    }

    @Override
    public List<ManagedApplicationBean> findManagedApplications() throws StorageException {
        EntityManager entityManager = getActiveEntityManager();
        String jpql = "SELECT m FROM ManagedApplicationBean m";
        Query query = entityManager.createQuery(jpql);
        return query.getResultList();
    }

    @Override
    public Set<OrganizationBean> getServiceContractHolders(ServiceBean service) throws StorageException {
        Set<OrganizationBean> returnValue = new HashSet<>();
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT o FROM OrganizationBean o WHERE o IN (SELECT a.application.organization FROM ApplicationVersionBean a WHERE a IN (SELECT c.application FROM ContractBean c WHERE c.service.service = :service))";
        returnValue.addAll(em.createQuery(jpql)
                .setParameter("service", service)
                .getResultList());
        return returnValue;
    }

    public ServiceBean getServiceByBasepath(String organizationId, String basepath) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT s FROM ServiceBean s WHERE s.basepath = :bpath AND s.organization.id = :orgId";
        try {
            return (ServiceBean) em.createQuery(jpql)
                    .setParameter("bpath", basepath)
                    .setParameter("orgId", organizationId)
                    .getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public ApplicationVersionSummaryBean resolveApplicationVersionByAPIKey(String apiKey) throws StorageException {
        ApplicationVersionSummaryBean rval = null;
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT a FROM ApplicationVersionBean a WHERE a IN (SELECT c.application FROM ContractBean c WHERE c.apikey = :apiKey)";
        ApplicationVersionBean result = null;
        try {
            result = (ApplicationVersionBean) em.createQuery(jpql)
                    .setParameter("apiKey", apiKey)
                    .getSingleResult();
        } catch (NoResultException ex) {
            //Do nothing
        }
        if (result != null) {
            rval = new ApplicationVersionSummaryBean();
            rval.setOrganizationId(result.getApplication().getOrganization().getId());
            rval.setOrganizationName(result.getApplication().getOrganization().getName());
            rval.setId(result.getApplication().getId());
            rval.setName(result.getApplication().getName());
            rval.setDescription(result.getApplication().getDescription());
            rval.setVersion(result.getVersion());
            rval.setStatus(result.getStatus());
        }
        return rval;
    }

    @Override
    public void deleteAclPolicies() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "DELETE FROM PolicyBean p WHERE p.definition.id = :polDefId";
        em.createQuery(jpql)
                .setParameter("polDefId", Policies.ACL.name())
                .executeUpdate();
    }

    @Override
    public void deleteContractPolicies() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "DELETE FROM PolicyBean p WHERE p.type = :pType";
        em.createQuery(jpql)
                .setParameter("pType", PolicyType.Contract)
                .executeUpdate();
    }

    @Override
    public void updateApplicationVersionApiKey(ApplicationVersionBean avb, String apiKey) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "UPDATE ContractBean c SET c.apikey = :newKey WHERE c.application = :avb";
        em.createQuery(jpql)
                .setParameter("newKey", apiKey)
                .setParameter("avb", avb)
                .executeUpdate();
    }

    @Override
    public List<ApplicationVersionBean> getAllNonRetiredApplicationVersions() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT a FROM ApplicationVersionBean a WHERE a.status <> :status";
        return em.createQuery(jpql)
                .setParameter("status", ApplicationStatus.Retired)
                .getResultList();
    }

    @Override
    public List<EventBean> getAllEventsRelatedToOrganization(String orgId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT e FROM EventBean e WHERE e.destinationId = :orgId OR e.destinationId LIKE :orgLike OR e.originId = :orgId OR e.originId LIKE :orgLike";
        return em.createQuery(jpql)
                .setParameter("orgId", orgId)
                .setParameter("orgLike", orgId + ".%")
                .getResultList();
    }

    @Override
    public List<ContractBean> getServiceContracts(ServiceBean service) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT c FROM ContractBean c WHERE c.service.service = :svc";
        return em.createQuery(jpql)
                .setParameter("svc", service)
                .getResultList();
    }

    @Override
    public List<ServiceVersionBean> getServiceVersionsInOrgByStatus(String organizationId, ServiceStatus status) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT s FROM ServiceVersionBean s WHERE s.service.organization.id = :orgId AND s.status = :status";
        return em.createQuery(jpql)
                .setParameter("orgId", organizationId)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<PolicyBean> listPoliciesForEntity(String organizationId, String entityId, String version, PolicyType type) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT p FROM PolicyBean p WHERE p.organizationId = :orgId AND p.entityId = :entId AND p.entityVersion = :version AND p.type = :pType";
        return em.createQuery(jpql)
                .setParameter("orgId", organizationId)
                .setParameter("entId", entityId)
                .setParameter("version", version)
                .setParameter("pType", type)
                .getResultList();
    }

    @Override
    public List<AuditEntryBean> listAuditEntriesForEntity(String organizationId, String entityId, String version, AuditEntityType type) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT a FROM AuditEntryBean a WHERE a.organizationId = :orgId AND a.entityId = :entId AND a.entityVersion = :version AND a.entityType = :aType";
        return em.createQuery(jpql)
                .setParameter("orgId", organizationId)
                .setParameter("entId", entityId)
                .setParameter("version", version)
                .setParameter("aType", type)
                .getResultList();
    }

    @Override
    public List<OrganizationBean> getAllOrgs() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT o FROM OrganizationBean o";
        return em.createQuery(jpql)
                .getResultList();
    }

    @Override
    public List<KeyMappingBean> getAllKeyMapping() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT k FROM KeyMappingBean k";
        return em.createQuery(jpql).getResultList();
    }

    @Override
    public List<KeyMappingBean> getKeyMapping(String fromSpec, String toSpec) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT k FROM KeyMappingBean k WHERE k.fromSpecType = :fromSpecType AND k.toSpecType = :toSpecType";
        return em.createQuery(jpql)
                .setParameter("fromSpecType", fromSpec)
                .setParameter("toSpecType", toSpec)
                .getResultList();
    }

    @Override
    public void createDefaults(DefaultsBean defaultsBean) throws StorageException {
        super.create(defaultsBean);
    }

    @Override
    public void createConfig(ConfigBean config) throws StorageException {
        super.create(config);
    }

    @Override
    public void updateDefaults(DefaultsBean defaultsBean) throws StorageException {
        super.update(defaultsBean);
    }

    @Override
    public void updateConfig(ConfigBean config) throws StorageException {
        super.update(config);
    }

    @Override
    public void deleteDefaults(DefaultsBean defaultsBean) throws StorageException {
        super.delete(defaultsBean);
    }

    @Override
    public void deleteConfig(ConfigBean configBean) throws StorageException {
        super.delete(config);
    }

    @Override
    public DefaultsBean getDefaults(String id) throws StorageException {
        return super.get(id, DefaultsBean.class);
    }

    @Override
    public List<ConfigBean> getDefaultConfig() throws StorageException {
        CriteriaBuilder cb = getActiveEntityManager().getCriteriaBuilder();
        CriteriaQuery<ConfigBean> cq = cb.createQuery(ConfigBean.class);
        Root<ConfigBean> rootEntry = cq.from(ConfigBean.class);
        CriteriaQuery<ConfigBean> all = cq.select(rootEntry);
        TypedQuery<ConfigBean> defaultConfig = em.createQuery(all);
        return defaultConfig.getResultList();
    }

    @Override
    public Set<String> getManagedAppPrefixesForTypes(List<ManagedApplicationTypes> types) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT p.prefix FROM ManagedApplicationBean p WHERE p.type IN :types";
        return new HashSet<>(em.createQuery(jpql)
                .setParameter("types", types)
                .getResultList());
    }

    @Override
    public List<GatewayBean> getAllGateways() throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT g FROM GatewayBean g";
        return em.createQuery(jpql).getResultList();
    }

    @Override
    public List<PolicyBean> getEntityPoliciesByDefinitionId(String organizationId, String entityId, String version, PolicyType type, Policies definitionId) throws StorageException {
        EntityManager em = getActiveEntityManager();
        String jpql = "SELECT p FROM PolicyBean p WHERE p.organizationId = :orgId " +
                "AND p.entityId = :entId " +
                "AND p.entityVersion = :version " +
                "AND p.type = :type " +
                "AND p.definition.id = :defId";
        return em.createQuery(jpql)
                .setParameter("orgId", organizationId)
                .setParameter("entId", entityId)
                .setParameter("version", version)
                .setParameter("type", type)
                .setParameter("defId", definitionId.getPolicyDefId())
                .getResultList();
    }

    private boolean doNotFilterServices() throws StorageException {
        return getManagedAppPrefixesForTypes(Arrays.asList(ManagedApplicationTypes.Consent, ManagedApplicationTypes.Publisher)).contains(appContext.getApplicationPrefix());
    }
}
