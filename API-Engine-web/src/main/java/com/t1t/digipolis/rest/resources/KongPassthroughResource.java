package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.qualifier.APIEngineContext;
import com.t1t.digipolis.rest.KongClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by michallispashidis on 02/08/15.
 */
@Api(value = "/kong", description = "Test endpoint. Should be used to validate the url.")
@Path("/kong")
@RequestScoped
public class KongPassthroughResource {

    private static final Logger LOG = LoggerFactory.getLogger(KongPassthroughResource.class);

    @Inject
    @APIEngineContext
    private KongClient restClient;

    @ApiOperation(value = "Get Kong information",
            notes = "Direct call to Kong for basic information")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongInfo.class, message = "Kong information.")
    })
    @GET
    @Produces("appilcation/json")
    public Response getInfo() {
        KongInfo info = restClient.getInfo();
        return Response.status(200).entity(info).type(MediaType.APPLICATION_JSON).build();
    }

    @ApiOperation(value = "Get Kong status",
            notes = "Direct call to Kong for status information")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongStatus.class, message = "Kong status.")
    })
    @GET
    @Path("/status")
    @Produces("application/json")
    public Response getStatus(){
        KongStatus stat = restClient.getStatus();
        return Response.status(200).entity(stat).type(MediaType.APPLICATION_JSON).build();
    }

    @ApiOperation(value = "Get API",
            notes = "Get API based on the API id or name")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongApi.class, message = "API for the given id/name")
    })
    @GET
    @Path("/apis/{id}")
    @Produces("application/json")
    public Response getApi(@PathParam("id")String id){
        KongApi api = restClient.getApi(id);
        return Response.status(200).entity(api).type(MediaType.APPLICATION_JSON).build();
    }

    @ApiOperation(value = "Add a new API",
            notes = "Add a new API definition")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongApi.class, message = "Persisted API definition")
    })
    @POST
    @Path("/apis")
    @Consumes("application/json")
    @Produces("application/json")
    public Response addApi(KongApi api){
        KongApi resultApi = restClient.addApi(api);
        return Response.status(200).entity(resultApi).type(MediaType.APPLICATION_JSON).build();
    }

    @ApiOperation(value = "Lists all APIs",
            notes = "Lists all defined APIs for the Kong gateway")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongApiList.class, message = "List of API defintions")
    })
    @GET
    @Path("/apis")
    @Produces("application/json")
    public Response listApis(){
        KongApiList apis = restClient.listApis();
        return Response.status(200).entity(apis).type(MediaType.APPLICATION_JSON).build();
    }

    @ApiOperation(value = "Update API",
            notes = "Update API with given id/name")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongApi.class, message = "Updated API definition")
    })
    @PATCH
    @Path("/apis/{id}")
    @Produces("application/json")
    public Response updateApi(@PathParam("id")String id, KongApi api){
        KongApi resultApi = restClient.updateApi(id, api);
        return Response.status(200).entity(resultApi).type(MediaType.APPLICATION_JSON).build();
    }

    @ApiOperation(value = "Create or update API",
            notes = "Creates or updates an existing API")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongApi.class, message = "Created or updated API")
    })
    @PUT
    @Path("/apis")
    @Produces("application/json")
    public Response createOrUpdateApi(KongApi api){
        KongApi resultApi = restClient.updateOrCreateApi(api);
        return Response.status(200).entity(resultApi).type(MediaType.APPLICATION_JSON).build();
    }

    @ApiOperation(value = "Delete API",
            notes = "Delete API with given id/name")
    @ApiResponses({
            @ApiResponse(code = 204, message = "no content")
    })
    @DELETE
    @Path("/apis/{id}")
    @Produces("application/json")
    public Response deleteApi(@PathParam("id")String id){
        restClient.deleteApi(id);
        return Response.status(204).build();
    }
}
