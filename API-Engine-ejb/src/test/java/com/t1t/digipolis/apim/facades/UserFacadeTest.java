package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.core.*;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.OrganizationNotFoundException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.security.ISecurityContext;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import javax.persistence.EntityManager;

/**
 * Created by michallispashidis on 26/11/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserFacade.class)
public class UserFacadeTest extends TestCase {
    private static final Logger _LOG = LoggerFactory.getLogger(UserFacadeTest.class.getName());
    @Rule public ExpectedException thrown = ExpectedException.none();
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
        //thrown.expect(StorageException.class);
        userFacade.search(new SearchCriteriaBean());

    }

    public void testGetOrganizations() throws Exception {
    }

    public void testGetApplications() throws Exception {

    }

    public void testGetServices() throws Exception {

    }

    public void testGetActivity() throws Exception {

    }

    public void testGenerateSAML2AuthRequest() throws Exception {

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