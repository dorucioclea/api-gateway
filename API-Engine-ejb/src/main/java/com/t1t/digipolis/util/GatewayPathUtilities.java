package com.t1t.digipolis.util;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.gateway.dto.Service;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by michallispashidis on 20/08/15.
 */
public class GatewayPathUtilities {
    /**
     * Create gateway path based on Gateway service DTO (contains version off course)
     *
     * @param service
     * @return
     */
    public static String generateGatewayContextPath(Service service) {
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getBasepath()));
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getOrganizationId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getVersion()));
        StringBuilder buildPath = new StringBuilder("/");
        buildPath.append(service.getOrganizationId().toLowerCase().trim().replaceAll(" ", ""));
        String servicePath = service.getBasepath();
        if (!servicePath.startsWith("/")) servicePath = "/" + servicePath;
        buildPath.append(servicePath);
        if (!buildPath.toString().endsWith("/")) buildPath.append("/");
        buildPath.append(service.getVersion());
        return buildPath.toString();
    }

    public static String generateGatewayContextPath(String orgId, String basePath, String serviceVersion) {
        Service tempService = new Service();
        tempService.setBasepath(basePath);
        tempService.setOrganizationId(orgId);
        tempService.setVersion(serviceVersion);
        return generateGatewayContextPath(tempService);
    }
}
