package com.t1t.apim.rest.resources.filter;

import com.t1t.apim.security.ISecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * Created by michallispashidis on 20/09/15.
 */
public class ResponseAPIMFilter implements ContainerResponseFilter {
    /**
     * Logger: is not possible to inject logger in filters
     */
    private static final Logger LOG = LoggerFactory.getLogger(ResponseAPIMFilter.class.getName());
    private static final String HEADER_APIKEY_USER = "X-Consumer-Username";
    //exclusions
    private static final String REDIRECT_PATH = "/users/idp/redirect";
    private static final String IDP_CALLBACK = "/users/idp/callback";

    //Security context
    @Inject
    private ISecurityContext securityContext;

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
        //nothing to do at the moment
    }
}

