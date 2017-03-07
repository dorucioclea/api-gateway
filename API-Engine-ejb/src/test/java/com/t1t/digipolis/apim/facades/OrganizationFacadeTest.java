package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.apps.*;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.audit.data.EntityUpdatedData;
import com.t1t.digipolis.apim.beans.idm.RoleBean;
import com.t1t.digipolis.apim.beans.orgs.NewOrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.digipolis.apim.beans.plans.PlanStatus;
import com.t1t.digipolis.apim.beans.plans.PlanVersionBean;
import com.t1t.digipolis.apim.beans.policies.*;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ContractSummaryBean;
import com.t1t.digipolis.apim.beans.summary.PolicySummaryBean;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.facades.audit.AuditUtils;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.gateway.dto.Policy;
import com.t1t.digipolis.apim.gateway.rest.GatewayValidation;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by michallispashidis on 22/10/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({OrganizationFacade.class, AuditUtils.class})
public class OrganizationFacadeTest {
    private static final Logger _LOG = LoggerFactory.getLogger(OrganizationFacadeTest.class.getName());
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock EntityManager em;
    @Mock AppConfig config;
    @Mock ISecurityContext securityContext;
    @Mock ISecurityAppContext appContext;
    @Mock IStorage storage;
    @Mock IStorageQuery query;
    @Mock IIdmStorage idmStorage;
    @Mock IApiKeyGenerator apiKeyGenerator;
    @Mock IApplicationValidator applicationValidator;
    @Mock IServiceValidator serviceValidator;
    @Mock IMetricsAccessor metrics;
    @Mock GatewayFacade gatewayFacade;
    @Mock GatewayValidation gatewayValidation;
    @Mock IGatewayLinkFactory gatewayLinkFactory;
    @Mock UserFacade userFacade;
    @Mock RoleFacade roleFacade;
    @InjectMocks OrganizationFacade orgFacade;

    @Test
    public void sanity() throws Exception {
        assertNotNull(em);
        assertNotNull(securityContext);
        assertNotNull(storage);
        assertNotNull(query);
        assertNotNull(idmStorage);
        assertNotNull(apiKeyGenerator);
        assertNotNull(applicationValidator);
        assertNotNull(serviceValidator);
        assertNotNull(metrics);
        assertNotNull(gatewayFacade);
        assertNotNull(gatewayLinkFactory);
        assertNotNull(userFacade);
        assertNotNull(roleFacade);
        assertNotNull(orgFacade);
    }

    @Test
    public void testCreateOrgAlreadyExists() throws Exception {
        when(idmStorage.findRoles(anyObject())).thenReturn(getDefaultSearchResultRoleBeans());
        when(securityContext.getCurrentUser()).thenReturn("admin");
        when(storage.getOrganization(anyString())).thenReturn(new OrganizationBean());
        thrown.expect(OrganizationAlreadyExistsException.class);
        orgFacade.create(getDefaultNewOrganization());
    }

    @Test
    public void testCreate() throws Exception {
        when(idmStorage.findRoles(anyObject())).thenReturn(getDefaultSearchResultRoleBeans());
        when(securityContext.getCurrentUser()).thenReturn("admin");
        when(storage.getOrganization(anyString())).thenReturn(null);
        OrganizationBean organizationBean = orgFacade.create(getDefaultNewOrganization());
        verify(storage).createOrganization(anyObject());
        verify(storage).createAuditEntry(anyObject());
        verify(idmStorage).createMembership(anyObject());
    }

    @Test
    public void testGetOrgNotFound() throws Exception {
        when(storage.getOrganization(anyString())).thenReturn(null);
        thrown.expect(OrganizationNotFoundException.class);
        orgFacade.get("somestring");
    }

    @Test
    public void testGet() throws Exception {
        OrganizationBean newOrg = new OrganizationBean();
        newOrg.setId("somestring");
        when(storage.getOrganization(anyString())).thenReturn(newOrg);
        orgFacade.get("somestring");
        verify(storage).getOrganization("somestring");
    }

    @Test
    public void testUpdateNoPermissions() throws Exception {
        when(securityContext.hasPermission(anyObject(), anyString())).thenReturn(false);
        thrown.expect(NotAuthorizedException.class);
        orgFacade.update("someorg", new UpdateOrganizationBean());
    }

