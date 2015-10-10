package com.t1t.digipolis.apim.rest.resources.filter;

import com.t1t.digipolis.apim.exceptions.UserNotFoundException;
import com.t1t.digipolis.apim.security.ISecurityContext;
import com.t1t.digipolis.rest.JaxRsActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by michallispashidis on 20/09/15.
 */
public class RequestAPIMFilter implements ContainerRequestFilter {
    /**
     * Logger: is not possible to inject logger in filters
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestAPIMFilter.class.getName());
    private static final String HEADER_APIKEY_USER = "X-Consumer-Username";
    //exclusions
    private static final String REDIRECT_PATH = "/users/idp/redirect";
    private static final String IDP_CALLBACK = "/users/idp/callback";
    private static final String IDP_SLO = "/users/idp/slo";
    private static final String SYSTEM_INFO = "/system/status";
    private static final String SWAGGER_DOC_URI = "API-Engine-web";
    private static final String SWAGGER_DOC_JSON = "/API-Engine-web/v1/swagger.json";

    //Security context
    @Inject
    private ISecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //dev mode
        LOG.info("Security context - request:{}",containerRequestContext.getUriInfo().getRequestUri().getPath());
        if (!JaxRsActivator.securedMode) {
            securityContext.setCurrentUser("admin");
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(REDIRECT_PATH)) {
            ;//allow access without setting security context.
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(IDP_CALLBACK)) {
            ;//allow from idp
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(IDP_SLO)) {
            ;//allow from idp
        } else if (containerRequestContext.getUriInfo().getRequestUri().getPath().contains(SYSTEM_INFO)) {
            ;//allow from idp
        } else {
            //Get the authorization header
            String userId = containerRequestContext.getHeaderString(HEADER_APIKEY_USER);
            String validatedUser = "";
            try {
                validatedUser = securityContext.setCurrentUser(userId);
            } catch (UserNotFoundException ex) {
                LOG.debug("Unauthorized user:{}", userId);
                containerRequestContext.abortWith(Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity("User cannot access the resource.")
                        .build());
                return;
            }
        }
    }
}

