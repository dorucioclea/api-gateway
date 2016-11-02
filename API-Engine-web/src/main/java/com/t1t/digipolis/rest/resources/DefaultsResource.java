package com.t1t.digipolis.rest.resources;

import com.t1t.digipolis.apim.beans.exceptions.ErrorBean;
import com.t1t.digipolis.apim.beans.services.DefaultServiceTermsBean;
import com.t1t.digipolis.apim.facades.DefaultsFacade;
import com.t1t.digipolis.apim.rest.resources.IDefaultsResource;
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

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Api(value = "/defaults", description = "The Defaults API.")
@Path("/defaults")
@ApplicationScoped
public class DefaultsResource implements IDefaultsResource {

    @Inject
    private DefaultsFacade defFacade;

    @Override
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