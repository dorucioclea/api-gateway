package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestBasicAuthBean;
import com.t1t.digipolis.apim.beans.exceptions.ErrorBean;
import com.t1t.digipolis.apim.exceptions.ApplicationNotFoundException;
import com.t1t.digipolis.apim.facades.AuthorizationFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by michallispashidis on 21/09/15.
 */
@Api(value = "/authorization", description = "The Authorization API.  This API facilitates the creation of authorizations.")
@Path("/authorization")
@ApplicationScoped
public class BasicAuthorizationResource implements IBasicAuthorization {
    @Inject
    private AuthorizationFacade authorizationFacade;

    @ApiOperation(value = "Create Basic authentication token for an application consumer.",
            notes = "Use this endpoint to register an application user, with basic authorization credentials (received from 1 registered service), in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username and generated BasicAuth token."),
            @ApiResponse(code = 409, response = String.class, message = "Conflict error.")
    })
    @POST
    @Path("/key-auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response createBasicAuthConsumer(AuthConsumerRequestBasicAuthBean criteria) throws ApplicationNotFoundException {
        return null;
    }

    @ApiOperation(value = "Retrieve Basic authentication token for an application consumer.",
            notes = "Use this endpoint to get an application user basic authentication token, in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username and generated BasicAuth token."),
            @ApiResponse(code = 409, response = ErrorBean.class, message = "Conflict error.")
    })
    @GET
    @Path("/key-auth/{key}/org/{orgId}/app/{appId}/version/{version}/user/{customUser}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response getBasicAuthConsumer(String apiKey, String orgId, String appId, String version, String customId) throws ApplicationNotFoundException {
        return null;
    }

    @ApiOperation(value = "Delete Basic authentication credentials for a consumer in the context of an application version.",
            notes = "Use this endpoint to delete an application user with basic authentication token (consumer), in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The result unique username and generated KeyAuth token."),
            @ApiResponse(code = 409, response = ErrorBean.class, message = "Conflict error.")
    })
    @DELETE
    @Path("/key-auth/{key}/org/{orgId}/app/{appId}/version/{version}/user/{customUser}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response deleteBasicAuthConsumer(String apiKey, String orgId, String appId, String version, String customId) throws ApplicationNotFoundException {
        return null;
    }
}