    @Test
    public void testUpdateNullObject() throws Exception {
        when(securityContext.hasPermission(anyObject(),anyString())).thenReturn(true);
        when(storage.getOrganization(anyString())).thenReturn(null);
        thrown.expect(OrganizationNotFoundException.class);
        orgFacade.update("someorg",new UpdateOrganizationBean());
    }

    @Test
    public void testUpdate() throws Exception {
        when(securityContext.hasPermission(anyObject(),anyString())).thenReturn(true);
        when(storage.getOrganization(anyString())).thenReturn(new OrganizationBean());
        orgFacade.update("someorg", getDefaultUpdateOrganization());
        verify(storage).updateOrganization(anyObject());
        verify(storage).createAuditEntry(anyObject());
    }

    @Test
    public void testActivity() throws Exception {
        orgFacade.activity("someorg", 2, 2);
        PagingBean paging = new PagingBean();
        paging.setPage(2);
        paging.setPageSize(2);
        verify(query).auditEntity("someorg", null, null, null, paging);
    }

    @Test
    public void testActivityLowerLimit() throws Exception {
        orgFacade.activity("someorg",0,0);
        PagingBean paging = new PagingBean();
        paging.setPage(1);
        paging.setPageSize(20);
        verify(query).auditEntity("someorg",null,null,null,paging);
    }

    @Test
    public void testCreateAppNotAuthorized() throws Exception {
        when(securityContext.hasPermission(anyObject(),anyString())).thenReturn(false);
        thrown.expect(NotAuthorizedException.class);
        orgFacade.createApp("someorg", new NewApplicationBean());
    }

    @Test
    public void testCreateAppAlreadyExists() throws Exception {
        when(securityContext.hasPermission(anyObject(), anyString())).thenReturn(true);
        when(securityContext.getCurrentUser()).thenReturn("admin");
        when(appContext.getApplicationIdentifier()).thenReturn(new AppIdentifier());
        when(storage.getOrganization(anyString())).thenReturn(new OrganizationBean());
        when(storage.getApplication(anyString(), anyString())).thenReturn(new ApplicationBean());
        thrown.expect(ApplicationAlreadyExistsException.class);
        NewApplicationBean bean = new NewApplicationBean();
        bean.setName("someapp");
        bean.setBase64logo("");
        bean.setDescription("somedesc");
        orgFacade.createApp("someorg", bean);
    }

    @Test
    public void testCreateApp() throws Exception {
        when(securityContext.hasPermission(anyObject(), anyString())).thenReturn(true);
        when(securityContext.getCurrentUser()).thenReturn("admin");
        when(appContext.getApplicationIdentifier()).thenReturn(new AppIdentifier());
        when(storage.getOrganization(anyString())).thenReturn(new OrganizationBean());
        when(storage.getApplication(anyString(), anyString())).thenReturn(null);
        NewApplicationBean bean = new NewApplicationBean();
        bean.setName("someapp");
        bean.setBase64logo("");
        bean.setDescription("somedesc");
        orgFacade.createApp("someorg", bean);
        verify(storage).createApplication(anyObject());
        verify(storage).createAuditEntry(anyObject());
    }

    @Test
    public void testCreateAppVersionAppNotFound() throws Exception {
        when(storage.getApplication(anyString(), anyString())).thenReturn(null);
        thrown.expect(ApplicationNotFoundException.class);
        orgFacade.createAppVersion("someorg", "someapp", new NewApplicationVersionBean());
    }

    @Test
    public void testCreateAppVersionAlreadyExists() throws Exception {
        when(storage.getApplication(anyString(), anyString())).thenReturn(new ApplicationBean());
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(new ApplicationVersionBean());
        thrown.expect(ApplicationVersionAlreadyExistsException.class);
        orgFacade.createAppVersion("someorg", "someapp", new NewApplicationVersionBean());
    }

