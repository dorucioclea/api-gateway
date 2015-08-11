package com.t1t.digipolis.apim.jpa;

import com.t1t.digipolis.apim.beans.apps.ApplicationBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.audit.AuditEntityType;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayType;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.plans.PlanBean;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.plugins.PluginBean;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaFilterOperator;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.services.*;
import com.t1t.digipolis.apim.beans.summary.*;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.core.util.PolicyTemplateUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * A JPA implementation of the storage interface.
 *
 */
@ApplicationScoped @Alternative
public class JpaStorage extends AbstractJpaStorage implements IStorage, IStorageQuery {

    private static Logger logger = LoggerFactory.getLogger(JpaStorage.class);

    /**
     * Constructor.
     */
    public JpaStorage() {
    }
    
    /**
     * @see IStorage#beginTx()
     */
    @Override
    public void beginTx() throws StorageException {
        super.beginTx();
    }
    
    /**
     * @see IStorage#commitTx()
     */
    @Override
    public void commitTx() throws StorageException {
        super.commitTx();
    }
    
    /**
     * @see IStorage#rollbackTx()
     */
    @Override
    public void rollbackTx() {
        super.rollbackTx();
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
            byte [] data = baos.toByteArray();
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

    /**
     * @see AbstractJpaStorage#find(SearchCriteriaBean, Class)
     */
    @Override
    protected <T> SearchResultsBean<T> find(SearchCriteriaBean criteria, Class<T> type) throws StorageException {
        beginTx();
        try {
            SearchResultsBean<T> rval = super.find(criteria, type);
            return rval;
        } finally {
            commitTx();
        }
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
            osb.setName(bean.getName());
            osb.setDescription(bean.getDescription());
            rval.getBeans().add(osb);
        }
        return rval;
    }

