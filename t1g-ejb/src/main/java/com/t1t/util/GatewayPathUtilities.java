package com.t1t.util;

import com.google.common.base.Preconditions;
import com.t1t.apim.gateway.dto.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public static List<String> generateGatewayContextPath(Service service) {
        List<String> rval = new ArrayList<>();
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getOrganizationId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(service.getVersion()));
        service.getBasepaths().forEach(basepath -> {
            Preconditions.checkArgument(!StringUtils.isEmpty(basepath));
            StringBuilder buildPath = new StringBuilder("/");
            buildPath.append(service.getOrganizationId().toLowerCase().trim().replaceAll(" ", ""));
            if (!basepath.startsWith("/")) basepath = "/" + basepath;
            buildPath.append(basepath);
            if (!buildPath.toString().endsWith("/")) buildPath.append("/");
            buildPath.append(service.getVersion());
            rval.add(buildPath.toString());
        });
        return rval;
    }

    public static List<String> generateGatewayContextPath(String orgId, Set<String> basePaths, String serviceVersion) {
        Service tempService = new Service();
        tempService.setBasepaths(basePaths);
        tempService.setOrganizationId(orgId);
        tempService.setVersion(serviceVersion);
        return generateGatewayContextPath(tempService);
    }
}