    @Test //TODO Fix test
    public void testCreateAppVersion() throws Exception {
//        NewApplicationVersionBean navb = new NewApplicationVersionBean();
//        navb.setVersion("newversion");
//        ApplicationBean ab = new ApplicationBean();
//        OrganizationBean ob = new OrganizationBean();
//        ob.setId("someorg");
//        ab.setOrganization(ob);
//        when(storage.getApplication(anyString(), anyString())).thenReturn(ab);
//        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(null);
//        when(gatewayFacade.createGatewayLink(anyString())).thenReturn(restGatewayLink);
//        orgFacade.createAppVersion("someorg", "someapp", navb);
//        verify(storage).createApplicationVersion(anyObject());
//        verify(storage).createAuditEntry(anyObject());
//        verify(gatewayFacade).createGatewayLink(anyString());
    }

    @Test //TODO Fix test
    public void testCreateAppVersionWithClone() throws Exception {
//        when(storage.getApplication(anyString(), anyString())).thenReturn(new ApplicationBean());
//        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(null);
//        when(orgFacade.createAppVersionInternal(anyObject(), anyObject())).thenReturn(new ApplicationVersionBean());
//        orgFacade.createAppVersion("someorg", "someapp", new NewApplicationVersionBean());
//        verify(orgFacade.createAppVersionInternal(anyObject(), anyObject()));
    }

    @Test
    public void testUpdateAppVersionURIAppNotFound() throws Exception {
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(null);
        thrown.expect(ApplicationNotFoundException.class);
        orgFacade.updateAppVersionURI("someorg", "someapp", "someversion", new UpdateApplicationVersionURIBean());
    }

    @Test
    public void testUpdateAppVersionURI() throws Exception {
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(new ApplicationVersionBean());
        when(securityContext.getCurrentUser()).thenReturn("admin");
        PowerMockito.mockStatic(AuditUtils.class);
        when(AuditUtils.applicationVersionUpdated(any(ApplicationVersionBean.class), any(EntityUpdatedData.class), any(ISecurityContext.class))).thenReturn(new AuditEntryBean());
        orgFacade.updateAppVersionURI("someorg", "someapp", "someversion", new UpdateApplicationVersionURIBean());
        verify(storage).updateApplicationVersion(anyObject());
    }

    @Test
    public void testCreateAppPolicyInvalidStatusRegistered() throws Exception {
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setStatus(ApplicationStatus.Registered);
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        avb.setApplication(ab);
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        thrown.expect(InvalidApplicationStatusException.class);
        orgFacade.createAppPolicy("someorg", "someapp", "someversion", new NewPolicyBean());

    }

    @Test
    public void testCreateAppPolicyInvalidStatusRetired() throws Exception {
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setStatus(ApplicationStatus.Retired);
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        avb.setApplication(ab);
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        thrown.expect(InvalidApplicationStatusException.class);
        orgFacade.createAppPolicy("someorg", "someapp", "someversion", new NewPolicyBean());
    }

    @Ignore
    @Test
    public void testCreateAppPolicy() throws Exception {
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setStatus(ApplicationStatus.Ready);
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        avb.setApplication(ab);
        NewPolicyBean npb = new NewPolicyBean();
        npb.setDefinitionId("rate-limiting");
        npb.setConfiguration("");
        PolicyDefinitionBean pdb = new PolicyDefinitionBean();
        pdb.setId("REQUESTSIZELIMITING");
        pdb.setName("somepolicy");
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        when(storage.getPolicyDefinition(anyString())).thenReturn(pdb);
        when(securityContext.getCurrentUser()).thenReturn("admin");
        when(gatewayValidation.validate(anyObject(),PolicyType.Application)).thenReturn(new Policy("somepolicy","{}", "someorg.someapp.someversion"));
        orgFacade.createAppPolicy("someorg", "someapp", "someversion", npb);
        verify(storage).createPolicy(anyObject());
        verify(storage).createAuditEntry(anyObject());
    }

    @Test
    public void testGetAppPolicy() throws Exception {
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setApplication(ab);
        PolicyBean pb = new PolicyBean();
        pb.setType(PolicyType.Application);
        pb.setOrganizationId("someorg");
        pb.setEntityId("someapp");
        pb.setEntityVersion("someversion");
        when(securityContext.hasPermission(anyObject(), anyString())).thenReturn(true);
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        when(storage.getPolicy(anyObject(), anyString(), anyString(), anyString(), anyLong())).thenReturn(pb);
        orgFacade.getAppPolicy("someorg", "someapp", "someversion", 1000);
        verify(storage).getApplicationVersion(anyString(), anyString(), anyString());
    }

