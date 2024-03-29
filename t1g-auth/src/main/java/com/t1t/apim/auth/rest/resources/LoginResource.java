package com.t1t.apim.auth.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.beans.exceptions.ErrorBean;
import com.t1t.apim.beans.idm.ExternalUserBean;
import com.t1t.apim.beans.jwt.*;
import com.t1t.apim.beans.scim.ExternalUserRequest;
import com.t1t.apim.beans.summary.ServiceVersionSummaryBean;
import com.t1t.apim.exceptions.ErrorCodes;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.facades.OrganizationFacade;
import com.t1t.apim.facades.UserFacade;
import com.t1t.util.GatewayUtils;
import com.t1t.util.ServiceConventionUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by michallispashidis on 26/11/15.
 */
@Api(value = "/login", description = "Various login-credential related methods")
@Path("/login")
@ApplicationScoped
public class LoginResource {

    @Inject
    private UserFacade userFacade;
    @Inject
    private OrganizationFacade orgFacade;

    @ApiOperation(value = "Performs a search on user email towards the coupled Identity Provider.",
            notes = "This endpoint can be used to search for users - external to the system - but discoverable through a coupled Identity Provider. The user - if not know in the API Manager - will be initialized and set ready for use.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ExternalUserBean.class, message = "External and initialized user.")
    })
    @POST
    @Path("/idp/user/mail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByMail(ExternalUserRequest externalUserRequest) {
        Preconditions.checkNotNull(externalUserRequest, Messages.i18n.format("nullValue", "External user request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(externalUserRequest.getUserMail()), Messages.i18n.format("emptyValue", "User mail"));
        ExternalUserBean userByEmail = null;
        userByEmail = userFacade.getUserByEmail(externalUserRequest.getUserMail());
        return Response.ok().entity(userByEmail).build();
    }

    @ApiOperation(value = "Performs a search on user unique name towards the coupled Identity Provider.",
            notes = "This endpoint can be used to search for users - external to the system - but discoverable through a coupled Identity Provider. The user - if not know in the API Manager - will be initialized and set ready for use.")
    @ApiResponses({
            @ApiResponse(code = 200, response = ExternalUserBean.class, message = "External and initialized user.")
    })
    @POST
    @Path("/idp/user/name")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUsername(ExternalUserRequest externalUserRequest) {
        Preconditions.checkNotNull(externalUserRequest, Messages.i18n.format("nullValue", "External user request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(externalUserRequest.getUserName()), Messages.i18n.format("emptyValue", "User name"));
        ExternalUserBean userByEmail = userFacade.getUserByUsername(externalUserRequest.getUserName());
        return Response.ok().entity(userByEmail).build();
    }

    @ApiOperation(value = "Retrieve a JWT for an application",
            notes = "This endpoint can be used to to generate a JWT for an application based on it's API key")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWT.class, message = "Application JWT")
    })
    @GET
    @Path("/application/token")
    @Produces(MediaType.APPLICATION_JSON)
    public JWT getAppJWT() {
        return orgFacade.getApplicationJWT(null);
    }

    @ApiOperation(value = "Retrieve a JWT for an admin application",
            notes = "This endpoint can be used to to generate a JWT for an application based on its API key, and if the application has admin rights, allow to impersonate a user")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWT.class, message = "Application JWT")
    })
    @POST
    @Path("/application/token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JWT getAppJWTWithImpersonation(@ApiParam ServiceAccountTokenRequest request) {
        return orgFacade.getApplicationJWT(request);
    }

    @ApiOperation(value = "Retrieve Domain-correlated JWT",
            notes = "This endpoint can be used to to generate a JWT for an application version. The application version will be selected by correlating against the requested domain and the possession of a contract with the requested service version. The requesting application must also be in possession of a contract with the desired service version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWT.class, message = "Application JWT"),
            @ApiResponse(code = 400, response = ErrorBean.class, message = "Invalid request"),
            @ApiResponse(code = 404, response = ErrorBean.class, message = "Not found"),
            @ApiResponse(code = 412, response = ErrorBean.class, message = "Precondition failed"),
            @ApiResponse(code = 500, response = ErrorBean.class, message = "System error"),
    })
    @POST
    @Path("/application/token/domain")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDomainCorrelatedToken(@ApiParam DomainCorrelationRequest request) {
        Preconditions.checkNotNull(request, Messages.i18n.format(ErrorCodes.REQUEST_NULL, "Request body"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getDomain()), Messages.i18n.format(ErrorCodes.EMPTY_VALUE, "domain"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(request.getServiceId()), Messages.i18n.format(ErrorCodes.EMPTY_VALUE, "serviceId"));
        ServiceVersionSummaryBean svcSummary = ServiceConventionUtil.getServiceVersionSummaryFromUniqueName(request.getServiceId());
        return Response.ok().entity(orgFacade.getDomainCorrelatedToken(svcSummary, request.getDomain())).build();
    }

    @ApiOperation(value = "Exchange external JWT",
            notes = "Use this endpoint to exchange a JWT from an authorized IDP into a valid T1G token.")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWT.class, message = "T1G token"),
            @ApiResponse(code = 400, response = ErrorBean.class, message = "Invalid token")
    })
    @POST
    @Path("/idp/exchange")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response exchangeExternalToken(@ApiParam JWT jwt) {
        Preconditions.checkNotNull(jwt, Messages.i18n.format(ErrorCodes.REQUEST_NULL), "Request body");
        Preconditions.checkArgument(StringUtils.isNotBlank(jwt.getToken()), Messages.i18n.format(ErrorCodes.EMPTY_FIELD, "token"));
        return Response.ok().entity(userFacade.exchangeExternalToken(jwt.getToken())).build();
    }

    @ApiOperation(value = "Refresh token",
            notes = "Use this endpoint to refresh a still valid token")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWT.class, message = "T1G token"),
            @ApiResponse(code = 400, response = ErrorBean.class, message = "Invalid token")
    })
    @POST
    @Path("/token/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response refreshToken(@ApiParam JWT jwt) {
        Preconditions.checkNotNull(jwt, Messages.i18n.format(ErrorCodes.REQUEST_NULL), "Request body");
        Preconditions.checkArgument(StringUtils.isNotBlank(jwt.getToken()), Messages.i18n.format(ErrorCodes.EMPTY_FIELD, "token"));
        return Response.ok().entity(userFacade.refreshToken(jwt.getToken())).build();
    }

    @ApiOperation(value = "Refresh an existing valid JWT",
            notes = "Use this endpoint to refresh and prolong your JWT expiration time. When no expiration time is provided, default applies. If 0 is provided as expiration configuration, the JWT will be indefinitely valid.")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWT.class, message = "Refreshed JWT."),
            @ApiResponse(code = 500, response = String.class, message = "Server error while refreshing token")
    })
    @POST
    @Path("/idp/token/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Deprecated
    public Response refreshToken(JWTRefreshRequestBean jwtRefreshRequestBean) {
        //TODO - DO NOT, I REPEAT, NOT, REMOVE THIS ENDPOINT BEFORE CHECKING WITH PROJECT LEAD!!!
        Preconditions.checkNotNull(jwtRefreshRequestBean, Messages.i18n.format("nullValue", "JWT refresh request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(jwtRefreshRequestBean.getOriginalJWT()), Messages.i18n.format("emptyValue", "Original JWT"));
        return Response.ok().entity(userFacade.refreshToken(jwtRefreshRequestBean.getOriginalJWT())).build();
    }


}
