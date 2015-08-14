package com.t1t.digipolis.rest;
import com.t1t.digipolis.rest.resources.*;

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
        resources.add(KongInfoResource.class);
        resources.add(KongApiResource.class);
        resources.add(SystemResource.class);
        resources.add(ActionResource.class);
        resources.add(CurrentUserResource.class);
        resources.add(GatewayResource.class);
        resources.add(PermissionsResource.class);
        resources.add(PluginResource.class);
        resources.add(PolicyDefinitionResource.class);
        resources.add(RoleResource.class);
        resources.add(SearchResource.class);
        resources.add(UserResource.class);
        resources.add(OrganizationResource.class);
        //resources.add(CORSRequestFilter.class);//CORS Request filter
        //resources.add(CORSResponseFilter.class);//CORS Response filter
    }
}