    @Test
    public void testGetAppPolicyNoPermission() throws Exception {
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setApplication(ab);
        PolicyBean pb = new PolicyBean();
        pb.setType(PolicyType.Application);
        pb.setOrganizationId("someorg");
        pb.setEntityId("someapp");
        pb.setEntityVersion("someversion");
        when(securityContext.hasPermission(anyObject(), anyString())).thenReturn(false);
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        when(storage.getPolicy(anyObject(), anyString(), anyString(), anyString(), anyLong())).thenReturn(pb);
        orgFacade.getAppPolicy("someorg", "someapp", "someversion", 1000);
        verify(storage).getApplicationVersion(anyString(), anyString(), anyString());
        assertNull(pb.getConfiguration());
    }

    @Test
    public void testListAppPolicies() throws Exception {
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setApplication(ab);
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        when(query.getPolicies(anyString(), anyString(), anyString(), anyObject())).thenReturn(null);
        orgFacade.listAppPolicies("someorg", "someapp", "someversion");
        verify(storage).getApplicationVersion("someorg", "someapp", "someversion");
        verify(query).getPolicies("someorg", "someapp", "someversion", PolicyType.Application);
    }

    @Test
    public void testCreateContract() throws Exception {

    }

    @Test
    public void testEnableOAuthForConsumer() throws Exception {

    }

    @Test
    public void testGetApplicationVersionContracts() throws Exception {
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setApplication(ab);
        when(securityContext.hasPermission(anyObject(), anyString())).thenReturn(true);
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        List<ContractSummaryBean> contractSummaryBeanList = new ArrayList<>();
        ContractSummaryBean csb = new ContractSummaryBean();
        contractSummaryBeanList.add(csb);
        when(query.getApplicationContracts(anyString(), anyString(), anyString())).thenReturn(contractSummaryBeanList);
        orgFacade.getApplicationVersionContracts("someorg", "someapp", "someversion");
        verify(query).getApplicationContracts("someorg", "someapp", "someversion");
    }

    @Test
    public void testGetApplicationVersionContractsNoPermission() throws Exception {
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setApplication(ab);
        when(securityContext.hasPermission(anyObject(), anyString())).thenReturn(false);
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        List<ContractSummaryBean> contractSummaryBeanList = new ArrayList<>();
        ContractSummaryBean csb = new ContractSummaryBean();
        contractSummaryBeanList.add(csb);
        when(query.getApplicationContracts(anyString(), anyString(), anyString())).thenReturn(contractSummaryBeanList);
        orgFacade.getApplicationVersionContracts("someorg", "someapp", "someversion");
        verify(query).getApplicationContracts("someorg", "someapp", "someversion");
        assertEquals(avb.getApikey(), null);
    }

    @Test
    public void testGetApp() throws Exception {
        ApplicationBean ab = new ApplicationBean();
        ab.setName("returnedapp");
        when(storage.getApplication(anyString(), anyString())).thenReturn(ab);
        orgFacade.getApp("someorg", "someapp");
        verify(storage).getApplication("someorg", "someapp");
    }

    @Test
    public void testGetAppNotFound() throws Exception {
        when(storage.getApplication(anyString(), anyString())).thenReturn(null);
        thrown.expect(ApplicationNotFoundException.class);
        orgFacade.getApp("someorg", "someapp");
        verify(storage).getApplication("someorg", "someapp");
    }

    @Test
    public void testGetAppActivity() throws Exception {
        orgFacade.getAppActivity("someorg", "someapp", 1, 10);
        PagingBean paging = new PagingBean();
        paging.setPage(1);
        paging.setPageSize(10);
        verify(query).auditEntity("someorg", "someapp", null, ApplicationBean.class, paging);
    }

    @Test
    public void testListAppsOrgNotFound() throws Exception {
        thrown.expect(OrganizationNotFoundException.class);
        orgFacade.listApps("someorg");
    }

    @Test
    public void testListApps() throws Exception {
        OrganizationBean ob = new OrganizationBean();
        ob.setName("someorg");
        when(storage.getOrganization(anyString())).thenReturn(ob);
        when(query.getApplicationsInOrg(anyString())).thenReturn(new ArrayList<>());
        orgFacade.listApps("someorg");
        verify(query).getApplicationsInOrg("someorg");
    }

