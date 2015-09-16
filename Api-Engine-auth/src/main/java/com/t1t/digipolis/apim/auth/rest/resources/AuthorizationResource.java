package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestBasicAuth;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestOAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by michallispashidis on 9/09/15.
 */
@Api(value = "/authorization", description = "The Authorization API.  This API facilitates the creation of authorizations.")
@Path("/authorization")
@ApplicationScoped
public class AuthorizationResource implements IAuthorizationResource {
    @ApiOperation(value = "Create Basic Authorization credentials for an application consumer.",
            notes = "Use this endpoint to register an application user, with basic authorization credentials.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username (from IDP) and generated BasicAuth token.")
    })
    @POST
    @Path("/basic-auth")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public AuthConsumerBean createKeyAuthConsumer(AuthConsumerRequestKeyAuthBean criteria) {
        return null;
    }

    @ApiOperation(value = "Create Key Authorization credentials (API Key) for an application consumer.",
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
    }
}
