package com.t1t.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.beans.audit.AuditEntryBean;
import com.t1t.apim.beans.dto.UserDtoBean;
import com.t1t.apim.beans.exceptions.ErrorBean;
import com.t1t.apim.beans.idm.NewUserBean;
import com.t1t.apim.beans.idm.UpdateUserBean;
import com.t1t.apim.beans.idm.UserBean;
import com.t1t.apim.beans.search.SearchCriteriaBean;
import com.t1t.apim.beans.search.SearchResultsBean;
import com.t1t.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.apim.beans.summary.ServiceSummaryBean;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.core.i18n.Messages;
import com.t1t.apim.exceptions.*;
import com.t1t.apim.exceptions.NotAuthorizedException;
import com.t1t.apim.facades.UserFacade;
import com.t1t.apim.rest.resources.IUserResource;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.util.DtoFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/users", description = "The User API.")
@Path("/users")
@ApplicationScoped
public class UserResource implements IUserResource {

    @Inject private ISecurityContext securityContext;
    @Inject private UserFacade userFacade;

    /**
     * Constructor.
     */
    public UserResource() {
    }

    @ApiOperation(value = "Get User by ID",
            notes = "Use this endpoint to get information about a specific user by the User ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserDtoBean.class, message = "Full user information.")
    })
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDtoBean get(@PathParam("userId") String userId) throws UserNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        UserDtoBean rval = DtoFactory.createUserDtoBean(userFacade.get(userId));
        if (!securityContext.isAdmin()) {
            rval.setJwtKey(null);
            rval.setJwtSecret(null);
        }
        return rval;
    }

    @ApiOperation(value = "Get Admin users",
                  notes = "Use this endpoint to get users who are granted with admin priviledges.")
    @ApiResponses({
                          @ApiResponse(code = 200,responseContainer = "List", response = UserBean.class, message = "Admin users.")
                  })
    @GET
    @Path("/admins")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdmins() throws UserNotFoundException, StorageException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        final List<UserBean> admins = userFacade.getAdmins();
        return Response.ok().entity(admins).build();
    }

    @ApiOperation(value = "Delete admin priviledges for user",
                  notes = "Use this endpoint to remove admin priviledges from user.")
    @ApiResponses({
                          @ApiResponse(code = 204, response = Response.class, message = "Admin priviledges removed.")
                  })
    @DELETE
    @Path("/admins/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAdminPriviledges(@PathParam("userId") String userId) throws UserNotFoundException, StorageException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        userFacade.deleteAdminPriviledges(userId);
        return Response.ok().status(204).build();
    }

    @ApiOperation(value = "Add admin priviledges for user",
                  notes = "Use this endpoint to add admin priviledges for user. If the user doesn't exist, user will be created.")
    @ApiResponses({
                          @ApiResponse(code = 204, response = Response.class, message = "Admin priviledges added.")
                  })
    @POST
    @Path("/admins/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAdminPriviledges(@PathParam("userId") String userId) throws UserNotFoundException, StorageException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        userFacade.addAdminPriviledges(userId);
        return Response.ok().status(204).build();
    }

    @ApiOperation(value = "Update a User by ID",
            notes = "Use this endpoint to update the information about a user.  This will fail unless the authenticated user is an admin or identical to the user being updated.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("userId") String userId, UpdateUserBean user) throws UserNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin() && !securityContext.getCurrentUser().equals(userId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        Preconditions.checkNotNull(user, Messages.i18n.format("nullValue", "Updated user"));
        Preconditions.checkArgument(user.getPic().getBytes().length <= 150_000, "Logo should not be greater than 100k");
        userFacade.update(userId, user);
    }

    @ApiOperation(value = "Search for Users",
            notes = "Use this endpoint to search for users.  The search criteria is provided in the body of the request, including filters (bool_eq, eq, neq, gt, gte, lt, lte, like), order-by, and paging information.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "The search results (a page of organizations).")
    })
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<UserBean> search(SearchCriteriaBean criteria) throws InvalidSearchCriteriaException {
        return userFacade.search(criteria);
    }

    @ApiOperation(value = "List User Organizations",
            notes = "This endpoint returns the list of organizations that the user is a member of.  The user is a member of an organization if she has at least one role for the org.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = OrganizationSummaryBean.class, message = "List of organizations.")
    })
    @GET
    @Path("/{userId}/organizations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationSummaryBean> getOrganizations(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        return userFacade.getOrganizations(userId);
    }

    @ApiOperation(value = "List User Applications",
            notes = "This endpoint returns all applications that the user has permission to edit.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ApplicationSummaryBean.class, message = "List of applications.")
    })
    @GET
    @Path("/{userId}/applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationSummaryBean> getApplications(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        return userFacade.getApplications(userId);
    }

    @ApiOperation(value = "List User Services",
            notes = "This endpoint returns all services that the user has permission to edit.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceSummaryBean.class, message = "List of services.")
    })
    @GET
    @Path("/{userId}/services")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceSummaryBean> getServices(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        return userFacade.getServices(userId);
    }

    @ApiOperation(value = "Get User Activity",
            notes = "Use this endpoint to get information about the user's audit history.  This returns audit entries corresponding to each of the actions taken by the user.  For example, when a user creates a new Organization, an audit entry is recorded and would be included in the result of this endpoint.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "List of audit entries.")
    })
    @GET
    @Path("/{userId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getActivity(@PathParam("userId") String userId, @QueryParam("page") int page, @QueryParam("count") int pageSize) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        return userFacade.getActivity(userId, page, pageSize);
    }

    @ApiOperation(value = "Create a new user (admin)",
                  notes = "Use this endpoint to create.  You must have admin privileges to create a new user.")
    @ApiResponses({
                          @ApiResponse(code = 204, message = "successful, no content"),
                          @ApiResponse(code = 409, response = ErrorBean.class, message = "Conflict error.")
                  })
    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(NewUserBean user) throws UserAlreadyExistsException, StorageException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(user, Messages.i18n.format("nullValue", "New user"));
        Preconditions.checkArgument(!StringUtils.isEmpty(user.getUsername()), Messages.i18n.format("emptyValue", "User name"));
        userFacade.initNewUser(user);
        return Response.ok().status(204).build();
    }

    @ApiOperation(value = "Delete user (admin)",
                  notes = "Use this endpoint to delete a user. When a user has still ownership on an organization an error message will be thrown. You must have admin privileges to delete an user.")
    @ApiResponses({
                          @ApiResponse(code = 204, message = "successful, no content")
                  })
    @PUT
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(String userId) throws UserAlreadyExistsException, StorageException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        userFacade.deleteUser(userId);
        return Response.ok().status(204).build();
    }
}
