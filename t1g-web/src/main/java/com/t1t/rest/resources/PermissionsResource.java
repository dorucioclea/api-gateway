package com.t1t.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.beans.idm.UserPermissionsBean;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.NotAuthorizedException;
import com.t1t.apim.exceptions.UserNotFoundException;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.facades.PermissionsFacade;
import com.t1t.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api(value = "/permissions", description = "The Permissions API.")
@Path("/permissions")
@ApplicationScoped
public class PermissionsResource {

    @Inject
    IIdmStorage idmStorage;
    @Inject
    ISecurityContext securityContext;
    @Inject
    private PermissionsFacade permissionsFacade;

    /**
     * Constructor.
     */
    public PermissionsResource() {
    }

    @ApiOperation(value = "Get User's Permissions",
            notes = "This endpoint returns all of the permissions assigned to a specific user.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserPermissionsBean.class, message = "All of the user's permissions.")
    })
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserPermissionsBean getPermissionsForUser(@PathParam("userId") String userId) throws UserNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(userId), Messages.i18n.format("emptyValue", "User ID"));
        return permissionsFacade.getPermissionsForUser(userId);
    }

    @ApiOperation(value = "Get Current User's Permissions",
            notes = "This endpoint returns all of the permissions assigned to the currently authenticated user.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserPermissionsBean.class, message = "All of the user's permissions.")
    })
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserPermissionsBean getPermissionsForCurrentUser() throws UserNotFoundException {
        return permissionsFacade.getPermissionsForCurrentUser();
    }
}
