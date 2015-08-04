package com.t1t.digipolis.rest.resources;

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

/**
 * Created by michallispashidis on 02/08/15.
 */
@Api(value = "/test", description = "Test endpoint. Should be used to validate the url.")


@Path("/test")
@RequestScoped
public class TestConnectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TestConnectionResource.class);

    @ApiOperation(value = "Test the connection",
            notes = "Calling this endpoint will return a version number. It can be used to verify if a correct API-Engine URL was used.")
    @ApiResponses({
            @ApiResponse(code = 412, message = "JSON validation failed, a property is missing!"),
            @ApiResponse(code = 415, message = "Unsupported mediatype, add HTTP header: 'Content-Type = application/json' to the request"),
            @ApiResponse(code = 200, response = String.class, message = "Result of the request.")
    })

    @GET
    @Produces("text/plain")
    public String testConnection() {
        return "0.0.1";
    }
}
