package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.audit.AuditEntryBean;
import com.t1t.digipolis.apim.beans.idm.UpdateUserBean;
import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.apim.beans.search.SearchCriteriaBean;
import com.t1t.digipolis.apim.beans.search.SearchResultsBean;
import com.t1t.digipolis.apim.beans.summary.ApplicationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.OrganizationSummaryBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import com.t1t.digipolis.apim.beans.user.*;
import com.t1t.digipolis.apim.core.IIdmStorage;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.InvalidSearchCriteriaException;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.facades.UserFacade;
import com.t1t.digipolis.apim.rest.resources.IUserResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.netty.util.internal.StringUtil;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.io.UnmarshallingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Api(value = "/users", description = "The User API.")
@Path("/users")
@ApplicationScoped
public class UserResource implements IUserResource {

    @Inject
    private
    IStorage storage;
    @Inject
    IIdmStorage idmStorage;
    @Inject
    ISecurityContext securityContext;
    @Inject
    IStorageQuery query;
    /*    @Inject @APIEngineContext
        Logger log;*/
    @Inject
    private UserFacade userFacade;

    public static final Logger log = LoggerFactory.getLogger(UserResource.class.getName());

    /**
     * Constructor.
     */
    public UserResource() {
    }

    @ApiOperation(value = "Get User by ID",
            notes = "Use this endpoint to get information about a specific user by the User ID.")
    @ApiResponses({
            @ApiResponse(code = 200, response = UserBean.class, message = "Full user information.")
    })
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserBean get(@PathParam("userId") String userId) throws UserNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.get(userId);
    }

    @ApiOperation(value = "Update a User by ID",
            notes = "Use this endpoint to update the information about a user.  This will fail unless the authenticated user is an admin or identical to the user being updated.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content")
    })
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("userId") String userId, UpdateUserBean user) throws UserNotFoundException, NotAuthorizedException {
        if (!securityContext.isAdmin() && !securityContext.getCurrentUser().equals(userId))
            throw ExceptionFactory.notAuthorizedException();
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        Preconditions.checkNotNull(user);
        Preconditions.checkArgument(user.getPic().getBytes().length <= 15000, "Logo should not be greater than 10k");
        userFacade.update(userId, user);
    }

    @ApiOperation(value = "Search for Users",
            notes = "Use this endpoint to search for users.  The search criteria is provided in the body of the request, including filters, order-by, and paging information.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "The search results (a page of organizations).")
    })
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<UserBean> search(SearchCriteriaBean criteria) throws InvalidSearchCriteriaException {
        return userFacade.search(criteria);
    }

    @ApiOperation(value = "List User Organizations",
            notes = "This endpoint returns the list of organizations that the user is a member of.  The user is a member of an organization if she has at least one role for the org.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = OrganizationSummaryBean.class, message = "List of organizations.")
    })
    @GET
    @Path("/{userId}/organizations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrganizationSummaryBean> getOrganizations(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.getOrganizations(userId);
    }

    @ApiOperation(value = "List User Applications",
            notes = "This endpoint returns all applications that the user has permission to edit.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ApplicationSummaryBean.class, message = "List of applications.")
    })
    @GET
    @Path("/{userId}/applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ApplicationSummaryBean> getApplications(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.getApplications(userId);
    }

    @ApiOperation(value = "List User Services",
            notes = "This endpoint returns all services that the user has permission to edit.")
    @ApiResponses({
            @ApiResponse(code = 200, responseContainer = "List", response = ServiceSummaryBean.class, message = "List of services.")
    })
    @GET
    @Path("/{userId}/services")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ServiceSummaryBean> getServices(@PathParam("userId") String userId) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.getServices(userId);
    }

    @ApiOperation(value = "Get User Activity",
            notes = "Use this endpoint to get information about the user's audit history.  This returns audit entries corresponding to each of the actions taken by the user.  For example, when a user creates a new Organization, an audit entry is recorded and would be included in the result of this endpoint.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SearchResultsBean.class, message = "List of audit entries.")
    })
    @GET
    @Path("/{userId}/activity")
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultsBean<AuditEntryBean> getActivity(@PathParam("userId") String userId, @QueryParam("page") int page, @QueryParam("count") int pageSize) {
        Preconditions.checkArgument(!StringUtils.isEmpty(userId));
        return userFacade.getActivity(userId, page, pageSize);
    }

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
    public String getSAML2AuthRequestUri(SAMLRequest request) {
        Preconditions.checkNotNull(request);
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getIdpUrl()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpName()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getSpUrl()));
        Preconditions.checkArgument(!StringUtils.isEmpty(request.getClientAppRedirect()));
        Preconditions.checkArgument(request.getToken().equals(ClientTokeType.opaque) || request.getToken().equals(ClientTokeType.jwt));
        return userFacade.generateSAML2AuthRequest(request.getIdpUrl(), request.getSpUrl(), request.getSpName(), request.getClientAppRedirect(), request.getToken(), request.getOverrideExpTimeInMinutes(),request.getOptionalClaimMap());
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
    public Response executeSAML2Callback(String request) {
        URI uri = null;
        try {
            SAMLResponseRedirect response = userFacade.processSAML2Response(request);
            String jwtToken = response.getToken();
            uri = new URL(response.getClientUrl()+"?jwt="+jwtToken).toURI();
            //Get the audience using the assertion => create new table for registered audiences == client applications.
            //String audience = assertion.getConditions().getAudienceRestrictions().get(0).getAudiences().get(0).getAudienceURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(uri!=null)return Response.seeOther(uri).build();
        return Response.ok(request).build();
    }

    @ApiOperation(value = "User logout",
            notes = "This endpoint performs actions upon an IDP triggered SAML2 logout request.")
    @ApiResponses({
            @ApiResponse(code = 200,response = String.class, message = "User specific logout.")
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
        return userFacade.generateSAML2LogoutRequest(request.getIdpUrl(),request.getSpName(),request.getUsername());
        //don't do anything, logout triggered from client

/*        String url = "https://idp.t1t.be:9443/dashboard";
        URI redirectURL = null;
        try {
            redirectURL = new URL(url).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/
        //return Response.seeOther(redirectURL).build();
    }

    @ApiOperation(value = "IDP single logout",
            notes = "This endpoint can be used by an IDP to logout a user.")
    @ApiResponses({
            @ApiResponse(code = 200,response = String.class, message = "IDP single logout.")
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
        if(redirectURL!=null)return Response.seeOther(redirectURL).build();
        return Response.ok(redirectURL).build();
    }
}