    @Test
    public void testUpdateAppNotFound() throws Exception {
        when(storage.getApplication(anyString(), anyString())).thenReturn(null);
        thrown.expect(ApplicationNotFoundException.class);
        orgFacade.updateApp("someorg", "someapp", new UpdateApplicationBean());
    }

    @Test
    public void testUpdateApp() throws Exception {
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        when(storage.getApplication(anyString(), anyString())).thenReturn(ab);
        orgFacade.updateApp("someorg", "someapp", new UpdateApplicationBean());
        verify(storage).updateApplication(ab);
        verify(storage).createAuditEntry(anyObject());
    }

    @Test
    public void testGetAppVersionNotFound() throws Exception {
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(null);
        thrown.expect(ApplicationVersionNotFoundException.class);
        orgFacade.getAppVersion("someorg", "someapp", "someversion");
    }

    @Test
    public void testGetAppVersion() throws Exception {
        ApplicationBean ab = new ApplicationBean();
        ab.setName("someapp");
        ApplicationVersionBean avb = new ApplicationVersionBean();
        avb.setApplication(ab);
        avb.setVersion("someversion");
        when(storage.getApplicationVersion(anyString(), anyString(), anyString())).thenReturn(avb);
        orgFacade.getAppVersion("someorg", "someapp", "someversion");
        verify(storage).getApplicationVersion("someorg", "someapp", "someversion");
    }

    @Test
    public void testGetPlanPolicy() throws Exception {
        when(securityContext.hasPermission(anyObject(), anyString())).thenReturn(true);
        when(storage.getPlanVersion(anyString(), anyString(), anyString())).thenReturn(new PlanVersionBean());
        PolicyBean pb = new PolicyBean();
        pb.setType(PolicyType.Plan);
        pb.setOrganizationId("someorg");
        pb.setEntityId("someplan");
        pb.setEntityVersion("someversion");
        pb.setConfiguration("someconfiguration");
        when(storage.getPolicy(anyObject(), anyString(), anyString(), anyString(), anyLong())).thenReturn(pb);
        orgFacade.getPlanPolicy("someorg", "someplan", "someversion", 1000);
        verify(storage).getPolicy(PolicyType.Plan, "someorg", "someplan", "someversion", 1000l);
        assertNotNull(pb.getConfiguration());
    }

    @Test
    public void testGetPlanVersionNotFound() throws Exception {
        when(storage.getPlanVersion(anyString(), anyString(), anyString())).thenReturn(null);
        thrown.expect(PlanVersionNotFoundException.class);
        orgFacade.getPlanVersion("someorg", "someplan", "someversion");
        verify(storage).getPlanVersion("someorg", "someplan", "someversion");
    }

    @Test
    public void testGetPlanVersion() throws Exception {
        when(storage.getPlanVersion(anyString(), anyString(), anyString())).thenReturn(new PlanVersionBean());
        orgFacade.getPlanVersion("someorg", "someplan", "someversion");
        verify(storage).getPlanVersion("someorg", "someplan", "someversion");
    }

