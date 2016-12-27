package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.idm.NewRoleBean;
import com.t1t.digipolis.apim.beans.idm.RoleBean;
import com.t1t.digipolis.apim.beans.idm.UpdateRoleBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.i18n.Messages;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.facades.RoleFacade;
import com.t1t.digipolis.apim.rest.impl.util.SearchCriteriaUtil;
import com.t1t.digipolis.apim.rest.resources.IRoleResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/roles", description = "The Role API. Used to manage roles. Note: not used to manage users or user membership in roles. This API simply provides a way to create and manage role definitions. Typically this API is only available to system admins.")
@Path("/roles")
@ApplicationScoped
public class RoleResource implements IRoleResource {
    
    @Inject
    IIdmStorage idmStorage;
    @Inject
    ISecurityContext securityContext;
    @Inject private RoleFacade roleFacade;
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
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(bean, Messages.i18n.format("nullValue", "New role"));
        return roleFacade.create(bean);
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
        Preconditions.checkArgument(!StringUtils.isEmpty(roleId), Messages.i18n.format("emptyValue", "Role ID"));
        return roleFacade.get(roleId);
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
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(roleId), Messages.i18n.format("emptyValue", "Role ID"));
        roleFacade.update(roleId, bean);
    }

    @ApiOperation(value = "Delete a Role by ID",
            notes = "Use this endpoint to remove a role by its ID.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @DELETE
    @Path("/{roleId}")
    public void delete(@PathParam("roleId") String roleId) throws RoleNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(roleId), Messages.i18n.format("emptyValue", "Role ID"));
        roleFacade.delete(roleId);
    }


    @ApiOperation(value = "List all Roles",
            notes = "This endpoint lists all of the roles currently defined in apiman.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = SearchCriteriaBean.class, message = "A list of roles.")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<RoleBean> list() throws NotAuthorizedException {
        return roleFacade.list();
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
    public SearchResultsBean<RoleBean> search(SearchCriteriaBean criteria) throws InvalidSearchCriteriaException, NotAuthorizedException {
        SearchCriteriaUtil.validateSearchCriteria(criteria);
        return roleFacade.search(criteria);
    }
}
