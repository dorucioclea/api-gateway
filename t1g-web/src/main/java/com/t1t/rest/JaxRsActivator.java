package com.t1t.rest;

import com.t1t.rest.mappers.IllegalArgumentExceptionMapper;
import com.t1t.rest.mappers.NullPointerExceptionMapper;
import com.t1t.rest.mappers.RestExceptionMapper;
import com.t1t.rest.servlet.RequestAPIMFilter;
import com.t1t.rest.resources.*;

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
        resources.add(ActionResource.class);
        resources.add(AdminResource.class);
        resources.add(BrandingResource.class);
        resources.add(CurrentUserResource.class);
        resources.add(DefaultsResource.class);
        resources.add(GatewayResource.class);
        resources.add(OrganizationResource.class);
        resources.add(PermissionsResource.class);
        resources.add(PolicyDefinitionResource.class);
        resources.add(RoleResource.class);
        resources.add(SearchResource.class);
        resources.add(SecurityResource.class);
        resources.add(SyncResource.class);
        resources.add(SystemResource.class);
        resources.add(UserResource.class);

        resources.add(RestExceptionMapper.class);
        resources.add(IllegalArgumentExceptionMapper.class);
        resources.add(NullPointerExceptionMapper.class);
        //resources.add(CORSRequestFilter.class);//CORS Request filter
        //resources.add(CORSResponseFilter.class);//CORS Response filter
    }
}