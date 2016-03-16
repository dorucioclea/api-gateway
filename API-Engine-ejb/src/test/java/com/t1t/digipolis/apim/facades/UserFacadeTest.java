package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.user.ClientTokeType;
import com.t1t.digipolis.apim.beans.user.SAMLRequest;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.apim.security.IdentityAttributes;
import com.t1t.digipolis.util.CacheUtil;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.opensaml.DefaultBootstrap;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.ws.wssecurity.Username;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Created by michallispashidis on 26/11/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserFacade.class)
public class UserFacadeTest extends TestCase {
    private static final Logger _LOG = LoggerFactory.getLogger(UserFacadeTest.class.getName());
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    CacheUtil ehcache;
    @Mock
    AppConfig config;
    @Mock
    EntityManager em;
    @Mock
    ISecurityContext securityContext;
    @Mock
    IStorage storage;
    @Mock
    IStorageQuery query;
    @Mock
    IIdmStorage idmStorage;
    @Mock
    IApiKeyGenerator apiKeyGenerator;
    @Mock
    IApplicationValidator applicationValidator;
    @Mock
    IServiceValidator serviceValidator;
    @Mock
    IMetricsAccessor metrics;
    @Mock
    GatewayFacade gatewayFacade;
    @Mock
    IGatewayLinkFactory gatewayLinkFactory;
    @InjectMocks
    UserFacade userFacade;
    @Mock
    RoleFacade roleFacade;
    @Mock
    OrganizationFacade orgFacade;
    @Mock
    DefaultBootstrap defaultBootstrap;

    public void testGet() throws Exception {
        UserBean ub = new UserBean();
        ub.setAdmin(true);
        ub.setUsername("someuser");
        when(idmStorage.getUser(anyString())).thenReturn(ub);
        UserBean someuser = userFacade.get("someuser");
        verify(idmStorage).getUser(anyString());
        assertNotNull(someuser);
    }

    public void testGetException() throws Exception {
        when(idmStorage.getUser(anyString())).thenReturn(null);
        thrown.expect(UserNotFoundException.class);
        userFacade.get("someuser");
    }

    public void testGetStorageException() throws Exception {
        when(idmStorage.getUser(anyString())).thenThrow(new StorageException());
        thrown.expect(SystemErrorException.class);
        userFacade.get("someuser");
    }

    public void testUpdate() throws Exception {
        UserBean updatedUser = new UserBean();
        updatedUser.setUsername("someuser");
        UpdateUserBean refUser = new UpdateUserBean();
        refUser.setEmail("somemail");
        refUser.setFullName("somefullname");
        refUser.setCompany("someorg");
        refUser.setLocation("someloc");
        refUser.setWebsite("someweb");
        refUser.setBio("somebio");
        when(idmStorage.getUser(anyString())).thenReturn(updatedUser);
        doNothing().when(idmStorage).updateUser(anyObject());
        userFacade.update("someuser", refUser);
        verify(idmStorage).updateUser(anyObject());
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

    public void testUpdateStorageException() throws Exception {
        when(idmStorage.getUser(anyString())).thenThrow(new StorageException());
        thrown.expect(SystemErrorException.class);
        userFacade.update("someuser",anyObject());
    }

    public void testUpdateUserNotFound() throws Exception {
        when(idmStorage.getUser(anyString())).thenReturn(null);
        thrown.expect(UserNotFoundException.class);
        userFacade.update("someuser",anyObject());
    }

    public void testUpdateException() throws Exception {
        when(idmStorage.getUser(anyString())).thenReturn(null);
        thrown.expect(UserNotFoundException.class);
        userFacade.update("somestring", null);
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
        when(query.auditUser(anyString(), anyObject())).thenReturn(result);
        userFacade.getActivity("someuserid", 1, 1);
        verify(query).auditUser(anyString(), anyObject());
    }

    public void testGetActivityException() throws Exception {
        SearchResultsBean<AuditEntryBean> result = new SearchResultsBean<>();
        when(query.auditUser(anyString(), anyObject())).thenThrow(new StorageException());
        thrown.expect(SystemErrorException.class);
        userFacade.getActivity("someuserid", 1, 1);
    }

    public void testGenerateSAML2AuthRequest() throws Exception {
        String idpUrl = "http://google.com";
        String spUrl = "http://google.com";
        String spName = "apimarket";
        String clientUrl = "http://localhost:4000";
        ClientTokeType tokeType = ClientTokeType.jwt;
        Integer overrideExp = 5;
        Map<String, String> optClaimMap = new HashMap<>();
        when(config.getJWTDefaultTokenExpInMinutes()).thenReturn(10);
        thrown.expect(IllegalArgumentException.class);//some issue bootstrapping context
        SAMLRequest samlRequest = new SAMLRequest();
        samlRequest.setClientAppRedirect(clientUrl);
        samlRequest.setIdpUrl(idpUrl);
        samlRequest.setSpName(spName);
        samlRequest.setSpUrl(spUrl);
        samlRequest.setToken(tokeType);
        samlRequest.setOptionalClaimMap(optClaimMap);
        String retVal = userFacade.generateSAML2AuthRequest(samlRequest);
    }

    public void testGenerateSAML2LogoutRequest() throws Exception {
        String idpUrl = "http://google.com";
        String user = "testuser";
        String spName = "apimarket";
        thrown.expect(IllegalArgumentException.class);//issue bootstrapping opensaml context
        String retval = userFacade.generateSAML2LogoutRequest(idpUrl, spName, user);
    }

    public void testBuildExtensions() throws Exception {
        String clientUrl = "someclienturi";
        Extensions ext = userFacade.buildExtensions(clientUrl);
        assertNotNull(ext);
    }

    public void testProcessSAML2Response() throws Exception {

    }

    public void testUserFromSAML2BearerToken() throws Exception {

    }

    public void testGetDecryptedAssertion() throws Exception {

    }

    public void testInitNewUser()throws Exception{
        NewUserBean newUserBean = new NewUserBean();
        newUserBean.setAdmin(true);
        newUserBean.setUsername("michallis@trust1team.com");
        userFacade.initNewUser(newUserBean);
        verify(idmStorage).createUser(anyObject());
        verifyNoMoreInteractions(idmStorage);
    }
}