package com.t1t.digipolis.apim.auth.rest;

import com.t1t.digipolis.apim.auth.rest.impl.mappers.RestExceptionMapper;
import com.t1t.digipolis.apim.auth.rest.resources.KeyAuthorizationResource;
import com.t1t.digipolis.apim.auth.rest.resources.SystemResource;
import com.t1t.digipolis.apim.auth.rest.resources.filter.RequestAUTHFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Created by Michallis on 02/08/15
 */
@ApplicationPath("/v1")
public class JaxRsActivator extends Application {

    public JaxRsActivator() {
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        //set filter for secured mode
        resources.add(RequestAUTHFilter.class); // request filter
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
        resources.add(KeyAuthorizationResource.class);
        resources.add(SystemResource.class);
        resources.add(RestExceptionMapper.class);
    }
}

