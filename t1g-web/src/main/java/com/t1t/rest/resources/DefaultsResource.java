package com.t1t.rest.resources;

import com.t1t.apim.beans.exceptions.ErrorBean;
import com.t1t.apim.beans.services.DefaultServiceTermsBean;
import com.t1t.apim.facades.DefaultsFacade;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Api(value = "/defaults", description = "The Defaults API.")
@Path("/defaults")
@ApplicationScoped
public class DefaultsResource {

    @Inject
    private DefaultsFacade defFacade;


    @ApiOperation(value = "Retrieve the default service terms")
    @ApiResponses({
            @ApiResponse(code = 200, response = DefaultServiceTermsBean.class, message = "Default terms"),
            @ApiResponse(code = 404, response = ErrorBean.class, message = "Default terms not found")
    })
    @GET
    @Path("terms")
    @Produces(MediaType.APPLICATION_JSON)
    public DefaultServiceTermsBean getDefaultServiceTerms() {
        return defFacade.getDefaultServiceTerms();
    }
}