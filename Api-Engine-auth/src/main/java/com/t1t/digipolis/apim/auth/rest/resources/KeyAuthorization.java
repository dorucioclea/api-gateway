package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by michallispashidis on 9/09/15.
 */
@Api(value = "/authorization", description = "The Authorization API.  This API facilitates the creation of authorizations.")
@Path("/authorization")
@ApplicationScoped
public class KeyAuthorization implements IKeyAuthorization {
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
    public AuthConsumerBean createKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria) {
        return null;
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
    public AuthConsumerBean getKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria) {
        return null;
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
    public AuthConsumerBean updateKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria) {
        return null;
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
        return null;
    }

/*    @ApiOperation(value = "Create Key Authorization credentials (API Key) for an application consumer.",
            notes = "Use this endpoint to register an application user, with key authorization credentials.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username (from IDP) and generated API Key token.")
    })
    @POST
    @Path("/key-auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public AuthConsumerBean createBasicAuthConsumer(AuthConsumerRequestBasicAuth criteria) {
        return null;
    }

    @ApiOperation(value = "Create OAuth2 credentials for an application consumer.",
            notes = "Use this endpoint to register an application user, with OAuth2 authorization credentials.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username (from IDP) and generated API Key token.")
    })
    @POST
    @Path("/oauth2")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public AuthConsumerBean createOAuthConsumer(AuthConsumerRequestOAuth criteria) {
        return null;
    }*/
}