    /**
     * @see IStorageQuery#findApplications(SearchCriteriaBean)
     */
    @Override
    public SearchResultsBean<ApplicationSummaryBean> findApplications(SearchCriteriaBean criteria)
            throws StorageException {
        SearchResultsBean<ApplicationBean> result = find(criteria, ApplicationBean.class);

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
    public SearchResultsBean<ServiceSummaryBean> findServices(SearchCriteriaBean criteria)
            throws StorageException {
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
        super.create(entry);
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
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();

            @SuppressWarnings("nls")
            String sql =
                    "SELECT g.id, g.name, g.description, g.type" +
                    "  FROM gateways g" +
                    " ORDER BY g.name ASC";
            Query query = entityManager.createNativeQuery(sql);

            List<Object[]> rows = (List<Object[]>) query.getResultList();
            List<GatewaySummaryBean> gateways = new ArrayList<>(rows.size());
            for (Object [] row : rows) {
                GatewaySummaryBean gateway = new GatewaySummaryBean();
                gateway.setId(String.valueOf(row[0]));
                gateway.setName(String.valueOf(row[1]));
                gateway.setDescription(String.valueOf(row[2]));
                gateway.setType(GatewayType.valueOf(String.valueOf(row[3])));
                gateways.add(gateway);
            }
            return gateways;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IStorageQuery#listPlugins()
     */
    @Override
    public List<PluginSummaryBean> listPlugins() throws StorageException {
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();

            @SuppressWarnings("nls")
            String sql =
                    "SELECT p.id, p.artifact_id, p.group_id, p.version, p.classifier, p.type, p.name, p.description, p.created_by, p.created_on" +
                    "  FROM plugins p" +
                    " ORDER BY p.name ASC";
            Query query = entityManager.createNativeQuery(sql);

            List<Object[]> rows = (List<Object[]>) query.getResultList();
            List<PluginSummaryBean> plugins = new ArrayList<>(rows.size());
            for (Object [] row : rows) {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IStorageQuery#listPolicyDefinitions()
     */
    @Override
    public List<PolicyDefinitionSummaryBean> listPolicyDefinitions() throws StorageException {
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();

            @SuppressWarnings("nls")
            String sql =
                    "SELECT pd.id, pd.policy_impl, pd.name, pd.description, pd.icon, pd.plugin_id, pd.form_type" +
                    "  FROM policydefs pd" +
                    " ORDER BY pd.name ASC";
            Query query = entityManager.createNativeQuery(sql);

            List<Object[]> rows = (List<Object[]>) query.getResultList();
            List<PolicyDefinitionSummaryBean> rval = new ArrayList<>(rows.size());
            for (Object [] row : rows) {
                PolicyDefinitionSummaryBean bean = new PolicyDefinitionSummaryBean();
                bean.setId(String.valueOf(row[0]));
                bean.setPolicyImpl(String.valueOf(row[1]));
                bean.setName(String.valueOf(row[2]));
                bean.setDescription(String.valueOf(row[3]));
                bean.setIcon(String.valueOf(row[4]));
                if (row[5] != null) {
                    bean.setPluginId(((Number) row[5]).longValue());
                }
                if (row[6] != null) {
                    bean.setFormType(PolicyFormType.valueOf(String.valueOf(row[6])));
                }
                rval.add(bean);
            }
            return rval;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IStorageQuery#getOrgs(Set)
     */
    @Override
    public List<OrganizationSummaryBean> getOrgs(Set<String> orgIds) throws StorageException {
        List<OrganizationSummaryBean> orgs = new ArrayList<>();
        if (orgIds == null || orgIds.isEmpty()) {
            return orgs;
        }
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            String jpql = "SELECT o from OrganizationBean o WHERE o.id IN :orgs ORDER BY o.id ASC"; //$NON-NLS-1$
            Query query = entityManager.createQuery(jpql);
            query.setParameter("orgs", orgIds); //$NON-NLS-1$
            List<OrganizationBean> qr = (List<OrganizationBean>) query.getResultList();
            for (OrganizationBean bean : qr) {
                OrganizationSummaryBean summary = new OrganizationSummaryBean();
                summary.setId(bean.getId());
                summary.setName(bean.getName());
                summary.setDescription(bean.getDescription());
                orgs.add(summary);
            }
            return orgs;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
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
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            String jpql = "SELECT a FROM ApplicationBean a JOIN a.organization o WHERE o.id IN :orgs ORDER BY a.id ASC"; //$NON-NLS-1$
            Query query = entityManager.createQuery(jpql);
            query.setParameter("orgs", orgIds); //$NON-NLS-1$

            List<ApplicationBean> qr = (List<ApplicationBean>) query.getResultList();
            for (ApplicationBean bean : qr) {
                ApplicationSummaryBean summary = new ApplicationSummaryBean();
                summary.setId(bean.getId());
                summary.setName(bean.getName());
                summary.setDescription(bean.getDescription());
                // TODO find the number of contracts - probably need a native SQL query to pull that together
                summary.setNumContracts(0);
                OrganizationBean org = bean.getOrganization();
                summary.setOrganizationId(org.getId());
                summary.setOrganizationName(org.getName());
                rval.add(summary);
            }
            return rval;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
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
        beginTx();
        try {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
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
        beginTx();
        try {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IStorageQuery#getServiceVersionPlans(String, String, String)
     */
    @Override
    public List<ServicePlanSummaryBean> getServiceVersionPlans(String organizationId, String serviceId,
            String version) throws StorageException {
        List<ServicePlanSummaryBean> plans = new ArrayList<>();

        beginTx();
        try {
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
        } catch (StorageException e) {
            rollbackTx();
            throw e;
        } finally {
            commitTx();
        }
    }

    /**
     * @see IStorageQuery#getServiceContracts(String, String, String, int, int)
     */
    @Override
    public List<ContractSummaryBean> getServiceContracts(String organizationId, String serviceId,
            String version, int page, int pageSize) throws StorageException {
        int start = (page - 1) * pageSize;
        beginTx();
        try {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
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
        beginTx();
        try {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IStorageQuery#getApplicationContracts(String, String, String)
     */
    @Override
    public List<ContractSummaryBean> getApplicationContracts(String organizationId, String applicationId,
            String version) throws StorageException {
        List<ContractSummaryBean> rval = new ArrayList<>();

        beginTx();
        try {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
        return rval;
    }

    /**
     * @see IStorageQuery#getApiRegistry(String, String, String)
     */
    @Override
    public ApiRegistryBean getApiRegistry(String organizationId, String applicationId, String version)
            throws StorageException {
        ApiRegistryBean rval = new ApiRegistryBean();

        beginTx();
        try {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
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
        beginTx();
        try {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
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
        beginTx();
        try {
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
    }

    /**
     * @see IStorageQuery#getPolicies(String, String, String, PolicyType)
     */
    @SuppressWarnings("nls")
    @Override
    public List<PolicySummaryBean> getPolicies(String organizationId, String entityId, String version,
            PolicyType type) throws StorageException {
        beginTx();
        try {
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
                PolicyTemplateUtil.generatePolicyDescription(policyBean);
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
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
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
        beginTx();
        try {
            EntityManager entityManager = getActiveEntityManager();
            
            @SuppressWarnings("nls")
            String sql = 
                    "SELECT pd.id, pd.policy_impl, pd.name, pd.description, pd.icon, pd.plugin_id, pd.form_type" +
                    "  FROM policydefs pd" + 
                    " WHERE pd.plugin_id = ?" +
                    " ORDER BY pd.name ASC";
            Query query = entityManager.createNativeQuery(sql);
            query.setParameter(1, pluginId);

            List<Object[]> rows = (List<Object[]>) query.getResultList();
            List<PolicyDefinitionSummaryBean> beans = new ArrayList<>(rows.size());
            for (Object [] row : rows) {
                PolicyDefinitionSummaryBean bean = new PolicyDefinitionSummaryBean();
                bean.setId(String.valueOf(row[0]));
                bean.setPolicyImpl(String.valueOf(row[1]));
                bean.setName(String.valueOf(row[2]));
                bean.setDescription(String.valueOf(row[3]));
                bean.setIcon(String.valueOf(row[4]));
                if (row[5] != null) {
                    bean.setPluginId(((Number) row[5]).longValue());
                }
                if (row[6] != null) {
                    bean.setFormType(PolicyFormType.valueOf(String.valueOf(row[6])));
                }
                beans.add(bean);
            }
            return beans;
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
            throw new StorageException(t);
        } finally {
            commitTx();
        }
    }

}