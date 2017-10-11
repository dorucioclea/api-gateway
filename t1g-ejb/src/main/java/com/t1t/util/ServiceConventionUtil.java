package com.t1t.util;

import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ServiceVersionSummaryBean;
import com.t1t.apim.gateway.dto.Service;

/**
 * Created by michallispashidis on 08/09/15.
 */
public class ServiceConventionUtil {
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

    public static String generateServiceUniqueName(String orgId, String serviceId, String serviceVersionsId) {
        StringBuilder serviceGatewayName = new StringBuilder(orgId)
                .append(".")
                .append(serviceId)
                .append(".")
                .append(serviceVersionsId);
        return serviceGatewayName.toString().toLowerCase();
    }

    public static String generateServiceUniqueName(ServiceVersionBean svb) {
        return generateServiceUniqueName(svb.getService().getOrganization().getId(),
                svb.getService().getId(), svb.getVersion());
    }

    public static String[] getOrgSvcVersionIds(ServiceVersionBean svb) {
        return new String[]{svb.getService().getOrganization().getId(), svb.getService().getId(), svb.getVersion()};
    }

    public static String[] getOrgSvcVersionIdsFromUniqueName(String uniqueName) {
        String[] rval = uniqueName.split("\\.");
        if (rval.length != 3) {
            throw new IllegalArgumentException("Unique name must contain 3 substrings separated by 2 periods");
        }
        return rval;
    }

    public static ServiceVersionSummaryBean getServiceVersionSummaryFromUniqueName(String uniqueServiceId) {
        ServiceVersionSummaryBean rval = new ServiceVersionSummaryBean();
        String[] split = getOrgSvcVersionIdsFromUniqueName(uniqueServiceId);
        rval.setOrganizationId(split[0]);
        rval.setId(split[1]);
        rval.setVersion(split[2]);
        return rval;
    }

    public static String getDatadogUniqueName(String orgId, String svcId, String version) {
        return generateServiceUniqueName(orgId, svcId, version).replace(".", "_").replace("-", "_");
    }
}
