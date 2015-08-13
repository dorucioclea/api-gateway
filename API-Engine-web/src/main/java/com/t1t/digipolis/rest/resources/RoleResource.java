package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.beans.BeanUtils;
import com.t1t.digipolis.apim.beans.idm.NewRoleBean;
import com.t1t.digipolis.apim.beans.idm.RoleBean;
import com.t1t.digipolis.apim.beans.idm.UpdateRoleBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.rest.impl.util.ExceptionFactory;
import com.t1t.digipolis.apim.rest.impl.util.SearchCriteriaUtil;
import com.t1t.digipolis.apim.rest.resources.IRoleResource;
import com.t1t.digipolis.apim.rest.resources.exceptions.*;
import com.t1t.digipolis.apim.rest.resources.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Api(value = "/roles", description = "The Role API. Used to manage roles. Note: not used to manage users or user membership in roles. This API simply provides a way to create and manage role definitions. Typically this API is only available to system admins.")
@Path("/roles")
@ApplicationScoped
public class RoleResource implements IRoleResource {
    
    @Inject
    IIdmStorage idmStorage;
    @Inject
    ISecurityContext securityContext;
    
    /**
     * Constructor.
     */
    public RoleResource() {
    }

    @ApiOperation(value = "Create Role",
            notes = "Use this endpoint to create a new role.  A role consists of a set of permissions granted to a user when that user is given the role within the context of an organization.")
    @ApiResponses({
            @ApiResponse(code = 200, response = RoleBean.class, message = "The new created role.")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public RoleBean create(NewRoleBean bean) throws RoleAlreadyExistsException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();

        RoleBean role = new RoleBean();
        role.setAutoGrant(bean.getAutoGrant());
        role.setCreatedBy(securityContext.getCurrentUser());
        role.setCreatedOn(new Date());
        role.setDescription(bean.getDescription());
        role.setId(BeanUtils.idFromName(bean.getName()));
        role.setName(bean.getName());
        role.setPermissions(bean.getPermissions());
        try {
            if (idmStorage.getRole(role.getId()) != null) {
                throw ExceptionFactory.roleAlreadyExistsException(role.getId());
            }
            idmStorage.createRole(role);
            return role;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Get a Role by ID.",
            notes = "Use this endpoint to retrieve information about a single Role by its ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = RoleBean.class, message = "A role.")
    })
    @GET
    @Path("/{roleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public RoleBean get(@PathParam("roleId") String roleId) throws RoleNotFoundException, NotAuthorizedException {
        try {
            RoleBean role = idmStorage.getRole(roleId);
            if (role == null) {
                throw ExceptionFactory.roleNotFoundException(roleId);
            }
            return role;
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Update a Role by ID",
            notes = "Use this endpoint to update the information about an existing role.  The role is identified by its ID.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{roleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("roleId") String roleId, UpdateRoleBean bean) throws RoleNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        try {
            RoleBean role = idmStorage.getRole(roleId);
            if (role == null) {
                throw ExceptionFactory.roleNotFoundException(roleId);
            }
            if (bean.getDescription() != null) {
                role.setDescription(bean.getDescription());
            }
            if (bean.getAutoGrant() != null) {
                role.setAutoGrant(bean.getAutoGrant());
            }
            if (bean.getName() != null) {
                role.setName(bean.getName());
            }
            if (bean.getPermissions() != null) {
                role.getPermissions().clear();
                role.getPermissions().addAll(bean.getPermissions());
            }
            idmStorage.updateRole(role);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Delete a Role by ID",
            notes = "Use this endpoint to delete a role by its ID.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{roleId}")
    public void delete(@PathParam("roleId") String roleId) throws RoleNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin())
            throw ExceptionFactory.notAuthorizedException();
        RoleBean bean = get(roleId);
        try {
            idmStorage.deleteRole(bean);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }


    @ApiOperation(value = "List all Roles",
            notes = "This endpoint lists all of the roles currently defined in apiman.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = SearchCriteriaBean.class, message = "A list of roles.")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoleBean> list() throws NotAuthorizedException {
        try {
            SearchCriteriaBean criteria = new SearchCriteriaBean();
            criteria.setOrder("name", true); //$NON-NLS-1$
            return idmStorage.findRoles(criteria).getBeans();
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    @ApiOperation(value = "Search for Roles",
            notes = "This endpoint provides a way to search for roles. The search criteria is provided in the body of the request, including filters, order-by, and paging information.")
    @ApiResponses({
            @ApiResponse(code = 200,response = SearchResultsBean.class, message = "A list of roles.")
    })
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<RoleBean> search(SearchCriteriaBean criteria)
            throws InvalidSearchCriteriaException, NotAuthorizedException {
        try {
            SearchCriteriaUtil.validateSearchCriteria(criteria);
            return idmStorage.findRoles(criteria);
        } catch (StorageException e) {
            throw new SystemErrorException(e);
        }
    }

    /**
     * @return the idmStorage
     */
    public IIdmStorage getIdmStorage() {
        return idmStorage;
    }

    /**
     * @param idmStorage the idmStorage to set
     */
    public void setIdmStorage(IIdmStorage idmStorage) {
        this.idmStorage = idmStorage;
    }

    /**
     * @return the securityContext
     */
    public ISecurityContext getSecurityContext() {
        return securityContext;
    }

    /**
     * @param securityContext the securityContext to set
     */
    public void setSecurityContext(ISecurityContext securityContext) {
        this.securityContext = securityContext;
    }
}
