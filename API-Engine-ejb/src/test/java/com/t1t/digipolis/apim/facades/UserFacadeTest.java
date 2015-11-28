package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.idm.PermissionBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.idm.RoleMembershipBean;
import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.user.ClientTokeType;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.OrganizationNotFoundException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.util.CacheUtil;
import junit.framework.TestCase;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.opensaml.DefaultBootstrap;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import javax.persistence.EntityManager;
import java.util.*;

/**
 * Created by michallispashidis on 26/11/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserFacade.class)
public class UserFacadeTest extends TestCase {
    private static final Logger _LOG = LoggerFactory.getLogger(UserFacadeTest.class.getName());
    @Rule public ExpectedException thrown = ExpectedException.none();
    @Mock CacheUtil ehcache;
    @Mock AppConfig config;
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
    @InjectMocks UserFacade userFacade;
    @Mock RoleFacade roleFacade;
    @Mock OrganizationFacade orgFacade;

    public void testGet() throws Exception {
        UserBean ub = new UserBean();
        ub.setAdmin(true);
        ub.setUsername("someuser");
        when(idmStorage.getUser(anyString())).thenReturn(ub);
        UserBean someuser = userFacade.get("someuser");
        verify(idmStorage).getUser(anyString());
        assertNotNull(someuser);
    }

    public void testUpdate() throws Exception {
        UserBean ub = new UserBean();
        ub.setAdmin(true);
        ub.setUsername("someuser");
        ub.setEmail("somemail");
        ub.setBase64pic("somebase64pic");
        ub.setFullName("somefullname");
        ub.setCompany("someorg");
        ub.setLocation("someloc");
        ub.setWebsite("someweb");
        ub.setBio("somebio");
        when(idmStorage.getUser(anyString())).thenReturn(ub);
        doNothing().when(idmStorage).updateUser(anyObject());
        UserBean someuser = userFacade.get("someuser");
        verify(idmStorage).getUser(anyString());
        assertNotNull(someuser);
    }

    public void testUpdateNullContent() throws Exception {
        UserBean ub = new UserBean();
        ub.setAdmin(true);
        ub.setUsername("someuser");
        ub.setEmail(null);
        ub.setBase64pic(null);
        ub.setFullName(null);
        ub.setCompany(null);
        ub.setLocation(null);
        ub.setWebsite(null);
        ub.setBio(null);
        when(idmStorage.getUser(anyString())).thenReturn(ub);
        doNothing().when(idmStorage).updateUser(anyObject());
        UserBean someuser = userFacade.get("someuser");
        verify(idmStorage).getUser(anyString());
        assertNotNull(someuser);
    }

    public void testUpdateException() throws Exception {
        when(idmStorage.getUser(anyString())).thenReturn(null);
        thrown.expect(UserNotFoundException.class);
        userFacade.update("somestring",null);
    }

    public void testSearch() throws Exception {
        when(idmStorage.findUsers(anyObject())).thenReturn(new SearchResultsBean<UserBean>());
        userFacade.search(new SearchCriteriaBean());
        verify(idmStorage).findUsers(anyObject());
    }

    public void testSearchException() throws Exception {
        when(idmStorage.findUsers(anyObject())).thenThrow(new StorageException());
        thrown.expect(SystemErrorException.class);
        userFacade.search(new SearchCriteriaBean());
    }

    public void testGetOrganizations() throws Exception {
        Set<RoleMembershipBean> memberships = new HashSet<>();
        RoleMembershipBean roleMembershipBean = new RoleMembershipBean();
        roleMembershipBean.setOrganizationId("someorg");
        roleMembershipBean.setRoleId("somerole");
        roleMembershipBean.setUserId("someuser");
        when(idmStorage.getUserMemberships(anyString())).thenReturn(memberships);
        when(query.getOrgs(anyObject())).thenReturn(new ArrayList<OrganizationSummaryBean>());
        userFacade.getOrganizations("someorg");
        verify(query).getOrgs(anyObject());
    }

    public void testGetApplications() throws Exception {
        Set<PermissionBean> permissions = new HashSet<>();
        PermissionBean perm = new PermissionBean();
        perm.setName(PermissionType.appView);
        when(idmStorage.getPermissions(anyString())).thenReturn(permissions);
        when(query.getApplicationsInOrg(anyObject())).thenReturn(new ArrayList<ApplicationSummaryBean>());
        userFacade.getApplications("someapp");
        verify(query).getApplicationsInOrgs(anyObject());
    }

    public void testGetServices() throws Exception {
        Set<PermissionBean> permissions = new HashSet<>();
        PermissionBean perm = new PermissionBean();
        perm.setName(PermissionType.svcView);
        when(idmStorage.getPermissions(anyString())).thenReturn(permissions);
        when(query.getServicesInOrgs(anyObject())).thenReturn(new ArrayList<ServiceSummaryBean>());
        userFacade.getServices("somesrvc");
        verify(query).getServicesInOrgs(anyObject());
    }

    public void testGetServicesException() throws Exception {
        Set<PermissionBean> permissions = new HashSet<>();
        PermissionBean perm = new PermissionBean();
        perm.setName(PermissionType.svcView);
        when(idmStorage.getPermissions(anyString())).thenThrow(new StorageException());
        thrown.expect(SystemErrorException.class);
        userFacade.getServices("somesrvc");
    }

    public void testGetActivity() throws Exception {
        SearchResultsBean<AuditEntryBean> result = new SearchResultsBean<>();
        when(query.auditUser(anyString(),anyObject())).thenReturn(result);
        userFacade.getActivity("someuserid",1,1);
        verify(query).auditUser(anyString(),anyObject());
    }

    public void testGetActivityException() throws Exception {
        SearchResultsBean<AuditEntryBean> result = new SearchResultsBean<>();
        when(query.auditUser(anyString(),anyObject())).thenThrow(new StorageException());
        thrown.expect(SystemErrorException.class);
        userFacade.getActivity("someuserid",1,1);
    }

    public void testGenerateSAML2AuthRequest() throws Exception {
        String idpUrl = "http://google.com";
        String spUrl = "http://google.com";
        String spName = "apimarket";
        String clientUrl = "http://localhost:4000";
        ClientTokeType tokeType = ClientTokeType.jwt;
        Integer overrideExp = 5;
        Map<String,String> optClaimMap = new HashMap<>();
        String retVal = userFacade.generateSAML2AuthRequest(idpUrl,spUrl,spName,clientUrl,tokeType,overrideExp,optClaimMap);
        when(config.getJWTDefaultTokenExpInMinutes()).thenReturn(10);
        doNothing().when(ehcache).getClientAppCache().put(anyObject());
        doNothing().when(userFacade).utilPrintCache();

    }

    public void testGenerateSAML2LogoutRequest() throws Exception {

    }

    public void testBuildExtensions() throws Exception {

    }

    public void testProcessSAML2Response() throws Exception {

    }

    public void testUserFromSAML2BearerToken() throws Exception {

    }

    public void testGetDecryptedAssertion() throws Exception {

    }
}