    @Ignore
    @Test
    public void testCreatePlanPolicy() throws Exception {
        List<PolicySummaryBean> summaryBeans = new ArrayList<>();
        GatewayValidation gValidation = new GatewayValidation();
        PolicySummaryBean policySummaryBean = new PolicySummaryBean();
        policySummaryBean.setDescription("some pol summ");
        policySummaryBean.setName("some pol name");
        policySummaryBean.setPolicyDefinitionId("somedefaultid");
        summaryBeans.add(policySummaryBean);
        PlanVersionBean planVersionBean = new PlanVersionBean();
        planVersionBean.setStatus(PlanStatus.Created);
        when(Policies.valueOf(anyString())).thenReturn(Policies.BASICAUTHENTICATION);
        when(gValidation.validateBasicAuth(anyObject())).thenReturn(new Policy());
        when(gValidation.validate(anyObject(), PolicyType.Plan)).thenReturn(new Policy());
        when(gValidation.validate(anyObject(),PolicyType.Plan)).thenReturn(new Policy());
        when(query.getMaxPolicyOrderIndex(anyString(), anyString(), anyString(), anyObject())).thenReturn(0);
        when(storage.getPlanVersion(anyString(), anyString(), anyString())).thenReturn(new PlanVersionBean());
        when(storage.getPolicyDefinition(anyString())).thenReturn(new PolicyDefinitionBean());
        when(orgFacade.listPlanPolicies("someorg", "someplan", "someverions")).thenReturn(summaryBeans);
        when(orgFacade.doCreatePolicy("someorg", "someplanx", "someversionx", new NewPolicyBean(), PolicyType.Plan)).thenReturn(new PolicyBean());
        when(orgFacade.getPlanVersion(anyString(), anyString(), anyString())).thenReturn(planVersionBean);
        NewPolicyBean newPolicyBean = new NewPolicyBean();
        newPolicyBean.setConfiguration("defaultconfig");
        newPolicyBean.setDefinitionId("defaultdefid");
        PolicyBean planPolicy = orgFacade.createPlanPolicy("someorg", "someplanx", "someverionsx", newPolicyBean);
        verify(orgFacade).doCreatePolicy("someorg","someplanx","someversionx",anyObject(),PolicyType.Plan);
    }

    @Test
    public void testCreateServicePolicy() throws Exception {

    }

    @Test
    public void testGetServiceVersion() throws Exception {

    }

    @Test
    public void testGetServicePolicy() throws Exception {

    }

    @Test
    public void testUpdateServiceVersion() throws Exception {

    }

    @Test
    public void testCreateService() throws Exception {

    }

    @Test
    public void testCreateServiceVersion() throws Exception {

    }

    @Test
    public void testGetServiceDefinition() throws Exception {

    }

    @Test
    public void testUpdateServiceDefinition() throws Exception {

    }

    @Test
    public void testListServicePolicies() throws Exception {

    }

    @Test
    public void testGetAppVersionActivity() throws Exception {

    }

    @Test
    public void testGetAppUsagePerService() throws Exception {

    }

    @Test
    public void testGetUsage() throws Exception {

    }

    @Test
    public void testGetMarketInfo() throws Exception {

    }

    @Test
    public void testGetResponseStats() throws Exception {

    }

    @Test
    public void testGetResponseStatsSummary() throws Exception {

    }

    @Test
    public void testListAppVersions() throws Exception {

    }

    @Test
    public void testGetContract() throws Exception {

    }

    @Test
    public void testDeleteAllContracts() throws Exception {

    }

    @Test
    public void testDeleteContract() throws Exception {

    }

    @Test
    public void testGetApiRegistryJSON() throws Exception {

    }

    @Test
    public void testGetApiRegistryXML() throws Exception {

    }

    @Test
    public void testUpdateAppPolicy() throws Exception {

    }

    @Test
    public void testDeleteAppPolicy() throws Exception {

    }

    @Test
    public void testReorderApplicationPolicies() throws Exception {

    }

    @Test
    public void testUpdateServiceTerms() throws Exception {

    }

    @Test
    public void testGetService() throws Exception {

    }

    @Test
    public void testGetServiceActivity() throws Exception {

    }

    @Test
    public void testListServices() throws Exception {

    }

    @Test
    public void testUpdateService() throws Exception {

    }

    @Test
    public void testGetServiceVersionEndpointInfo() throws Exception {

    }

    @Test
    public void testGetServiceVersionActivity() throws Exception {

    }

    @Test
    public void testListServiceVersions() throws Exception {

    }

    @Test
    public void testGetServiceVersionPlans() throws Exception {

    }

    @Test
    public void testUpdateServicePolicy() throws Exception {

    }

    @Test
    public void testDeleteServicePolicy() throws Exception {

    }

    @Test
    public void testDeleteServiceDefinition() throws Exception {

    }

    @Test
    public void testReorderServicePolicies() throws Exception {

    }

    @Test
    public void testGetServicePolicyChain() throws Exception {

    }

    @Test
    public void testGetServiceVersionContracts() throws Exception {

    }

    @Test
    public void testCreatePlan() throws Exception {

    }

    @Test
    public void testGetPlan() throws Exception {

    }

    @Test
    public void testGetPlanActivity() throws Exception {

    }

