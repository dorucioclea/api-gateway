package com.t1t.digipolis.apim.auth.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.authorization.*;
import com.t1t.digipolis.apim.exceptions.OAuthException;
import com.t1t.digipolis.apim.facades.AuthorizationFacade;
import com.t1t.digipolis.apim.facades.OAuthFacade;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponse;
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

/**
 * Created by michallispashidis on 26/09/15.
 */
@Api(value = "/oauth", description = "OAuth 2 API Engine resource.")
@Path("/oauth")
@ApplicationScoped
public class OAuthResource implements IOAuth2Authorization {
    @Inject private AuthorizationFacade authorizationFacade;
    @Inject private OAuthFacade oAuthFacade;

    @ApiOperation(value = "Enable an application consumer for OAuth2 in the context of the application.",
            notes = "The client application is identified with a client_id and client_password. Both are needed to provide the application name and redirect URL in order to register a consumer for OAuth2.")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongPluginOAuthConsumerResponse.class, message = "The result unique username and generated KeyAuth token."),
            @ApiResponse(code = 409, response = String.class, message = "Conflict error.")
    })
    @POST
    @Path("/consumer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public KongPluginOAuthConsumerResponse enableOAuthForConsumer(OAuthConsumerRequestBean request) throws OAuthException {
        Preconditions.checkNotNull(request);
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getAppOAuthId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getAppOAuthSecret()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getUniqueUserName()));
        return oAuthFacade.enableOAuthForConsumer(request);
    }

    @ApiOperation(value = "Retrieve Application OAuth2 information for targeted service.",
            notes = "Retrieve the Application OAuth2 information in order to inform the user through a consent page for a specific service.")
    @ApiResponses({
            @ApiResponse(code = 200, response = OAuthApplicationResponse.class, message = "The result unique username and generated KeyAuth token."),
            @ApiResponse(code = 409, response = String.class, message = "Conflict error.")
    })
    @GET
    @Path("/application/{clientId}/target/organization/{orgId}/service/{serviceId}/version/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public OAuthApplicationResponse getApplicationInfo(@PathParam("clientId")String oauthClientId, @PathParam("orgId")String orgId, @PathParam("serviceId")String serviceId, @PathParam("version")String version) throws OAuthException {
        Preconditions.checkArgument(!StringUtils.isEmpty(oauthClientId));
        Preconditions.checkArgument(!StringUtils.isEmpty(orgId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return oAuthFacade.getApplicationOAuthInformation(oauthClientId, orgId, serviceId, version);
    }

    @ApiOperation(value = "Utility endpoint to composes a redirect request for user authorization.",
            notes = "Returns a redirect URI that will forward the user to an authorization page (Authorization/Implicit Grant). The response type can be - code - for Authorization Code Grant; or - token - for Implicit Grant ")
    @ApiResponses({
            @ApiResponse(code = 200, response = String.class, message = "The authorization redirect URI."),
            @ApiResponse(code = 409, response = String.class, message = "Conflict error.")
    })
    @POST
    @Path("/redirect/{responseType}/user/{userId}/application/{clientId}/target/organization/{orgId}/service/{serviceId}/version/{version}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Override
    public String getAuthorizationRedirect(@PathParam("responseType")OAuthResponseType responseType,@PathParam("userId")String authenticatedUserId ,@PathParam("clientId")String oauthClientId, @PathParam("orgId")String orgId, @PathParam("serviceId")String serviceId, @PathParam("version")String version, OAuthServiceScopeRequest requestScopes) throws OAuthException{
        Preconditions.checkArgument(!StringUtils.isEmpty(oauthClientId));
        Preconditions.checkArgument(!StringUtils.isEmpty(orgId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        Preconditions.checkNotNull(responseType);
        Preconditions.checkNotNull(requestScopes);
        Preconditions.checkNotNull(requestScopes.getScopes());
        Preconditions.checkArgument(requestScopes.getScopes().size()>0);
        return oAuthFacade.getAuthorizationRedirect(responseType, authenticatedUserId ,oauthClientId, orgId, serviceId, version, requestScopes.getScopes());
    }

    @ApiOperation(value = "Information endpoint to retrieve service version scopes.",
            notes = "Returns a list of string values representing available service versions copes")
    @ApiResponses({
            @ApiResponse(code = 200,response = OAuthServiceScopeResponse.class, message = "list of service version scopes."),
            @ApiResponse(code = 409, response = String.class, message = "Conflict error.")
    })
    @GET
    @Path("/application/{clientId}/target/organization/{orgId}/service/{serviceId}/version/{version}/scopes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public OAuthServiceScopeResponse getServiceVersionScopes(@PathParam("clientId")String oauthClientId, @PathParam("orgId")String orgId, @PathParam("serviceId")String serviceId, @PathParam("version")String version){
        Preconditions.checkArgument(!StringUtils.isEmpty(oauthClientId));
        Preconditions.checkArgument(!StringUtils.isEmpty(orgId));
        Preconditions.checkArgument(!StringUtils.isEmpty(serviceId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        return oAuthFacade.getServiceVersionScopes(oauthClientId, orgId, serviceId, version);
    }

}