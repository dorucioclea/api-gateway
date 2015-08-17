package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.idm.*;
import com.t1t.digipolis.apim.beans.search.PagingBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.facades.UserFacade;
import com.t1t.digipolis.apim.rest.resources.IUserResource;
import com.t1t.digipolis.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.qualifier.APIEngineContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Api(value = "/users", description = "The User API.")
@Path("/users")
@ApplicationScoped
public class UserResource implements IUserResource {
    
    @Inject
    private
    IStorage storage;
    @Inject
    IIdmStorage idmStorage;
    @Inject
    ISecurityContext securityContext;
    @Inject
    IStorageQuery query;
    @Inject @APIEngineContext
    Logger log;
    @Inject private UserFacade userFacade;
    /**
     * Constructor.
     */
    public UserResource() {
    }

    @ApiOperation(value = "Get User by ID",
            notes = "Use this endpoint to get information about a specific user by the User ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserBean.class, message = "Full user information.")
    })
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserBean get(@PathParam("userId") String userId) throws UserNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.get(userId);
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
        if (!securityContext.isAdmin() && !securityContext.getCurrentUser().equals(userId)) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        Preconditions.checkNotNull(user);
        userFacade.update(userId,user);
    }

    @ApiOperation(value = "Search for Users",
            notes = "Use this endpoint to search for users.  The search criteria is provided in the body of the request, including filters, order-by, and paging information.")
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
            @ApiResponse(code = 200,responseContainer = "List", response = OrganizationSummaryBean.class, message = "List of organizations.")
    })
    @GET
    @Path("/{userId}/organizations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationSummaryBean> getOrganizations(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.getOrganizations(userId);
    }

    @ApiOperation(value = "List User Applications",
            notes = "This endpoint returns all applications that the user has permission to edit.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = ApplicationSummaryBean.class, message = "List of applications.")
    })
    @GET
    @Path("/{userId}/applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationSummaryBean> getApplications(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.getApplications(userId);
    }

    @ApiOperation(value = "List User Services",
            notes = "This endpoint returns all services that the user has permission to edit.")
    @ApiResponses({
            @ApiResponse(code = 200,responseContainer = "List", response = ServiceSummaryBean.class, message = "List of services.")
    })
    @GET
    @Path("/{userId}/services")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceSummaryBean> getServices(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.getServices(userId);
    }

    @ApiOperation(value = "Get User Activity",
            notes = "Use this endpoint to get information about the user's audit history.  This returns audit entries corresponding to each of the actions taken by the user.  For example, when a user creates a new Organization, an audit entry is recorded and would be included in the result of this endpoint.")
    @ApiResponses({
            @ApiResponse(code = 200,response = SearchResultsBean.class, message = "List of audit entries.")
    })
    @GET
    @Path("/{userId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getActivity(@PathParam("userId") String userId, @QueryParam("page") int page,@QueryParam("count") int pageSize) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.getActivity(userId,page,pageSize);
    }
}