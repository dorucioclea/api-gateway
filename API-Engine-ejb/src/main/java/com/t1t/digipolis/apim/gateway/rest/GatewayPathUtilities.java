package com.t1t.digipolis.apim.gateway.rest;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.gateway.dto.Service;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by michallispashidis on 20/08/15.
 */
public class GatewayPathUtilities {
    public static String generateGatewayContextPath(Service service){
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getBasepath()));
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getOrganizationId()));
        StringBuilder buildPath = new StringBuilder("/");
        buildPath.append(service.getOrganizationId().toLowerCase().trim().replaceAll(" ",""));
        String servicePath=service.getBasepath();
        if(!servicePath.startsWith("/"))servicePath="/"+servicePath;
        buildPath.append(servicePath);
        if(!StringUtils.isEmpty(service.getVersion())){
            if(!buildPath.toString().endsWith("/")) buildPath.append("/");
            buildPath.append(service.getVersion());
        }
        return buildPath.toString();
    }
}
