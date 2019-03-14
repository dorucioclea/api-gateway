package com.t1t.apim.auth.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.beans.apps.NewApiKeyBean;
import com.t1t.apim.beans.apps.NewOAuthCredentialsBean;
import com.t1t.apim.beans.authorization.OAuth2TokenRevokeBean;
import com.t1t.apim.beans.idm.IdpIssuerBean;
import com.t1t.apim.beans.idm.PermissionType;
import com.t1t.apim.beans.system.SystemStatusBean;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.GatewayAuthenticationException;
import com.t1t.apim.exceptions.NotAuthorizedException;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.facades.OrganizationFacade;
import com.t1t.apim.facades.SecurityFacade;
import com.t1t.apim.facades.SystemFacade;
import com.t1t.apim.security.ISecurityAppContext;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.apim.security.JWTExpTimeRequest;
import com.t1t.apim.security.JWTExpTimeResponse;
import com.t1t.apim.security.OAuthExpTimeRequest;
import com.t1t.apim.security.OAuthExpTimeResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Api(value = "/security", description = "Security Endpoint. Central security settings")
@Path("/security")
@ApplicationScoped
public class SecurityResource {
    @Inject
    private SecurityFacade securityFacade;


    @ApiOperation(value = "Get trusted IDP issuers")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = IdpIssuerBean.class, message = "Trusted issuers")
    })
    @GET
    @Path("/idp/issuers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIdpIssuers() {
        if (!securityFacade.isAdminApp()) throw ExceptionFactory.notAuthorizedException();
        return Response.ok().entity( securityFacade.getIdpIssuers()).build();
    }

    @ApiOperation(value = "Create trusted IDP issuer")
    @ApiResponses({
            @ApiResponse(code = 200, response = IdpIssuerBean.class, message = "Created")
    })
    @POST
    @Path("/idp/issuers")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createIdpIssuer(@ApiParam final IdpIssuerBean idpIssuer) {
        validateIdpIssuer(idpIssuer);
        if (!securityFacade.isAdminApp()) throw ExceptionFactory.notAuthorizedException();
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
        if (!securityFacade.isAdminApp()) throw ExceptionFactory.notAuthorizedException();
        return Response.ok().entity(securityFacade.updateIdpIssuer(idpIssuer)).build();
    }

    @ApiOperation(value = "Delete trusted IDP issuer")
    @ApiResponses({
            @ApiResponse(code = 204, response = IdpIssuerBean.class, message = "Updated")
    })
    @DELETE
    @Path("/idp/issuers/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteIdpIssuer(@ApiParam final IdpIssuerBean issuer) {
        validateIdpIssuer(issuer);
        if (!securityFacade.isAdminApp()) throw ExceptionFactory.notAuthorizedException();
        securityFacade.deleteIdpIssuer(issuer);
        return Response.noContent().build();
    }

    private void validateIdpIssuer(final IdpIssuerBean idpIssuer) {
        Preconditions.checkNotNull(idpIssuer, Messages.i18n.format(ErrorCodes.REQUEST_NULL), "Request body");
        Preconditions.checkArgument(StringUtils.isNotBlank(idpIssuer.getIssuer()), Messages.i18n.format(ErrorCodes.EMPTY_FIELD, "issuer"));
        Preconditions.checkArgument(StringUtils.isNotBlank(idpIssuer.getJwksUri()), Messages.i18n.format(ErrorCodes.EMPTY_FIELD, "jwksUri"));
    }
}