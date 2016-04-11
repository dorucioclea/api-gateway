package com.t1t.digipolis.apim.auth.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.authorization.ProxyAuthRequest;
import com.t1t.digipolis.apim.beans.idm.ExternalUserBean;
import com.t1t.digipolis.apim.beans.jwt.JWTRefreshRequestBean;
import com.t1t.digipolis.apim.beans.jwt.JWTRefreshResponseBean;
import com.t1t.digipolis.apim.beans.jwt.JWTRequest;
import com.t1t.digipolis.apim.beans.jwt.JWTResponse;
import com.t1t.digipolis.apim.beans.scim.ExternalUserRequest;
import com.t1t.digipolis.apim.beans.user.ClientTokeType;
import com.t1t.digipolis.apim.beans.user.SAMLLogoutRequest;
import com.t1t.digipolis.apim.beans.user.SAMLRequest;
import com.t1t.digipolis.apim.beans.user.SAMLResponseRedirect;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.OAuthException;
import com.t1t.digipolis.apim.exceptions.SAMLAuthException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.facades.OAuthFacade;
import com.t1t.digipolis.apim.facades.UserFacade;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.http.Query;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by michallispashidis on 26/11/15.
 */
@Api(value = "/login", description = "The JWT-based .")
@Path("/login")
@ApplicationScoped
public class LoginResource implements ILoginResource {
    @Inject private IStorage storage;
    @Inject IIdmStorage idmStorage;
    @Inject ISecurityContext securityContext;
    @Inject IStorageQuery query;
    @Inject private UserFacade userFacade;
    @Inject private OAuthFacade oAuthFacade;
    @Inject private AppConfig config;
    private static final Logger log = LoggerFactory.getLogger(LoginResource.class.getName());

