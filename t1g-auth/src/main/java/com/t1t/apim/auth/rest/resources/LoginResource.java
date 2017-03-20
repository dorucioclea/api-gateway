package com.t1t.apim.auth.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.cache.WebClientCacheBean;
import com.t1t.apim.beans.idm.ExternalUserBean;
import com.t1t.apim.beans.jwt.JWTRefreshRequestBean;
import com.t1t.apim.beans.jwt.JWTRefreshResponseBean;
import com.t1t.apim.beans.jwt.JWTRequest;
import com.t1t.apim.beans.jwt.JWTResponse;
import com.t1t.apim.beans.scim.ExternalUserRequest;
import com.t1t.apim.beans.user.ClientTokeType;
import com.t1t.apim.beans.user.SAMLLogoutRequest;
import com.t1t.apim.beans.user.SAMLRequest;
import com.t1t.apim.beans.user.SAMLResponseRedirect;
import com.t1t.apim.core.IIdmStorage;
import com.t1t.apim.core.IStorage;
import com.t1t.apim.core.IStorageQuery;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.core.i18n.Messages;
import com.t1t.apim.exceptions.AbstractRestException;
import com.t1t.apim.exceptions.SAMLAuthException;
import com.t1t.apim.exceptions.SystemErrorException;
import com.t1t.apim.facades.OAuthFacade;
import com.t1t.apim.facades.OrganizationFacade;
import com.t1t.apim.facades.UserFacade;
import com.t1t.apim.security.ISecurityContext;
import com.t1t.util.CacheUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Created by michallispashidis on 26/11/15.
 */
@Api(value = "/login", description = "The JWT-based .")
@Path("/login")
@ApplicationScoped
public class LoginResource implements ILoginResource {

    private static final Logger log = LoggerFactory.getLogger(LoginResource.class.getName());

