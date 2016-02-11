package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.apps.AppIdentifier;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by michallispashidis on 19/09/15.
 */
public class ConsumerConventionUtil {
    public static String createAppConsumerUnqiueId(String orgId, String appId, String appVersionId, String userId){
        StringBuilder uniqueName = new StringBuilder("");
        uniqueName.append(orgId)
                .append(".")
                .append(appId)
                .append(".")
                .append(appVersionId)
                .append(".")
                .append(userId);
        return uniqueName.toString().toLowerCase();
    }

    public static String createAppUniqueId(String orgId, String appId, String appVersionId){
        StringBuilder uniqueName = new StringBuilder("");
        uniqueName.append(orgId)
                .append(".")
                .append(appId)
                .append(".")
                .append(appVersionId);
        return uniqueName.toString().toLowerCase();
    }

    public static String createUserUniqueId(String userName) {
        return userName.toLowerCase();
    }

    /**
     * The application identifier denotes the consuming application of the service.
     * In order to support scoped marketplaces/publishers; a scope will be set if the unique appId contains a known prefix.
     * The known prefixes are in configruation to avoid table lookups.
     * @param appId
     * @return
     */
    public static AppIdentifier parseApplicationIdentifier(String appId, List<String> availableMarketplaces){
        if(StringUtils.isEmpty(appId))return null;//normally shouldn't be null
        if(StringUtils.countMatches(appId,".")!=2)return null;
        AppIdentifier appIdBean = new AppIdentifier();
        String[] splitResult = appId.split("\\.");
        if(splitResult.length==3){
            //set optional marketplace scope
            if(availableMarketplaces!=null && availableMarketplaces.size()>0){
                appIdBean.setScope(((availableMarketplaces.contains(splitResult[0].toLowerCase()))?splitResult[0].toLowerCase():""));
            }
            appIdBean.setAppId(splitResult[1]);
            appIdBean.setVersion(splitResult[2]);
        }
        return appIdBean;
    }
}
