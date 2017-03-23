package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.idm.UserPermissionsBean;
import com.t1t.apim.exceptions.NotAuthorizedException;
import com.t1t.apim.exceptions.UserNotFoundException;

import javax.ws.rs.PathParam;

/**
 * The Permissions API.
 */
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
    public UserPermissionsBean getPermissionsForCurrentUser() throws UserNotFoundException;
    
}
