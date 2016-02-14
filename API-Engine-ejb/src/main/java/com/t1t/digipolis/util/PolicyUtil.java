package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.iprestriction.BlacklistBean;
import com.t1t.digipolis.apim.beans.iprestriction.WhitelistBean;
import com.t1t.digipolis.kong.model.KongPluginIPRestriction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 14/02/16.
 */
public class PolicyUtil {
    public static KongPluginIPRestriction createDefaultIPRestriction(List<WhitelistBean> whitelistBeanList, List<BlacklistBean> blacklistBeanList){
        KongPluginIPRestriction kpip = null;
        List<String> transformedWhitelists = null;
        List<String> transformedBlacklists = null;
        if(whitelistBeanList!=null && whitelistBeanList.size()>0){
             transformedWhitelists = whitelistBeanList.stream().map(wt -> wt.getNetwValue()).collect(Collectors.toList());
        }
        if(blacklistBeanList!=null && blacklistBeanList.size()>0){
           transformedBlacklists = blacklistBeanList.stream().map(bl -> bl.getNetwValue()).collect(Collectors.toList());
        }
        if(transformedBlacklists!=null && transformedWhitelists!=null){
            kpip = new KongPluginIPRestriction();
            if(transformedBlacklists.size()>0)kpip.setBlacklist(transformedBlacklists);
            if(transformedWhitelists.size()>0)kpip.setWhitelist(transformedWhitelists);
        }
        return kpip;
    }
}
