package com.t1t.digipolis.apim.auth.rest.resources.filter;

import com.t1t.digipolis.apim.auth.rest.JaxRsActivator;
import com.t1t.digipolis.apim.exceptions.ApplicationNotFoundException;
import com.t1t.digipolis.apim.security.ISecurityAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by michallispashidis on 20/09/15.
 * A request filter in order to validate an authorized application.
 */
public class RequestAUTHFilter implements ContainerRequestFilter {
    /**
     * Logger: is not possible to inject logger in filters
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestAUTHFilter.class.getName());
    private static final String HEADER_APIKEY_USER = "X-Consumer-Username";//here the consumer should be an application consumer

    //Security context
    @Inject
    private ISecurityAppContext securityAppContext;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //Get the authorization header
        String appId = containerRequestContext.getHeaderString(HEADER_APIKEY_USER);
        String validatedApp = "";
        try {
            if(!JaxRsActivator.securedMode){
                securityAppContext.setCurrentApplication("dummyapp");
            }else {
                validatedApp = securityAppContext.setCurrentApplication(appId);
            }
        } catch (ApplicationNotFoundException ex) {
            LOG.info("Unauthorized application:{}", appId);
            containerRequestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Application cannot access the resource.")
                    .build());
            return;
        }
    }
}

