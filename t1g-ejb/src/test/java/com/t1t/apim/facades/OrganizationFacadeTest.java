package com.t1t.apim.facades;

import com.t1t.apim.beans.apps.*;
import com.t1t.apim.beans.audit.AuditEntryBean;
import com.t1t.apim.beans.audit.data.EntityUpdatedData;
import com.t1t.apim.beans.idm.RoleBean;
import com.t1t.apim.beans.orgs.NewOrganizationBean;
import com.t1t.apim.beans.orgs.OrganizationBean;
import com.t1t.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.apim.beans.plans.PlanStatus;
import com.t1t.apim.beans.plans.PlanVersionBean;
import com.t1t.apim.beans.policies.*;
import com.t1t.apim.beans.search.PagingBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.summary.ContractSummaryBean;
import com.t1t.apim.beans.summary.PolicySummaryBean;
import com.t1t.apim.core.*;
import com.t1t.apim.core.metrics.MetricsService;
import com.t1t.apim.exceptions.*;
import com.t1t.apim.facades.audit.AuditUtils;
import com.t1t.apim.gateway.IGatewayLinkFactory;
import com.t1t.apim.gateway.dto.Policy;
import com.t1t.apim.gateway.rest.GatewayValidation;
import com.t1t.apim.security.ISecurityAppContext;
import com.t1t.apim.security.ISecurityContext;
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
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock private EntityManager em;
    @Mock private ISecurityContext securityContext;
    @Mock private ISecurityAppContext appContext;
    @Mock private IStorage storage;
    @Mock private IStorageQuery query;
    @Mock private IIdmStorage idmStorage;
    @Mock private IApiKeyGenerator apiKeyGenerator;
    @Mock private IApplicationValidator applicationValidator;
    @Mock private IServiceValidator serviceValidator;
    @Mock private MetricsService metrics;
    @Mock private GatewayFacade gatewayFacade;
    @Mock private GatewayValidation gatewayValidation;
    @Mock private IGatewayLinkFactory gatewayLinkFactory;
    @Mock private UserFacade userFacade;
    @Mock private RoleFacade roleFacade;
    @InjectMocks private OrganizationFacade orgFacade;

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
        orgFacade.create(getDefaultNewOrganization());
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
        when(gatewayValidation.validate(anyObject(), PolicyType.Application)).thenReturn(new Policy("somepolicy","{}", "someorg.someapp.someversion"));
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
        //Empty - to do
    }

    @Test
    public void testEnableOAuthForConsumer() throws Exception {
        //Empty - to do
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
        orgFacade.createPlanPolicy("someorg", "someplanx", "someverionsx", newPolicyBean);
        verify(orgFacade).doCreatePolicy("someorg","someplanx","someversionx",anyObject(),PolicyType.Plan);
    }

    @Test
    public void testCreateServicePolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceVersion() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServicePolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdateServiceVersion() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreateService() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreateServiceVersion() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceDefinition() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdateServiceDefinition() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListServicePolicies() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetAppVersionActivity() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetAppUsagePerService() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetUsage() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetMarketInfo() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetResponseStats() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetResponseStatsSummary() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListAppVersions() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetContract() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteAllContracts() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteContract() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetApiRegistryJSON() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetApiRegistryXML() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdateAppPolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteAppPolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testReorderApplicationPolicies() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdateServiceTerms() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetService() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceActivity() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListServices() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdateService() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceVersionEndpointInfo() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceVersionActivity() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListServiceVersions() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceVersionPlans() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdateServicePolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteServicePolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteServiceDefinition() throws Exception {
        //Empty - to do
    }

    @Test
    public void testReorderServicePolicies() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServicePolicyChain() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceVersionContracts() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreatePlan() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetPlan() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetPlanActivity() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListPlans() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdatePlan() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreatePlanVersion() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListPlanPolicies() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetPlanVersionActivity() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListPlanVersions() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdatePlanPolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeletePlanPolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testReorderPlanPolicies() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGrant() throws Exception {
        //Empty - to do
    }

    @Test
    public void testRevoke() throws Exception {
        //Empty - to do
    }

    @Test
    public void testRevokeAll() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListMembers() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreateAppVersionInternal() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreateContractInternal() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDoGetPolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDoCreatePolicy() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreateServiceVersionInternal() throws Exception {
        //Empty - to do
    }

    @Test
    public void testStoreServiceDefinition() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetApiRegistry() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreatePlanVersionInternal() throws Exception {
        //Empty - to do
    }

    @Test
    public void testSetEm() throws Exception {
        //Empty - to do
    }

    @Test
    public void testAddServiceFollower() throws Exception {
        //Empty - to do
    }

    @Test
    public void testRemoveServiceFollower() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceFollowers() throws Exception {

    }

    @Test
    public void testCreateServiceAnnouncement() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteServiceAnnouncement() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceAnnouncement() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceAnnouncements() throws Exception {
        //Empty - to do
    }

    @Test
    public void testCreateServiceSupportTicket() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdateServiceSupportTicket() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceSupportTicket() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteSupportTicket() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListServiceSupportTickets() throws Exception {
        //Empty - to do
    }

    @Test
    public void testAddServiceSupportComment() throws Exception {
        //Empty - to do
    }

    @Test
    public void testUpdateServiceSupportComment() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteServiceSupportComment() throws Exception {
        //Empty - to do
    }

    @Test
    public void testGetServiceSupportComment() throws Exception {
        //Empty - to do
    }

    @Test
    public void testListServiceSupportTicketComments() throws Exception {
        //Empty - to do
    }

    @Test
    public void testDeleteOrganization() throws Exception {
        //Empty - to do
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