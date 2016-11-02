package com.t1t.digipolis.rest.resources;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.exceptions.ErrorBean;
import com.t1t.digipolis.apim.beans.services.DefaultServiceTermsBean;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.exceptions.SystemErrorException;
import com.t1t.digipolis.apim.facades.DefaultsFacade;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import com.t1t.digipolis.apim.rest.resources.IAdminResource;
import com.t1t.digipolis.apim.security.ISecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class AdminResource implements IAdminResource {


    @Inject
    private DefaultsFacade defFacade;
    @Inject
    ISecurityContext securityContext;

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
        Preconditions.checkNotNull(bean);
        Preconditions.checkArgument(StringUtils.isNotEmpty(bean.getTerms()));
        return defFacade.updateDefaultServiceTerms(bean);
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