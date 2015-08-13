package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.idm.UserPermissionsBean;
import com.t1t.digipolis.apim.rest.resources.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.rest.resources.exceptions.UserNotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * The Permissions API.
 */
@Path("permissions")
public interface IPermissionsResource {

    /**
     * This endpoint returns all of the permissions assigned to a specific user.
     * @summary Get User's Permissions
     * @servicetag admin
     * @param userId The user's ID.
     * @statuscode 200 If the permissions are successfully retrieved.
     * @return All of the user's permissions.
     * @throws UserNotFoundException when a request is sent for a user who does not exist
     * @throws NotAuthorizedException when the user is not authorized to perform this action
     */
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserPermissionsBean getPermissionsForUser(@PathParam("userId") String userId)
            throws UserNotFoundException, NotAuthorizedException;

    /**
     * This endpoint returns all of the permissions assigned to the currently 
     * authenticated user.
     * @summary Get Current User's Permissions
     * @statuscode 200 If the permissions are successfully retrieved.
     * @return All of the user's permissions.
     * @throws UserNotFoundException when a request is sent for a user who does not exist
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserPermissionsBean getPermissionsForCurrentUser() throws UserNotFoundException;
    
}
