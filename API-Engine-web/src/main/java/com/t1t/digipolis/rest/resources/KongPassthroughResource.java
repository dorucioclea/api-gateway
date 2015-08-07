package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.kong.model.KongInfo;
import com.t1t.digipolis.kong.model.KongStatus;
import com.t1t.digipolis.qualifier.APIEngineContext;
import com.t1t.digipolis.rest.KongClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    @ApiOperation(value = "Test the connection",
            notes = "Calling this endpoint will return a version number. It can be used to verify if a correct API-Engine URL was used.")
    @ApiResponses({
            @ApiResponse(code = 412, message = "JSON validation failed, a property is missing!"),
            @ApiResponse(code = 415, message = "Unsupported mediatype, add HTTP header: 'Content-Type = application/json' to the request"),
            @ApiResponse(code = 200, response = KongInfo.class, message = "Result of the request.")
    })

    @GET
    @Produces("appilcation/json")
    public Response getInfo() {
        KongInfo info = restClient.getInfo();
        return Response.status(200).entity(info).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/status")
    @Produces("application/json")
    public Response getStatus(){
        KongStatus stat = restClient.getStatus();
        return Response.status(200).entity(stat).type(MediaType.APPLICATION_JSON).build();
    }
}
