package com.t1t.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.beans.apps.NewApiKeyBean;
import com.t1t.apim.beans.apps.NewOAuthCredentialsBean;
import com.t1t.apim.beans.authorization.OAuth2TokenRevokeBean;
import com.t1t.apim.beans.idm.PermissionType;
import com.t1t.apim.beans.idm.IdpIssuerBean;
import com.t1t.apim.beans.system.SystemStatusBean;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.NotAuthorizedException;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.facades.OrganizationFacade;
import com.t1t.apim.facades.SecurityFacade;
import com.t1t.apim.facades.SystemFacade;
import com.t1t.apim.exceptions.GatewayAuthenticationException;
import com.t1t.apim.security.*;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

/**
 * Implementation of the System API.
 */
@Api(value = "/security", description = "Security Endpoint. Central security settings")
@Path("/security")
@ApplicationScoped
public class SecurityResource {
    @Inject
    private SecurityFacade securityFacade;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private SystemFacade systemFacade;
    @Inject
    private OrganizationFacade orgFacade;

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
        Preconditions.checkNotNull(request, Messages.i18n.format("nullValue", "OAuth expiration time request"));
        Preconditions.checkNotNull(request.getExpirationTime(), Messages.i18n.format("emptyValue", "Expiration time"));
        Preconditions.checkArgument(request.getExpirationTime() >= 0, "Expiration time must be equal to or greater than 0.");
        securityFacade.setOAuthExpTime(request.getExpirationTime());
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
        Preconditions.checkNotNull(request, Messages.i18n.format("nullValue", "JWT expiration time request"));
        Preconditions.checkNotNull(request.getExpirationTime(), Messages.i18n.format("", "Expiration time"));
        Preconditions.checkArgument(request.getExpirationTime() >= 0, "Expiration time must be equal to or greater than 0.");
        securityFacade.setJWTExpTime(request.getExpirationTime());
    }


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


    @ApiOperation(value = "Revoke Application Version Oauth2 Token",
            notes = "Deprecated, please use the DELETE method")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @POST
    @Path("/oauth2/tokens/revoke")
    @Consumes(MediaType.APPLICATION_JSON)
    public void revokeApplicationVersionOAuthToken(OAuth2TokenRevokeBean token) throws NotAuthorizedException {
        Preconditions.checkNotNull(token, Messages.i18n.format("nullValue", "Token"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(token.getOrganizationId()) && StringUtils.isNotEmpty(token.getApplicationId()) && StringUtils.isNotEmpty(token.getVersion()), Messages.i18n.format("emptyValue", "Organization ID, application ID & version"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(token.getId()) && StringUtils.isNotEmpty(token.getGatewayId()), Messages.i18n.format("emptyValue", "Token ID & gateway ID"));
        if (!securityContext.hasPermission(PermissionType.appAdmin, token.getOrganizationId())) {
            throw ExceptionFactory.notAuthorizedException();
        }
        orgFacade.revokeApplicationVersionOAuthToken(token);
    }


    @ApiOperation(value = "Revoke Oauth2 Token")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Succesful, no content")
    })
    @DELETE
    @Path("/oauth2/tokens/revoke")
    public void revokeOAuthToken(@QueryParam("token") String token) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(token), Messages.i18n.format("emptyValue", "Token"));
        securityFacade.revokeOAuthToken(token);
    }

    @ApiOperation(value = "Get trusted IDP issuers")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = IdpIssuerBean.class, message = "Trusted issuers")
    })
    @GET
    @Path("/idp/issuers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIdpIssuers() {
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        return Response.ok().entity(securityFacade.getIdpIssuers()).build();
    }

    @ApiOperation(value = "Create trusted IDP issuer")
    @ApiResponses({
            @ApiResponse(code = 200, response = IdpIssuerBean.class, message = "Created")
    })
    @POST
    @Path("/idp/issuers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createIdpIssuer(@ApiParam IdpIssuerBean idpIssuer) {
        validateIdpIssuer(idpIssuer);
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        return Response.ok().entity(securityFacade.createIdpIssuer(idpIssuer)).build();
    }

    @ApiOperation(value = "Update trusted IDP issuer")
    @ApiResponses({
            @ApiResponse(code = 200, response = IdpIssuerBean.class, message = "Updated")
    })
    @PUT
    @Path("/idp/issuers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateIdpIssuer(@ApiParam final IdpIssuerBean idpIssuer) {
        validateIdpIssuer(idpIssuer);
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        return Response.ok().entity(securityFacade.updateIdpIssuer(idpIssuer)).build();
    }

    @ApiOperation(value = "Update trusted IDP issuer")
    @ApiResponses({
            @ApiResponse(code = 204, response = IdpIssuerBean.class, message = "Updated")
    })
    @DELETE
    @Path("/idp/issuers/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteIssuer(@ApiParam final IdpIssuerBean issuer) {
        validateIdpIssuer(issuer);
        if (!securityContext.isAdmin()) throw ExceptionFactory.notAuthorizedException();
        securityFacade.deleteIdpIssuer(issuer);
        return Response.noContent().build();
    }

    private void validateIdpIssuer(final IdpIssuerBean idpIssuer) {
        Preconditions.checkNotNull(idpIssuer, Messages.i18n.format(ErrorCodes.REQUEST_NULL), "Request body");
        Preconditions.checkArgument(StringUtils.isNotBlank(idpIssuer.getIssuer()), Messages.i18n.format(ErrorCodes.EMPTY_FIELD, "issuer"));
        Preconditions.checkArgument(StringUtils.isNotBlank(idpIssuer.getJwksUri()), Messages.i18n.format(ErrorCodes.EMPTY_FIELD, "jwksUri"));
    }
}
