package com.t1t.digipolis.rest;
import com.t1t.digipolis.rest.resources.MemberResourceRESTService;
import com.t1t.digipolis.rest.resources.TestConnectionResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Created by Michallis on 02/08/15
 */
@ApplicationPath("/v1")
public class JaxRsActivator extends Application {
    public static boolean securedMode;

    static {
        /**
         * Start server in secured mode -> requiring API key and secret.
         * When the server needs to be tested, you can set the boolean value to false, thus the filter driver
         * will not be loaded and REST communication can be done unsecure.
         */
        securedMode = false;
    }

    public JaxRsActivator() {
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        //set filter for secured mode
        //if (securedMode) resources.add(RequestAuthorizationFilter.class);
        //resources.add(ResponseFilter.class);//we don't do anything with the response headers on client side
        addRestResourceClasses(resources);
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(TestConnectionResource.class);
        //resources.add(CORSRequestFilter.class);//CORS Request filter
        //resources.add(CORSResponseFilter.class);//CORS Response filter
    }
}

