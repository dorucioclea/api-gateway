package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.actions.ActionBean;
import com.t1t.digipolis.apim.beans.actions.SwaggerDocBean;
import com.t1t.digipolis.apim.core.IApplicationValidator;
import com.t1t.digipolis.apim.core.IServiceValidator;
import com.t1t.digipolis.apim.core.IStorage;
import com.t1t.digipolis.apim.core.IStorageQuery;
import com.t1t.digipolis.apim.exceptions.ActionException;
import com.t1t.digipolis.apim.facades.ActionFacade;
import com.t1t.digipolis.apim.gateway.IGatewayLinkFactory;
import com.t1t.digipolis.apim.rest.resources.IActionResource;
import com.t1t.digipolis.apim.rest.resources.IOrganizationResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Implementation of the Action API.
 */
@Api(value = "/actions", description = "The Action API.  This API allows callers to perform actions on various entities - actions other than the standard REST crud actions.")
@Path("/actions")
@ApplicationScoped
public class ActionResource implements IActionResource {

    @Inject
    IStorage storage;
    @Inject
    IStorageQuery query;
    @Inject
    IGatewayLinkFactory gatewayLinkFactory;
    @Inject
    IOrganizationResource orgs;
    @Inject
    IServiceValidator serviceValidator;
    @Inject
    IApplicationValidator applicationValidator;
    @Inject
    ISecurityContext securityContext;
    @Inject ActionFacade actionFacade;

    /**
     * Constructor.
     */
    public ActionResource() {
    }

    @ApiOperation(value = "Execute an Entity Action (lifecycle related)",
            notes = "Call this endpoint in order to execute actions for entities such" +
                    " as Plans, Services, or Applications.  The type of the action must be" +
                    " included in the request payload and can be one of: publishService, retireService, registerApplication, unregisterApplication, lockPlan .")
    @ApiResponses({
            @ApiResponse(code = 204, message = "successful, no content"),
            @ApiResponse(code = 409, message = "service has contracts")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void performAction(ActionBean action) throws ActionException {
        Preconditions.checkNotNull(action);
        actionFacade.performAction(action);
    }

    @ApiOperation(value = "Fetch Swagger documentation from a remote server",
            notes = "Provide this endpoint with a URI to your Swagger documentation and it will return a JSON string.")
    @ApiResponses({
            @ApiResponse(code = 200, response = SwaggerDocBean.class, message = "Swagger Documentation")
    })
    @POST
    @Path("/swaggerdoc/")
    @Produces(MediaType.APPLICATION_JSON)
    public SwaggerDocBean fetchSwaggerDoc(SwaggerDocBean swaggerDocBean) throws ActionException {
        Preconditions.checkNotNull(swaggerDocBean.getSwaggerURI());
        return actionFacade.fetchSwaggerDoc(swaggerDocBean);
    }
}
