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

import javax.enterprise.context.ApplicationScoped;
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
@ApplicationScoped
public class KongInfoResource {

    private static final Logger LOG = LoggerFactory.getLogger(KongInfoResource.class);

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
    public Response getStatus() {
        KongStatus stat = restClient.getStatus();
        return Response.status(200).entity(stat).type(MediaType.APPLICATION_JSON).build();
    }

}
