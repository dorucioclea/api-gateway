package com.t1t.digipolis.apim.jpa.roles;

import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.facades.UserFacade;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

import static org.mockito.Mockito.*;

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
}