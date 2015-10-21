package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.idm.NewRoleBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.idm.RoleBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.exceptions.RoleAlreadyExistsException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by michallispashidis on 21/10/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class RoleFacadeTest {
    private static final Logger _LOG = LoggerFactory.getLogger(RoleFacadeTest.class.getName());
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    EntityManager em;
    @Mock
    ISecurityContext securityContext;
    @Mock
    IIdmStorage idmStorage;
    @InjectMocks
    RoleFacade roleFacade;

    @Test
    public void sanity() throws Exception {
        assertNotNull(em);
        assertNotNull(securityContext);
        assertNotNull(idmStorage);
        assertNotNull(roleFacade);
    }

    @Test
    public void testCreate() throws Exception {
        Set<PermissionType> permissions = new HashSet<>();
        permissions.add(PermissionType.appAdmin);
        permissions.add(PermissionType.appEdit);
        permissions.add(PermissionType.appView);
        NewRoleBean newRoleBean = getDummyNewRoleBean(permissions);
        when(securityContext.getCurrentUser()).thenReturn("admin");
        when(idmStorage.getRole(anyString())).thenReturn(null);
        doNothing().when(idmStorage).createRole(anyObject());

        RoleBean roleBean = roleFacade.create(newRoleBean);
        _LOG.info("Rolebean:{}", roleBean);
        assertNotNull(roleBean);
        assertThat(roleBean.getId(), equalTo(newRoleBean.getName()));
        assertThat(roleBean.getName(), equalTo(newRoleBean.getName()));
        assertThat(roleBean.getDescription(), equalTo(newRoleBean.getDescription()));
        assertThat(roleBean.getCreatedBy(), equalTo("admin"));
        assertThat(roleBean.getAutoGrant(), equalTo(newRoleBean.getAutoGrant()));
        assertThat(roleBean.getPermissions(), hasItem(PermissionType.appAdmin));
        assertThat(roleBean.getPermissions(), hasItem(PermissionType.appEdit));
        assertThat(roleBean.getPermissions(), hasItem(PermissionType.appView));
    }

    @Test
    public void testDuplicateCreate() throws Exception {
        Set<PermissionType> permissions = new HashSet<>();
        permissions.add(PermissionType.appAdmin);
        permissions.add(PermissionType.appEdit);
        permissions.add(PermissionType.appView);
        NewRoleBean newRoleBean = getDummyNewRoleBean(permissions);
        RoleBean existingRole = new RoleBean();
        existingRole.setId(newRoleBean.getName());//to simulate same id
        when(securityContext.getCurrentUser()).thenReturn("admin");
        when(idmStorage.getRole(anyString())).thenReturn(existingRole);
        doNothing().when(idmStorage).createRole(anyObject());
        thrown.expect(RoleAlreadyExistsException.class);
        RoleBean roleBean = roleFacade.create(newRoleBean);
    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testList() throws Exception {

    }

    @Test
    public void testSearch() throws Exception {

    }

    private NewRoleBean getDummyNewRoleBean(Set<PermissionType> permissions) {
        NewRoleBean newRoleBean = new NewRoleBean();
        newRoleBean.setAutoGrant(true);
        newRoleBean.setDescription("SomeRole");
        newRoleBean.setName("RoleName");
        newRoleBean.setPermissions(permissions);
        return newRoleBean;
    }
}