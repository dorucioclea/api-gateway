package com.t1t.digipolis.rest;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.rest.impl.mappers.RestExceptionMapper;
import com.t1t.digipolis.apim.rest.resources.filter.RequestAPIMFilter;
import com.t1t.digipolis.rest.resources.*;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.security.Security;
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
        //set filter for secured mode - will provide 'admin' user in dev.
        resources.add(RequestAPIMFilter.class);
        //resources.add(ResponseFilter.class);//clear the security context
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
        resources.add(SecurityResource.class);
        resources.add(RestExceptionMapper.class);
        resources.add(MigrationResource.class);
        resources.add(EventResource.class);
        //resources.add(CORSRequestFilter.class);//CORS Request filter
        //resources.add(CORSResponseFilter.class);//CORS Response filter
    }
}