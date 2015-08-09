package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.kong.model.KongApi;
import com.t1t.digipolis.kong.model.KongApiList;
import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongStatus;
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
@Path("/kong/api")
@RequestScoped
public class KongApiResource {

    private static final Logger LOG = LoggerFactory.getLogger(KongApiResource.class);
    @Inject
    @APIEngineContext
    private KongClient restClient;


    @ApiOperation(value = "Get API",
            notes = "Get API based on the API id or name")
    @ApiResponses({
            @ApiResponse(code = 200, response = KongApi.class, message = "API for the given id/name")
    })
    @GET
    @Path("/{id}")
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
    @Path("/")
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
    @Path("/all")
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
    @Path("/{id}")
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
    @Path("/")
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
    @Path("/{id}")
    @Produces("application/json")
    public Response deleteApi(@PathParam("id")String id){
        restClient.deleteApi(id);
        return Response.status(204).build();
    }
}
