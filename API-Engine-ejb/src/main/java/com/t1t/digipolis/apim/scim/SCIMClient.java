package com.t1t.digipolis.apim.scim;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.IConfig;
import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.typesafe.config.ConfigFactory;
/*import com.unboundid.scim.sdk.SCIMException;*/
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Created by michallispashidis on 10/10/15.
 */
public class SCIMClient {
    private static Logger _LOG = LoggerFactory.getLogger(SCIMClient.class.getName());
    @Inject private AppConfig config;

/*    public UserBean getUserInformation(String userId)throws SCIMException{
        return null;
    }
    public UserBean updateUserInformation(String userId)throws SCIMException{
        return null;
    }*/
}
