package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.apps.AppIdentifier;
import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;
import com.t1t.digipolis.apim.beans.managedapps.ManagedApplicationBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;

/**
 * Created by michallispashidis on 19/09/15.
 */
public class ConsumerConventionUtil {
    private static Logger _LOG = LoggerFactory.getLogger(ConsumerConventionUtil.class.getName());
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

    public static String createAppUniqueId(ApplicationVersionBean avb) {
        return createAppUniqueId(avb.getApplication().getOrganization().getId(),
                avb.getApplication().getId(), avb.getVersion());
    }

    public static String createAppVersionlessId(String orgId, String appId){
        StringBuilder uniqueName = new StringBuilder("");
        uniqueName.append(orgId)
                .append(".")
                .append(appId);
        return uniqueName.toString().toLowerCase();
    }

    public static String createUserUniqueId(String userName) {
        return userName.toLowerCase();
    }

    public static String createManagedApplicationConsumerName(ManagedApplicationBean bean) {
        StringBuilder builder = new StringBuilder(bean.getPrefix());
        if (builder.length() > 0) builder.append(".");
        return builder.append(bean.getAppId())
                .append(".")
                .append(bean.getVersion())
                .toString();
    }

    /**
     * The application identifier denotes the consuming application of the service.
     * In order to support scoped marketplaces/publishers; a scope will be set if the unique appId contains a known prefix.
     * The known prefixes are in configruation to avoid table lookups.
     * @param appId
     * @return
     */
    public static AppIdentifier parseApplicationIdentifier(String appId){
        if(StringUtils.isEmpty(appId))return null;//normally shouldn't be null
        AppIdentifier appIdBean = new AppIdentifier();
        String[] splitResult = appId.split("\\.");
        if(splitResult.length==3){
            //set optional marketplace scope
            appIdBean.setPrefix(splitResult[0]);
            appIdBean.setAppId(splitResult[1]);
            appIdBean.setVersion(splitResult[2]);
        }else if (splitResult.length == 2){
            appIdBean.setPrefix(splitResult[0]);
            appIdBean.setAppId(splitResult[1]);
        }else {
            return null;
        }
        return appIdBean;
    }
}
