package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.idm.RoleBean;
import com.t1t.digipolis.apim.beans.orgs.NewOrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.OrganizationBean;
import com.t1t.digipolis.apim.beans.orgs.UpdateOrganizationBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.exceptions.OrganizationAlreadyExistsException;
import com.t1t.digipolis.apim.exceptions.OrganizationNotFoundException;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManager;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 22/10/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(OrganizationFacade.class)
public class OrganizationFacadeTest {
    private static final Logger _LOG = LoggerFactory.getLogger(OrganizationFacadeTest.class.getName());
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock EntityManager em;
    @Mock ISecurityContext securityContext;
    @Mock IStorage storage;
    @Mock IStorageQuery query;
    @Mock IIdmStorage idmStorage;
    @Mock IApiKeyGenerator apiKeyGenerator;
    @Mock IApplicationValidator applicationValidator;
    @Mock IServiceValidator serviceValidator;
    @Mock IMetricsAccessor metrics;
    @Mock GatewayFacade gatewayFacade;
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
        when(securityContext.hasPermission(anyObject(),anyString())).thenReturn(false);
        thrown.expect(NotAuthorizedException.class);
        orgFacade.update("someorg",new UpdateOrganizationBean());
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
        orgFacade.update("someorg",getDefaultUpdateOrganization());
        verify(storage).updateOrganization(anyObject());
        verify(storage).createAuditEntry(anyObject());
    }

    @Test
    public void testActivity() throws Exception {

    }

    @Test
    public void testCreateApp() throws Exception {

    }

    @Test
    public void testCreateAppVersion() throws Exception {

    }

    @Test
    public void testUpdateAppVersionURI() throws Exception {

    }

    @Test
    public void testCreateAppPolicy() throws Exception {

    }

    @Test
    public void testGetAppPolicy() throws Exception {

    }

    @Test
    public void testListAppPolicies() throws Exception {

    }

    @Test
    public void testCreateContract() throws Exception {

    }

    @Test
    public void testEnableOAuthForConsumer() throws Exception {

    }

    @Test
    public void testGetApplicationVersionContracts() throws Exception {

    }

    @Test
    public void testGetApp() throws Exception {

    }

    @Test
    public void testGetAppActivity() throws Exception {

    }

    @Test
    public void testListApps() throws Exception {

    }

    @Test
    public void testUpdateApp() throws Exception {

    }

    @Test
    public void testGetAppVersion() throws Exception {

    }

    @Test
    public void testGetPlanPolicy() throws Exception {

    }

    @Test
    public void testGetPlanVersion() throws Exception {

    }

    @Test
    public void testCreatePlanPolicy() throws Exception {

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