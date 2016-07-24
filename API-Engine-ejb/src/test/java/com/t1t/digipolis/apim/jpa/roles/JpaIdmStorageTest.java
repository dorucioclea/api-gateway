package com.t1t.digipolis.apim.jpa.roles;

import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.facades.UserFacade;
import junit.framework.TestCase;
import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.junit.Rule;
import org.junit.Test;
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

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 2/03/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(UserFacade.class)
public class JpaIdmStorageTest {
    private static final Logger _LOG = LoggerFactory.getLogger(JpaIdmStorageTest.class.getName());
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    public EntityManager em;
    @InjectMocks
    public JpaIdmStorage storage;

    @Test
    public void testCreateUser() throws Exception {
        UserBean userBean = new UserBean();
        userBean.setUsername("michallis@trust1team.com");
        storage.createUser(userBean);
        verify(em).persist(anyObject());
        verify(em).flush();
        verifyNoMoreInteractions(em);
    }

    @Test
    public void testDeleteUser() throws Exception {
        storage.deleteUser("anystring");
        verify(em).remove(anyObject());
    }

    @Test
    public void testGetUser() throws Exception {
        when(em.find(anyObject(),anyObject())).thenReturn(new UserBean());
        storage.getUser("anystring");
        verify(em).find(anyObject(),anyObject());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserBean userBean = new UserBean();
        userBean.setUsername("michallis@trust1team.com");
        storage.updateUser(userBean);
        verify(em).merge(anyObject());
        verify(em).flush();
    }

    @Test
    public void testFindUsers() throws Exception {
/*        SearchCriteriaBean scb = new SearchCriteriaBean();
        when(em.getCriteriaBuilder()).thenReturn(anyObject());
        when(em.createQuery(anyString())).thenReturn(anyObject());
        storage.findUsers(scb);*/
    }

    @Test
    public void testCreateRole() throws Exception {

    }

    @Test
    public void testUpdateRole() throws Exception {

    }

    @Test
    public void testDeleteRole() throws Exception {

    }

    @Test
    public void testGetUserByMail() throws Exception {

    }

    @Test
    public void testGetRole() throws Exception {

    }

    @Test
    public void testFindRoles() throws Exception {

    }

    @Test
    public void testCreateMembership() throws Exception {

    }

    @Test
    public void testGetMembership() throws Exception {

    }

    @Test
    public void testDeleteMembership() throws Exception {

    }

    @Test
    public void testDeleteMemberships() throws Exception {

    }

    @Test
    public void testGetUserMemberships() throws Exception {

    }

    @Test
    public void testGetUserMemberships1() throws Exception {

    }

    @Test
    public void testGetOrgMemberships() throws Exception {

    }

    @Test
    public void testGetPermissions() throws Exception {

    }

    @Test
    public void testGetAllPermissions() throws Exception {

    }

    @Test
    public void testGetAllUsers() throws Exception {

    }

    @Test
    public void testGetAdminUsers() throws Exception {

    }

    @Test
    public void testGetRoleInternal() throws Exception {

    }
}