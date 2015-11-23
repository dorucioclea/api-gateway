package com.t1t.digipolis.apim.scim;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.IConfig;
import com.t1t.digipolis.apim.beans.idm.UserBean;
import com.t1t.digipolis.kong.model.KongPluginOAuthConsumerResponseList;
import com.t1t.digipolis.kong.model.SCIMUserList;
import com.typesafe.config.ConfigFactory;
/*import com.unboundid.scim.sdk.SCIMException;*/
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.http.GET;
import retrofit.http.Query;

import javax.inject.Inject;

/**
 * Created by michallispashidis on 10/10/15.
 */
public interface SCIMClient {
    @GET("/Users")SCIMUserList getUserInformation(@Query("filter")String userFilter);
}