    @Inject private UserFacade userFacade;
    @Inject private CacheUtil cacheUtil;
    @Inject private OrganizationFacade orgFacade;

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
        Preconditions.checkNotNull(request, Messages.i18n.format("nullValue", "Request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getIdpUrl()), Messages.i18n.format("emptyValue", "IDP URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpName()), Messages.i18n.format("emptyValue", "Service provider name"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpUrl()), Messages.i18n.format("emptyValue", "Service provider URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getClientAppRedirect()), Messages.i18n.format("emptyValue", "Client redirect"));
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
                                         @QueryParam("sp_url") String spUrl,
                                         @QueryParam("client_redirect")String clientRedirect) {
        Preconditions.checkArgument(!StringUtils.isEmpty(idpUrl), Messages.i18n.format("emptyValue", "IDP URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(spName), Messages.i18n.format("emptyValue", "Service provider name"));
        Preconditions.checkArgument(!StringUtils.isEmpty(spUrl), Messages.i18n.format("emptyValue", "Service provider URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(clientRedirect), Messages.i18n.format("emptyValue", "Client redirect"));
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
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getIdpUrl()), Messages.i18n.format("emptyValue", "IDP URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpName()), Messages.i18n.format("emptyValue", "Service provider name"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpUrl()), Messages.i18n.format("emptyValue", "Service provider URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getClientAppRedirect()), Messages.i18n.format("emptyValue", "Client redirect"));
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
                                                @QueryParam("sp_url") String spUrl,
                                                @QueryParam("client_redirect")String clientRedirect) {
        Preconditions.checkArgument(!StringUtils.isEmpty(idpUrl), Messages.i18n.format("emptyValue", "IDP URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(spName), Messages.i18n.format("emptyValue", "Service provide name"));
        Preconditions.checkArgument(!StringUtils.isEmpty(spUrl), Messages.i18n.format("emptyValue", "Service provider URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(clientRedirect), Messages.i18n.format("emptyValue", "Client redirect"));
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

    @ApiOperation(value = "Refresh an existing valid JWT",
            notes = "Use this endpoint to refresh and prolong your JWT expiration time. When no expiration time is provided, default applies. If 0 is provided as expiration configuration, the JWT will be indefinitely valid.")
    @ApiResponses({
            @ApiResponse(code = 200, response = JWTRefreshResponseBean.class, message = "Refreshed JWT."),
            @ApiResponse(code = 500, response = String.class, message = "Server error while refreshing token")
    })
    @POST
    @Path("/idp/token/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public JWTRefreshResponseBean refreshToken(JWTRefreshRequestBean jwtRefreshRequestBean) {
        Preconditions.checkNotNull(jwtRefreshRequestBean, Messages.i18n.format("nullValue", "JWT refresh request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(jwtRefreshRequestBean.getOriginalJWT()), Messages.i18n.format("emptyValue", "Original JWT"));
        return userFacade.refreshToken(jwtRefreshRequestBean);
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
        return samlCallback(samlResponse, relayState);
    }

    @ApiOperation(value = "The service provider for the SAML2 Authentication request (external markatplace).",
                  notes = "This endpoint should be used by an IDP who's responding with a SAML2 Authentication response (external marketplace). The endpoint will provide an authorization token in return, towards the configured client URL (provided with the /idp/redirect request).")
    @ApiResponses({
                          @ApiResponse(code = 200, response = String.class, message = "SAML2 authentication request"),
                          @ApiResponse(code = 500, response = String.class, message = "Server error generating the SAML2 request")
                  })
    @POST
    @Path("/idp/callback/astad")
    @Produces(MediaType.TEXT_PLAIN)
    public Response executeSAML2CallbackAStad(@FormParam("SAMLResponse") String samlResponse, @FormParam("RelayState") String relayState) {
        return samlCallback(samlResponse, relayState);
    }

    /**
     * Callback endpoint for IDP. The IDP can have different Service providers, returning on a different callback.
     * This method resolves the callbacks SP independantly.
     *
     * @param samlResponse
     * @param relayState
     * @return
     */
    private Response samlCallback(String samlResponse,String relayState){
        URI uri = null;
        log.debug("SAML Response - pure: {}",samlResponse);
        try {
            SAMLResponseRedirect response = userFacade.processSAML2Response(samlResponse,relayState);
            String jwtToken = response.getToken();
            if (!response.getClientUrl().startsWith("http")) {
                response.setClientUrl("https://" + response.getClientUrl());
            }
            URI clientUrl = new URI(response.getClientUrl());
            if(clientUrl.getQuery()!=null){
                uri = new URL(response.getClientUrl() + "&jwt=" + jwtToken).toURI();
            }else{
                uri = new URL(response.getClientUrl() + "?jwt=" + jwtToken).toURI();
            }
        } catch (URISyntaxException | StorageException | MalformedURLException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            //Catch the exception check if there's an underlying AbstractRestException and create an error message for the redirect
            if (e.getCause() != null && AbstractRestException.class.isAssignableFrom(e.getCause().getClass()) ) {
                AbstractRestException ex = (AbstractRestException) e.getCause();
                try {
                    WebClientCacheBean webCache = cacheUtil.getWebCacheBean(URLEncoder.encode(relayState, "UTF-8"));
                    StringBuilder clientURLString = new StringBuilder(webCache == null ? new URI("https://" + URLEncoder.encode(relayState, "UTF-8")).toString() : webCache.getClientAppRedirect());
                    URI clientURL = new URI(clientURLString.toString());
                    clientURLString
                            .append(clientURL.getQuery() != null ? "&" : "?")
                            .append("errorcode=").append(ex.getErrorCode())
                            .append("&errormessage=")
                            .append(URLEncoder.encode(ex.getMessage(), "UTF-8"));
                    return Response.seeOther(new URL(clientURLString.toString())
                            .toURI()).build();
                } catch (MalformedURLException | URISyntaxException | UnsupportedEncodingException exception) {
                    //do nothing at this point
                    ex.printStackTrace();
                }
            }
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
        Preconditions.checkNotNull(request, Messages.i18n.format("nullValue", "JWT request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSamlResponse()), Messages.i18n.format("emptyValue", "SAML response"));
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
        Preconditions.checkNotNull(request, Messages.i18n.format("nullValue", "SAML logout request"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getIdpUrl()), Messages.i18n.format("emptyValue", "IDP URL"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpName()), Messages.i18n.format("emptyValue", "Service provider name"));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getUsername()), Messages.i18n.format("emptyValue", "User name"));
        return userFacade.generateSAML2LogoutRequest(request.getIdpUrl(), request.getSpName(), request.getUsername(), request.getRelayState());
    }

    @ApiOperation(value = "IDP single logout",
            notes = "This endpoint can be used by an IDP to logout a user.")
    @ApiResponses({@ApiResponse(code = 200, response = String.class, message = "IDP single logout.")})
    @POST
    @Path("/idp/slo")
    @Produces(MediaType.TEXT_PLAIN)
    public Response singleIDPLogout(@FormParam("SAMLResponse") String samlResponse, @FormParam("RelayState") String relayState) {
        return idpLogout(relayState);
    }

    @ApiOperation(value = "IDP single logout",
            notes = "This endpoint can be used by an IDP to logout a user using query strings.")
    @ApiResponses({@ApiResponse(code = 200, response = String.class, message = "IDP single logout.")})
    @GET
    @Path("/idp/slo")
    @Produces(MediaType.TEXT_PLAIN)
    public Response singleIDPLogoutQuery(@QueryParam("SAMLResponse") String samlResponse, @QueryParam("RelayState") String relayState) {
        return idpLogout(relayState);
    }

    @ApiOperation(value = "IDP single logout (external marketplace)", notes = "This endpoint can be used by an IDP to logout a user from the external marketplace.")
    @ApiResponses({@ApiResponse(code = 200, response = String.class, message = "IDP single logout.")})
    @POST
    @Path("/idp/slo/astad")
    @Produces(MediaType.TEXT_PLAIN)
    public Response singleIDPLogoutAStad(@FormParam("SAMLResponse") String samlResponse, @FormParam("RelayState") String relayState) {
        return idpLogout(relayState);
    }

    @ApiOperation(value = "IDP single logout (external marketplace)", notes = "This endpoint can be used by an IDP to logout a user from the external marketplace.")
    @ApiResponses({@ApiResponse(code = 200, response = String.class, message = "IDP single logout.")})
    @GET
    @Path("/idp/slo/astad")
    @Produces(MediaType.TEXT_PLAIN)
    public Response singleIDPLogoutAStadQuery(@QueryParam("SAMLResponse") String samlResponse, @QueryParam("RelayState") String relayState) {
        return idpLogout(relayState);
    }

    private Response idpLogout(String relayState){
        //TODO change redirect response
        //TODO clean cache based on SAML SLO Response
        String fallBackUrl = "https://google.com/";
        URI redirectURL = null;
        try {
            redirectURL = new URL(URLDecoder.decode(relayState, "UTF-8")).toURI();
        } catch (URISyntaxException | MalformedURLException | UnsupportedEncodingException | NullPointerException e) {
            try {
                redirectURL = new URL(fallBackUrl).toURI();
            }
            catch (URISyntaxException | MalformedURLException ex) {
                e.printStackTrace();
            }
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
            @ApiResponse(code = 200, response = JWTResponse.class, message = "Application JWT")
    })
    @GET
    @Path("/application/token")
    @Produces(MediaType.APPLICATION_JSON)
    public JWTResponse getAppJWT() {
        return orgFacade.getApplicationJWT();
    }
}
