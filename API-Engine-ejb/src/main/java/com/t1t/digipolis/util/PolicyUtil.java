package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.iprestriction.IPRestrictionBean;
import com.t1t.digipolis.apim.beans.iprestriction.IPRestrictionFlavor;
import com.t1t.digipolis.kong.model.KongPluginIPRestriction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by michallispashidis on 14/02/16.
 */
public class PolicyUtil {
    public static KongPluginIPRestriction createDefaultIPRestriction(IPRestrictionFlavor flavor, List<? extends IPRestrictionBean> ipRestrictionBeanList){
        KongPluginIPRestriction kpip = null;
        List<String> transformedIPList = null;
        if(ipRestrictionBeanList!=null && ipRestrictionBeanList.size()>0){
             transformedIPList = ipRestrictionBeanList.stream().map(wt -> wt.getNetwValue()).collect(Collectors.toList());
        }
        if(transformedIPList!=null && transformedIPList.size()>0){
            kpip = new KongPluginIPRestriction();
            switch (flavor){
                case WHITELIST:kpip.setWhitelist(transformedIPList);break;
                case BLACKLIST:kpip.setBlacklist(transformedIPList);break;
            }
            return kpip;
        }else return null;
    }
}
