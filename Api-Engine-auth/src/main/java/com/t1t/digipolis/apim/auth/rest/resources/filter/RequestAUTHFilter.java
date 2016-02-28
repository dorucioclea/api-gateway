package com.t1t.digipolis.apim.auth.rest.resources.filter;

import com.t1t.digipolis.apim.auth.rest.JaxRsActivator;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
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
 * The request filter doesn't take into account any JWT.
 * The request filter is based upon APIkey, the application context will be provided.
 */
public class RequestAUTHFilter implements ContainerRequestFilter {
    /**
     * Logger: is not possible to inject logger in filters
     */
    private static final Logger LOG = LoggerFactory.getLogger(RequestAUTHFilter.class.getName());
    private static final String HEADER_CONSUMER_USERNAME = "x-consumer-username";//here the consumer should be an application consumer
    private static final String SWAGGER_DOC_URI = "API-Engine-auth";
    private static final String SWAGGER_DOC_JSON = "/API-Engine-auth/v1/swagger.json";

    //Security context
    @Inject
    private ISecurityAppContext securityAppContext;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //Get the authorization header
        String appId = containerRequestContext.getHeaderString(HEADER_CONSUMER_USERNAME);
        try {
            if(!JaxRsActivator.securedMode){
                securityAppContext.setCurrentApplication("dummyapp");
            }else {
                securityAppContext.setCurrentApplication(appId);
            }
        } catch (ApplicationNotFoundException|StorageException ex) {
            LOG.info("Unauthorized application:{}", appId);
            containerRequestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Application cannot access the resource.")
                    .build());
            return;
        }
    }
}

