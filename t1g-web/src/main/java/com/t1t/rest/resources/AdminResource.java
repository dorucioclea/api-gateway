package com.t1t.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.apim.beans.services.DefaultServiceTermsBean;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.apim.exceptions.NotAuthorizedException;
import com.t1t.apim.exceptions.i18n.Messages;
import com.t1t.apim.facades.DefaultsFacade;
import com.t1t.apim.maintenance.MaintenanceController;
import com.t1t.apim.rest.resources.IAdminResource;
import com.t1t.apim.rest.resources.IMaintenanceResource;
import com.t1t.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */

@Api(value = "/admin", description = "The Admin API.")
@Path("/admin")
@ApplicationScoped
public class AdminResource implements IAdminResource, IMaintenanceResource {


    @Inject
    private DefaultsFacade defFacade;
    @Inject
    private ISecurityContext securityContext;
    @Inject
    private MaintenanceController maintenance;

    @Override
    @ApiOperation(value = "Update or set the default service terms")
    @ApiResponses({
            @ApiResponse(code = 200, response = DefaultServiceTermsBean.class, message = "Default terms updated")
    })
    @PUT
    @Path("defaults/terms")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public DefaultServiceTermsBean updateDefaultServiceTerms(DefaultServiceTermsBean bean) throws NotAuthorizedException {
        isAdmin();
        Preconditions.checkNotNull(bean, Messages.i18n.format("emptyValue", "Service terms"));
        Preconditions.checkArgument(StringUtils.isNotEmpty(bean.getTerms()), Messages.i18n.format("emptyValue", "Service terms"));
        return defFacade.updateDefaultServiceTerms(bean);
    }

    @Override
    @ApiOperation(value = "Enable maintenance mode",
            notes = "If the maintenance mode is enabled, only HTTP safe methods will be allowed through. Unsafe methods will return a MaintenanceException with the message provided in this call.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Maintenance mode enabled")
    })
    @POST
    @Path("maintenance/enable")
    public void enableMaintenanceMode(String message) throws NotAuthorizedException {
        isAdmin();
        Preconditions.checkArgument(StringUtils.isNotEmpty(message), Messages.i18n.format("emptyValue", "Message"));
        maintenance.enableMaintenanceMode(message);
    }

    @Override
    @ApiOperation(value = "Disable maintenance mode")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Maintenance mode disabled")
    })
    @POST
    @Path("maintenance/disable")
    public void disableMaintenanceMode() throws NotAuthorizedException {
        isAdmin();
        maintenance.disableMaintenanceMode();
    }

    @Override
    @ApiOperation(value = "Update maintenance message", notes = "Update the maintenance mode message if it is enabled.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Maintenance message updated")
    })
    @PUT
    @Path("maintenance/message")
    public void updateMaintenanceMessage(String message) throws NotAuthorizedException {
        isAdmin();
        Preconditions.checkArgument(StringUtils.isNotEmpty(message), Messages.i18n.format("emptyValue", "Message"));
        maintenance.updateMaintenanceMessage(message);
    }

    private boolean isAdmin() {
        if (securityContext.isAdmin()) {
            return true;
        }
        else {
            throw ExceptionFactory.notAuthorizedException();
        }
    }
}