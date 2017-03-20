package com.t1t.apim.facades;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.audit.AuditEntryBean;
import com.t1t.apim.beans.idm.*;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.apim.beans.summary.ServiceSummaryBean;
import com.t1t.apim.beans.user.ClientTokeType;
import com.t1t.apim.beans.user.SAMLRequest;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.exceptions.UserNotFoundException;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.opensaml.saml2.common.Extensions;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.mockito.Mockito.*;

/**
 * Created by michallispashidis on 26/11/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserFacade.class)
public class UserFacadeTest extends TestCase {
    @Rule public ExpectedException thrown = ExpectedException.none();

    @Mock private AppConfig config;
    @Mock IStorageQuery query;
    @Mock private IIdmStorage idmStorage;
    @InjectMocks private UserFacade userFacade;

    public void testURIUtilForRelayState() throws URISyntaxException {
        String uriA = "https://someurl.com/?token=my&type=nothingspecial";
        String uriB = "https://someurl.com/endpoint/test";
        URI A = new URI(uriA);
        URI B = new URI(uriB);
        assertEquals("someurl.com",A.getHost());
        assertEquals("someurl.com",B.getHost());
    }

    public void testURIUtilForQueryString() throws URISyntaxException {
        String uriA = "https://someurl.com/?token=my&type=nothingspecial";
        String uriB = "https://someurl.com/endpoint/test";
        URI A = new URI(uriA);
        URI B = new URI(uriB);
        System.out.println("Query:"+A.getQuery());
        System.out.println("Raw query:"+A.getRawQuery());
        System.out.println("Query:"+B.getQuery());
        System.out.println("Raw query:"+B.getRawQuery());
    }

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
        Map<String, String> optClaimMap = new HashMap<>();
        when(config.getJWTDefaultTokenExpInSeconds()).thenReturn(10);
        thrown.expect(IllegalArgumentException.class);//some issue bootstrapping context
        SAMLRequest samlRequest = new SAMLRequest();
        samlRequest.setClientAppRedirect(clientUrl);
        samlRequest.setIdpUrl(idpUrl);
        samlRequest.setSpName(spName);
        samlRequest.setSpUrl(spUrl);
        samlRequest.setToken(tokeType);
        samlRequest.setOptionalClaimMap(optClaimMap);
        userFacade.generateSAML2AuthRequest(samlRequest);
    }

    public void testGenerateSAML2LogoutRequest() throws Exception {
        String idpUrl = "http://google.com";
        String user = "testuser";
        String spName = "apimarket";
        thrown.expect(IllegalArgumentException.class);//issue bootstrapping opensaml context
        userFacade.generateSAML2LogoutRequest(idpUrl, spName, user);
    }

    public void testBuildExtensions() throws Exception {
        String clientUrl = "someclienturi";
        Extensions ext = userFacade.buildExtensions(clientUrl);
        assertNotNull(ext);
    }

    public void testProcessSAML2Response() throws Exception {
        //Empty test - to do
    }

    public void testUserFromSAML2BearerToken() throws Exception {
        //Empty test - to do
    }

    public void testGetDecryptedAssertion() throws Exception {
        //Empty test - to do
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