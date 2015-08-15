package com.t1t.digipolis.apim.core;

import com.t1t.digipolis.apim.beans.apps.ApplicationBean;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.contracts.ContractBean;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.plans.PlanBean;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.plugins.PluginBean;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyDefinitionBean;
import com.t1t.digipolis.apim.beans.policies.PolicyType;
import com.t1t.digipolis.apim.beans.services.ServiceBean;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;

import java.io.InputStream;
import java.util.List;

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

    /*
     * Anything that doesn't fall into the above categories!
     */
    public void reorderPolicies(PolicyType type, String organizationId, String entityId,
                                String entityVersion, List<Long> newOrder) throws StorageException;
}
