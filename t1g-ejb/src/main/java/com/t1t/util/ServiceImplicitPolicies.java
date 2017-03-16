package com.t1t.util;

import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.visibility.VisibilityBean;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 14/02/16.
 */
public class ServiceImplicitPolicies {
    private static String EXTERNAL_MKT_PREFIX = "ext";
    /**
     * Verify if the service has a visibility for the external marketplace.
     * This bean should be more generic, but here, it's implemented as an extra functionality.
     *
     * @param svb
     * @return
     */
    public static boolean verifyIfIPRestrictionShouldBeSet(ServiceVersionBean svb, boolean admin){
        Set<VisibilityBean> visibilities = svb.getVisibility();
        if(visibilities!=null && visibilities.size()>0){
            List<VisibilityBean> filteredCollection = visibilities.stream().filter(vs ->
                vs.getCode().trim().equalsIgnoreCase(EXTERNAL_MKT_PREFIX) && (!vs.getShow() || admin)
            ).collect(Collectors.toList());
            if(filteredCollection.size()>0)return true;
        }
        return false;
    }
}
