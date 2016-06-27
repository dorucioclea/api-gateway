package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.beans.summary.ServiceSummaryBean;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 14/02/16.
 * Facilitates the filtering of orgs, apps, services in context of the calling application (mkt, pub, ...)
 */
public class ServiceScopeUtil {
    /**
     * Utility to filter a list of service beans based on a predefined scope (consumer application scope).
     * @param svbList
     * @param consumerType
     * @return
     */
    public static List<ServiceVersionBean> resolveSVBScope(List<ServiceVersionBean> svbList,String consumerType){
        List<ServiceVersionBean> allServicesByStatusFiltered = new ArrayList<>();
        if(!StringUtils.isEmpty(consumerType)){
            for(ServiceVersionBean svb:svbList){
                if((svb.getVisibility().stream().filter(svbVis -> (svbVis.getCode().equalsIgnoreCase(consumerType)&&svbVis.getShow())).collect(Collectors.toList())).size()>0) allServicesByStatusFiltered.add(svb);
            }
        }else{
            allServicesByStatusFiltered = svbList;
        }
        return allServicesByStatusFiltered;
    }
}
