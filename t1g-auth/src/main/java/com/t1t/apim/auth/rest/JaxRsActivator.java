package com.t1t.apim.auth.rest;

import com.t1t.apim.auth.rest.impl.mappers.IllegalArgumentExceptionMapper;
import com.t1t.apim.auth.rest.impl.mappers.NullPointerExceptionMapper;
import com.t1t.apim.auth.rest.impl.mappers.RestExceptionMapper;
import com.t1t.apim.auth.rest.resources.*;
import com.t1t.apim.auth.rest.resources.filter.RequestAUTHFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Created by Michallis on 02/08/15
 */
@ApplicationPath("/v1")
public class JaxRsActivator extends Application {

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
        //resources.add(KeyAuthorizationResource.class); TODO enable?
        //resources.add(BasicAuthorizationResource.class); TODO enable?
        resources.add(LoginResource.class);
        resources.add(OAuthResource.class);
        resources.add(OrganizationResource.class);
        resources.add(SearchResource.class);
        resources.add(GatewayResource.class);
        resources.add(RestExceptionMapper.class);
        resources.add(SystemResource.class);

        resources.add(IllegalArgumentExceptionMapper.class);
        resources.add(NullPointerExceptionMapper.class);
    }
}