    @ApiOperation(value = "IDP Callback URL for the Marketplace",
                       notes = "Use this endpoint if no user is logged in, and a redirect to the IDP is needed. This enpoint is generating the SAML2 SSO redirect request using OpenSAML and the provided IDP URL. The requests specifies the client token expectations, 'jwt' token supported. The clientAppName property is optional and will serve as the JWT audience claim." +
                               "When the token expiration time is set to 0, the token will be valid for all times. The optional claims map can be provided by the consuming application. The claim set can be changed upon refreshing the JWT.")
    @ApiResponses({
                          @ApiResponse(code = 200, response = String.class, message = "SAML2 authentication request"),
                          @ApiResponse(code = 500, response = String.class, message = "Server error generating the SAML2 request")
                  })
    @POST
    @Path("/idp/redirect")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String postSAML2AuthRequestUri(SAMLRequest request) {
        Preconditions.checkNotNull(request);
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getIdpUrl()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpName()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpUrl()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getClientAppRedirect()));
        Preconditions.checkArgument(request.getToken().equals(ClientTokeType.opaque) || request.getToken().equals(ClientTokeType.jwt));
        return userFacade.generateSAML2AuthRequest(request);
    }

    @ApiOperation(value = "IDP Callback URL for the Marketplace",
                  notes = "Use this endpoint if no user is logged in, and a redirect to the IDP is needed. This enpoint is generating the SAML2 SSO redirect request using OpenSAML and the provided IDP URL. The requests specifies the client token expectations, 'jwt' token supported. The clientAppName property is optional and will serve as the JWT audience claim.")
    @ApiResponses({
                          @ApiResponse(code = 200, response = String.class, message = "SAML2 authentication request"),
                          @ApiResponse(code = 500, response = String.class, message = "Server error generating the SAML2 request")
                  })
    @GET
    @Path("/idp/redirect")
    @Produces(MediaType.TEXT_PLAIN)
    public String getSAML2AuthRequestUri(@QueryParam("idp_url")String idpUrl,
                                         @QueryParam("sp_name")String spName,
                                         @Query("sp_url")String spUrl,
                                         @QueryParam("client_redirect")String clientRedirect) {
        Preconditions.checkArgument(!StringUtils.isEmpty(idpUrl));
        Preconditions.checkArgument(!StringUtils.isEmpty(spName));
        Preconditions.checkArgument(!StringUtils.isEmpty(spUrl));
        Preconditions.checkArgument(!StringUtils.isEmpty(clientRedirect));
        SAMLRequest request = new SAMLRequest();
        request.setClientAppRedirect(clientRedirect);
        request.setIdpUrl(idpUrl);
        request.setSpName(spName);
        request.setSpUrl(spUrl);
        request.setToken(ClientTokeType.jwt);
        return userFacade.generateSAML2AuthRequest(request);
    }

    @ApiOperation(value = "IDP Immediate Redirect URL for the Marketplace",
            notes = "Use this endpoint if no user is logged in, and a redirect to the IDP is needed. This enpoint is generating and performing the SAML2 SSO redirect request using OpenSAML and the provided IDP URL. The requests specifies the client token expectations, 'jwt' token supported. The clientAppName property is optional and will serve as the JWT audience claim." +
                    "When the token expiration time is set to 0, the token will be valid for all times. The optional claims map can be provided by the consuming application. The claim set can be changed upon refreshing the JWT.")
    @ApiResponses({
            @ApiResponse(code = 301, message = "SAML2 authentication request")
    })
    @POST
    @Path("/idp/redirect/proxied")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postSAML2AuthRequestRedirect(SAMLRequest request) {
        Preconditions.checkNotNull(request);
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getIdpUrl()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpName()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpUrl()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getClientAppRedirect()));
        Preconditions.checkArgument(request.getToken().equals(ClientTokeType.opaque) || request.getToken().equals(ClientTokeType.jwt));
        String idpRequest = userFacade.generateSAML2AuthRequest(request);
        URI idpRequestUri = null;
        try {
            idpRequestUri = new URI(idpRequest);
            return Response.seeOther(idpRequestUri).build();
        } catch (URISyntaxException e) {
            new SystemErrorException(e.getMessage());
        }
        throw  new SystemErrorException("Error in redirect");
    }

    @ApiOperation(value = "IDP Immediate Redirect URL for the Marketplace",
                  notes = "Use this endpoint if no user is logged in, and a redirect to the IDP is needed. This enpoint is generating and performing the SAML2 SSO redirect request using OpenSAML and the provided IDP URL. The requests specifies the client token expectations, 'jwt' token supported. The clientAppName property is optional and will serve as the JWT audience claim.")
    @ApiResponses({@ApiResponse(code = 301, message = "SAML2 authentication request")})
    @GET
    @Path("/idp/redirect/proxied")
    public Response getSAML2AuthRequestRedirect(@QueryParam("idp_url")String idpUrl,
                                                @QueryParam("sp_name")String spName,
                                                @Query("sp_url")String spUrl,
                                                @QueryParam("client_redirect")String clientRedirect) {
        Preconditions.checkArgument(!StringUtils.isEmpty(idpUrl));
        Preconditions.checkArgument(!StringUtils.isEmpty(spName));
        Preconditions.checkArgument(!StringUtils.isEmpty(spUrl));
        Preconditions.checkArgument(!StringUtils.isEmpty(clientRedirect));
        SAMLRequest request = new SAMLRequest();
        request.setClientAppRedirect(clientRedirect);
        request.setIdpUrl(idpUrl);
        request.setSpName(spName);
        request.setSpUrl(spUrl);
        request.setToken(ClientTokeType.jwt);
        String idpRequest = userFacade.generateSAML2AuthRequest(request);
        URI idpRequestUri = null;
        try {
            idpRequestUri = new URI(idpRequest);
            return Response.seeOther(idpRequestUri).build();
        } catch (URISyntaxException e) {
            new SystemErrorException(e.getMessage());
        }
        throw  new SystemErrorException("Error in redirect");
    }

    @ApiOperation(value = "Refresh an existing valid JWT. When no expiration time is provided, default applies. When no callback is provided, the result will be returned in JSON body else the callback will be called with a jwt querystring parameter.",
            notes = "Use this endpoint to refresh and prolong your JWT expiration time. If 0 is provided as expiration configruation, the JWT will be infinitly valid. The consuming application can provide at this moment optionally a custom claim map.")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWTRefreshResponseBean.class, message = "Refreshed JWT."),
            @ApiResponse(code = 500, response = String.class, message = "Server error while refreshing token")
    })
    @POST
    @Path("/idp/token/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JWTRefreshResponseBean refreshToken(JWTRefreshRequestBean jwtRefreshRequestBean) {
        Preconditions.checkNotNull(jwtRefreshRequestBean);
        Preconditions.checkArgument(!StringUtils.isEmpty(jwtRefreshRequestBean.getOriginalJWT()));
        JWTRefreshResponseBean jwtRefreshResponseBean = new JWTRefreshResponseBean();
        try {
            jwtRefreshResponseBean = userFacade.refreshToken(jwtRefreshRequestBean);
        } catch (UnsupportedEncodingException | InvalidJwtException | MalformedClaimException | JoseException |StorageException e) {
            new SystemErrorException(e);
        }
        return jwtRefreshResponseBean;
    }

    @ApiOperation(value = "The service provider for the SAML2 Authentication request",
            notes = "This endpoint should be used by an IDP who's responding with a SAML2 Authentication response. The endpoint will provide an authorization token in return, towards the configured client URL (provided with the /idp/redirect request).")
    @ApiResponses({
            @ApiResponse(code = 200, response = String.class, message = "SAML2 authentication request"),
            @ApiResponse(code = 500, response = String.class, message = "Server error generating the SAML2 request")
    })
    @POST
    @Path("/idp/callback")
    @Produces(MediaType.TEXT_PLAIN)
    public Response executeSAML2Callback(@FormParam("SAMLResponse") String samlResponse, @FormParam("RelayState") String relayState) {
        URI uri = null;
        log.info("SAML Response - pure: {}",samlResponse);
        try {
            SAMLResponseRedirect response = userFacade.processSAML2Response(samlResponse,relayState);
            //if returned null - we are operating in restricted mode - 401 should be returned
            String jwtToken = response.getToken();
            uri = new URL(response.getClientUrl() + "?jwt=" + jwtToken).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            log.error("Grant Error:{}",e.getMessage());
            throw new SAMLAuthException(e.getMessage());
        }
        if (uri != null) return Response.seeOther(uri).build();
        return Response.status(500).entity("Could not parse the initial consumer URI").build();
    }

    @ApiOperation(value = "External SAML2 validation endpoint for consumers dealing with IDP directly",
            notes = "This endpoint should be used by an application, who is know as a Service Provider for an IDP. The API Engine will validate the SAML2 response and issue a JWT.")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWTResponse.class, message = "JWT response"),
            @ApiResponse(code = 500, response = Response.class, message = "Server error validating and generating JWT")
    })
    @POST
    @Path("/idp/ext/validation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response externalSAML2Validation(JWTRequest request) {
        Preconditions.checkNotNull(request);
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSamlResponse()));
        SAMLResponseRedirect response = null;
        try {
            response = userFacade.validateExtSAML2(request.getSamlResponse());
        } catch (Exception e) {
            log.error("Grant Error:{}",e.getMessage());
            throw new SAMLAuthException(e.getMessage());
        }
        JWTResponse jwtResponse = new JWTResponse();
        String jwtToken = response.getToken();
        jwtResponse.setToken(jwtToken);
        return Response.ok().entity(jwtResponse).build();
    }

    @ApiOperation(value = "User logout",
            notes = "This endpoint performs actions upon an IDP triggered SAML2 logout request.")
    @ApiResponses({
            @ApiResponse(code = 200, response = String.class, message = "User specific logout.")
    })
    @POST
    @Path("/idp/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String logout(SAMLLogoutRequest request) {
        Preconditions.checkNotNull(request);
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getIdpUrl()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpName()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getUsername()));
        return userFacade.generateSAML2LogoutRequest(request.getIdpUrl(), request.getSpName(), request.getUsername());
    }

    @ApiOperation(value = "IDP single logout",
            notes = "This endpoint can be used by an IDP to logout a user.")
    @ApiResponses({
            @ApiResponse(code = 200, response = String.class, message = "IDP single logout.")
    })
    @POST
    @Path("/idp/slo")
    @Produces(MediaType.TEXT_PLAIN)
    public Response singleIDPLogout() {
        //don't do anything, logout triggered from client
        //TODO change to the mkt page to login
        String url = "https://google.com/";
        URI redirectURL = null;
        try {
            redirectURL = new URL(url).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (redirectURL != null) return Response.seeOther(redirectURL).build();
        return Response.ok(redirectURL).build();
    }

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
        Preconditions.checkNotNull(externalUserRequest);
        Preconditions.checkArgument(!StringUtils.isEmpty(externalUserRequest.getUserMail()));
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
        Preconditions.checkNotNull(externalUserRequest);
        Preconditions.checkArgument(!StringUtils.isEmpty(externalUserRequest.getUserName()));
        ExternalUserBean userByEmail = userFacade.getUserByUsername(externalUserRequest.getUserName());
        return Response.ok().entity(userByEmail).build();
    }

}
