package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.gateway.dto.Service;

/**
 * Created by michallispashidis on 08/09/15.
 */
public class NameConventionUtil {
    /**
     * Generates a unique service name for Kong.
     * The name is constituted of the organizationid.serviceid.version
     *
     * @param service
     * @return
     */
    public static String generateServiceUniqueName(Service service) {
        return generateServiceUniqueName(service.getOrganizationId(), service.getServiceId(), service.getVersion());
    }

    public static String generateServiceUniqueName(String orgId, String serviceId, String serviceVersionsId){
        StringBuilder serviceGatewayName = new StringBuilder(orgId)
                .append(".")
                .append(serviceId)
                .append(".")
                .append(serviceVersionsId);
        return serviceGatewayName.toString().toLowerCase();
    }
}
