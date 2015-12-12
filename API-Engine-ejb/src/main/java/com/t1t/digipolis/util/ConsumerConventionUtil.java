package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.apps.AppIdentifier;
import org.apache.commons.lang3.StringUtils;

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

    public static AppIdentifier parseApplicationIdentifier(String appId){
        if(StringUtils.isEmpty(appId))return null;
        if(StringUtils.countMatches(appId,".")!=2)return null;
        AppIdentifier appIdBean = new AppIdentifier();
        String[] splitResult = appId.split("\\.");
        if(splitResult.length==3){
            appIdBean.setOrgId(splitResult[0]);
            appIdBean.setAppId(splitResult[1]);
            appIdBean.setVersion(splitResult[2]);
        }
        return appIdBean;
    }
}
