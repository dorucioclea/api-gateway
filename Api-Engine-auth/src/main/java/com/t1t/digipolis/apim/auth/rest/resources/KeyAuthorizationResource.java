package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
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
 * Created by michallispashidis on 9/09/15.
 */
@Api(value = "/authorization", description = "The Authorization API.  This API facilitates the creation of authorizations.")
@Path("/authorization")
@ApplicationScoped
public class KeyAuthorizationResource implements IKeyAuthorization {
    @Inject
    private AuthorizationFacade authorizationFacade;

    @ApiOperation(value = "Create Key Authorization credentials for an application consumer.",
            notes = "Use this endpoint to register an application user, with key authorization credentials, in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username and generated KeyAuth token.")
    })
    @POST
    @Path("/key-auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response createKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @ApiOperation(value = "Retrieve Key Authorization credentials for an application consumer.",
            notes = "Use this endpoint to get an application user credentials, in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username and generated KeyAuth token.")
    })
    @GET
    @Path("/key-auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response getKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @ApiOperation(value = "Update Key Authorization credentials for an application consumer.",
            notes = "Use this endpoint to update an application user credentials, in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The updated result unique username and generated KeyAuth token.")
    })
    @PUT
    @Path("/key-auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response updateKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @ApiOperation(value = "Delete authorization for a consumer in the context of an application version.",
            notes = "Use this endpoint to delete an application user (consumer), in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username and generated KeyAuth token."),
            @ApiResponse(code = 401, message = "Unauthorized.")
    })
    @DELETE
    @Path("/key-auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response deleteKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