    @Test
    public void testListPlans() throws Exception {

    }

    @Test
    public void testUpdatePlan() throws Exception {

    }

    @Test
    public void testCreatePlanVersion() throws Exception {

    }

    @Test
    public void testListPlanPolicies() throws Exception {

    }

    @Test
    public void testGetPlanVersionActivity() throws Exception {

    }

    @Test
    public void testListPlanVersions() throws Exception {

    }

    @Test
    public void testUpdatePlanPolicy() throws Exception {

    }

    @Test
    public void testDeletePlanPolicy() throws Exception {

    }

    @Test
    public void testReorderPlanPolicies() throws Exception {

    }

    @Test
    public void testGrant() throws Exception {

    }

    @Test
    public void testRevoke() throws Exception {

    }

    @Test
    public void testRevokeAll() throws Exception {

    }

    @Test
    public void testListMembers() throws Exception {

    }

    @Test
    public void testCreateAppVersionInternal() throws Exception {

    }

    @Test
    public void testCreateContractInternal() throws Exception {

    }

    @Test
    public void testDoGetPolicy() throws Exception {

    }

    @Test
    public void testDoCreatePolicy() throws Exception {

    }

    @Test
    public void testCreateServiceVersionInternal() throws Exception {

    }

    @Test
    public void testStoreServiceDefinition() throws Exception {

    }

    @Test
    public void testGetApiRegistry() throws Exception {

    }

    @Test
    public void testCreatePlanVersionInternal() throws Exception {

    }

    @Test
    public void testSetEm() throws Exception {

    }

    @Test
    public void testAddServiceFollower() throws Exception {

    }

    @Test
    public void testRemoveServiceFollower() throws Exception {

    }

    @Test
    public void testGetServiceFollowers() throws Exception {

    }

    @Test
    public void testCreateServiceAnnouncement() throws Exception {

    }

    @Test
    public void testDeleteServiceAnnouncement() throws Exception {

    }

    @Test
    public void testGetServiceAnnouncement() throws Exception {

    }

    @Test
    public void testGetServiceAnnouncements() throws Exception {

    }

    @Test
    public void testCreateServiceSupportTicket() throws Exception {

    }

    @Test
    public void testUpdateServiceSupportTicket() throws Exception {

    }

    @Test
    public void testGetServiceSupportTicket() throws Exception {

    }

    @Test
    public void testDeleteSupportTicket() throws Exception {

    }

    @Test
    public void testListServiceSupportTickets() throws Exception {

    }

    @Test
    public void testAddServiceSupportComment() throws Exception {

    }

    @Test
    public void testUpdateServiceSupportComment() throws Exception {

    }

    @Test
    public void testDeleteServiceSupportComment() throws Exception {

    }

    @Test
    public void testGetServiceSupportComment() throws Exception {

    }

    @Test
    public void testListServiceSupportTicketComments() throws Exception {

    }

    @Test
    public void testDeleteOrganization() throws Exception {

    }

    private OrganizationBean getDefaultOrganization(){
        OrganizationBean org = new OrganizationBean();
        org.setCreatedBy("admin");
        org.setCreatedOn(new Date());
        org.setDescription("neworg");
        org.setId("neworg");
        org.setName("neworg");
        return org;
    }

    private NewOrganizationBean getDefaultNewOrganization(){
        NewOrganizationBean newOrganizationBean = new NewOrganizationBean();
        newOrganizationBean.setDescription("someorg");
        newOrganizationBean.setName("someorg");
        return newOrganizationBean;
    }

    private UpdateOrganizationBean getDefaultUpdateOrganization(){
        UpdateOrganizationBean updateOrganization = new UpdateOrganizationBean();
        updateOrganization.setDescription("updatedesc");
        return updateOrganization;
    }

    private SearchResultsBean<RoleBean> getDefaultSearchResultRoleBeans(){
        SearchResultsBean<RoleBean> roleResult = new SearchResultsBean<RoleBean>();
        List<RoleBean> roleBeans = new ArrayList<>();
        RoleBean rb = new RoleBean();
        rb.setAutoGrant(true);
        roleBeans.add(rb);
        roleResult.setBeans(roleBeans);
        return roleResult;
    }
}