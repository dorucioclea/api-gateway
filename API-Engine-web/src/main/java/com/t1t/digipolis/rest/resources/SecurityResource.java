package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.idm.PermissionType;
import com.t1t.digipolis.apim.beans.iprestriction.BlacklistBean;
import com.t1t.digipolis.apim.beans.iprestriction.WhitelistBean;
import com.t1t.digipolis.apim.beans.policies.NewPolicyBean;
import com.t1t.digipolis.apim.beans.policies.PolicyBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ServiceVersionAvailabilityBean;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.config.Version;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.*;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.facades.SecurityFacade;
import com.t1t.digipolis.apim.facades.SystemFacade;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.rest.resources.ISecurityResource;
import com.t1t.digipolis.apim.rest.resources.ISystemResource;
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

/**
 * Implementation of the System API.
 */
@Api(value = "/security", description = "Security Endpoint. Central security settings")
@Path("/security")
@ApplicationScoped
public class SecurityResource implements ISecurityResource {
    @Inject private SecurityFacade securityFacade;
    @Inject private ISecurityContext securityContext;

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
}
