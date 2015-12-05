package com.t1t.digipolis.apim.auth.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestBasicAuthBean;
import com.t1t.digipolis.apim.beans.authorization.AuthConsumerRequestKeyAuthBean;
import com.t1t.digipolis.apim.beans.exceptions.ErrorBean;
import com.t1t.digipolis.apim.exceptions.ApplicationNotFoundException;
import com.t1t.digipolis.apim.facades.AuthorizationFacade;
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

/**
 * Created by michallispashidis on 21/09/15.
 */
/*@Api(value = "/basicauth", description = "The Authorization API.  This API facilitates the creation of authorizations.")
@Path("/basicauth")
@ApplicationScoped*/
public class BasicAuthorizationResource implements IBasicAuthorization {
    @Inject
    private AuthorizationFacade authorizationFacade;

    @ApiOperation(value = "Create Basic authentication token for an application consumer.",
            notes = "Use this endpoint to register an application user, with basic authorization credentials (received from 1 registered service), in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username and generated BasicAuth token."),
            @ApiResponse(code = 409, response = String.class, message = "Conflict error.")
    })
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response createBasicAuthConsumer(AuthConsumerRequestBasicAuthBean criteria) throws ApplicationNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(criteria.getOrgId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(criteria.getAppId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(criteria.getAppVersion()));
        Preconditions.checkArgument(!StringUtils.isEmpty(criteria.getCustomId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(criteria.getContractApiKey()));
        Preconditions.checkArgument(!StringUtils.isEmpty(criteria.getUserLoginName()));
        Preconditions.checkArgument(!StringUtils.isEmpty(criteria.getUserLoginPassword()));
        AuthConsumerBean resConsumer = authorizationFacade.createBasicAuthConsumer(criteria);
        if(resConsumer!=null)return Response.ok().entity(resConsumer).build();
        else {
            ErrorBean error = new ErrorBean();
            error.setMessage("Application not found:" + criteria.getAppId());
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
    }

    @ApiOperation(value = "Retrieve Basic authentication token for an application consumer.",
            notes = "Use this endpoint to get an application user basic authentication token, in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, response = AuthConsumerBean.class, message = "The result unique username and generated BasicAuth token."),
            @ApiResponse(code = 409, response = ErrorBean.class, message = "Conflict error.")
    })
    @GET
    @Path("/{key}/org/{orgId}/app/{appId}/version/{version}/user/{customUser}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response getBasicAuthConsumer(@PathParam("key")String apiKey,
                                         @PathParam("orgId")String orgId,
                                         @PathParam("appId")String appId,
                                         @PathParam("version")String version,
                                         @PathParam("customUser")String customId) throws ApplicationNotFoundException {
        Preconditions.checkArgument(!StringUtils.isEmpty(apiKey));
        Preconditions.checkArgument(!StringUtils.isEmpty(orgId));
        Preconditions.checkArgument(!StringUtils.isEmpty(appId));
        Preconditions.checkArgument(!StringUtils.isEmpty(version));
        Preconditions.checkArgument(!StringUtils.isEmpty(customId));
        AuthConsumerRequestBasicAuthBean criteria = new AuthConsumerRequestBasicAuthBean();
        criteria.setContractApiKey(apiKey);
        criteria.setOrgId(orgId);
        criteria.setAppId(appId);
        criteria.setAppVersion(version);
        criteria.setCustomId(customId);
        AuthConsumerBean result = authorizationFacade.getBasicAuthConsumer(criteria);
        if(result!=null) return Response.ok().entity(result).build();
        else{
            ErrorBean error = new ErrorBean();
            error.setMessage("Application not found:"+criteria.getAppId());
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
    }

    @ApiOperation(value = "Delete Basic authentication credentials for a consumer in the context of an application version.",
            notes = "Use this endpoint to delete an application user with basic authentication token (consumer), in the context of your application version.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "The result unique username and generated KeyAuth token."),
            @ApiResponse(code = 409, response = ErrorBean.class, message = "Conflict error.")
    })
    @DELETE
    @Path("/{key}/org/{orgId}/app/{appId}/version/{version}/user/{customUser}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Override
    public Response deleteBasicAuthConsumer(@PathParam("key")String apiKey,
                                            @PathParam("orgId")String orgId,
                                            @PathParam("appId")String appId,
                                            @PathParam("version")String version,
                                            @PathParam("customUser")String customId) throws ApplicationNotFoundException {
        AuthConsumerRequestBasicAuthBean criteria = new AuthConsumerRequestBasicAuthBean();
        criteria.setContractApiKey(apiKey);
        criteria.setOrgId(orgId);
        criteria.setAppId(appId);
        criteria.setAppVersion(version);
        criteria.setCustomId(customId);
        authorizationFacade.deleteBasicAuthConsumer(criteria);
        return Response.ok().build();
    }
}
