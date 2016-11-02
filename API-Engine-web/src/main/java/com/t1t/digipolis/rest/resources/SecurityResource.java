package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.apps.NewApiKeyBean;
import com.t1t.digipolis.apim.beans.apps.NewOAuthCredentialsBean;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenBean;
import com.t1t.digipolis.apim.beans.authorization.OAuth2TokenRevokeBean;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.facades.SecurityFacade;
import com.t1t.digipolis.apim.facades.SystemFacade;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.rest.resources.ISecurityResource;
import com.t1t.digipolis.apim.security.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the System API.
 */
@Api(value = "/security", description = "Security Endpoint. Central security settings")
@Path("/security")
@ApplicationScoped
public class SecurityResource implements ISecurityResource {
    @Inject private SecurityFacade securityFacade;
    @Inject private ISecurityContext securityContext;
    @Inject private SystemFacade systemFacade;
    @Inject private OrganizationFacade orgFacade;

    @ApiOperation(value = "Set OAuth2 expiration time (in seconds)",
                  notes = "Use this endpoint to set the central OAuth2 token expiration time (in seconds)")
    @ApiResponses({
                          @ApiResponse(code = 204, message = "successful, no content")
                  })
    @POST
    @Path("/oauth/expiration-time")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void setOAuthExpTime(OAuthExpTimeRequest request) throws NotAuthorizedException {
        //only admin can perform this action
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(request.getExpirationTime());
        Preconditions.checkArgument(request.getExpirationTime()>0);
        securityFacade.setOAuthExpTime(request.getExpirationTime());
    }

    @ApiOperation(value = "Get OAuth2 expiration time (in seconds)",
                  notes = "Use this endpoint to get the central OAuth2 token expiration time (in seconds).")
    @ApiResponses({
                          @ApiResponse(code = 200, response = OAuthExpTimeResponse.class, message = "OAuth2 central token issuance expiration time.")
                  })
    @GET
    @Path("/oauth/expiration-time")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public OAuthExpTimeResponse getOAuthExpTime() throws NotAuthorizedException {
        //only admin can perform this action
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        return securityFacade.getOAuthExpTime();
    }

    @ApiOperation(value = "Set JWT expiration time (in seconds)",
                  notes = "Use this endpoint to set the central JWT token expiration time (in seconds)")
    @ApiResponses({
                          @ApiResponse(code = 204, message = "successful, no content")
                  })
    @POST
    @Path("/jwt/expiration-time")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void setJWTExpTime(JWTExpTimeRequest request) throws NotAuthorizedException {
        //only admin can perform this action
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkNotNull(request);
        Preconditions.checkNotNull(request.getExpirationTime());
        Preconditions.checkArgument(request.getExpirationTime()>0);
        securityFacade.setJWTExpTime(request.getExpirationTime());
    }

    @ApiOperation(value = "Get JWT expiration time (in seconds)",
                  notes = "Use this endpoint to get the central JWT token expiration time (in seconds).")
    @ApiResponses({
                          @ApiResponse(code = 200, response = OAuthExpTimeResponse.class, message = "JWT central token issuance expiration time.")
                  })
    @GET
    @Path("/jwt/expiration-time")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JWTExpTimeResponse getJWTExpTime() throws NotAuthorizedException {
        //only admin can perform this action
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        return securityFacade.getJWTExpTime();
    }

    @Override
    @ApiOperation(value = "Reissue all API keys", notes = "Use this endpoint to revoke all current API keys and issue new ones")
    @ApiResponses({
            @ApiResponse(code = 204, responseContainer = "List", response = NewApiKeyBean.class, message = "API keys reissued")
    })
    @POST
    @Path("/key-auth/reissue")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<NewApiKeyBean> reissueAllApiKeys() throws NotAuthorizedException {
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        return securityFacade.reissueAllApiKeys();
    }

    @Override
    @ApiOperation(value = "Reissue all OAuth2 credentials", notes = "Use this endpoint to revoke all current OAuth2 credentials and issue new ones")
    @ApiResponses({
            @ApiResponse(code = 204, responseContainer = "List", response = NewOAuthCredentialsBean.class, message = "OAuth2 credentials reissued")
    })
    @POST
    @Path("/oauth2/reissue")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<NewOAuthCredentialsBean> reissueAllOAuth2Credentials() throws NotAuthorizedException {
        if (!securityContext.isAdmin()) {
            throw ExceptionFactory.notAuthorizedException();
        }
        return securityFacade.reissueAllOAuthCredentials();
    }

    @ApiOperation(value = "Get System Cluster Status",
                  notes = "This endpoint simply returns the status of the api engine system. This is a useful endpoint to use when testing a client's connection to the API Manager REST services.")
    @ApiResponses({
                          @ApiResponse(code = 200, response = SystemStatusBean.class, message = "System status information")
                  })
    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public SystemStatusBean getAdminStatus() throws GatewayAuthenticationException, StorageException {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        return systemFacade.getAdminStatus();
    }

    @Override
    @ApiOperation(value = "Revoke Application Version Oauth2 Token",
                  notes = "Deprecated, please use the DELETE method")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @POST
    @Path("/oauth2/tokens/revoke")
    @Consumes(MediaType.APPLICATION_JSON)
    public void revokeApplicationVersionOAuthToken(OAuth2TokenRevokeBean token) throws NotAuthorizedException {
        Preconditions.checkNotNull(token);
        Preconditions.checkArgument(StringUtils.isNotEmpty(token.getOrganizationId()) && StringUtils.isNotEmpty(token.getApplicationId()) && StringUtils.isNotEmpty(token.getVersion()));
        Preconditions.checkArgument(StringUtils.isNotEmpty(token.getId()) && StringUtils.isNotEmpty(token.getGatewayId()));
        if (!securityContext.hasPermission(PermissionType.appAdmin, token.getOrganizationId())) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.revokeApplicationVersionOAuthToken(token);
    }

    @Override
    @ApiOperation(value = "Revoke Oauth2 Token")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @DELETE
    @Path("/oauth2/tokens/revoke")
    public void revokeOAuthToken(@QueryParam("token") String token) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(token));
        securityFacade.revokeOAuthToken(token);
    }
}
