package com.t1t.digipolis.apim.scim;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.kong.model.SCIMUser;

/**
 * Created by michallispashidis on 2/12/15.
 */
public class SCIMUserFailSilent extends HystrixCommand<SCIMUser> {
    private final String userId;
    private final SCIMClient scimClient;

    protected SCIMUserFailSilent(SCIMClient scimClient, String userId){
        super(HystrixCommandGroupKey.Factory.asKey("SCIMUserGroup"));
        this.userId = userId;
        this.scimClient = scimClient;
    }

    @Override
    protected SCIMUser run() throws Exception {
        return scimClient.getUserInforamation(userId);
    }

    @Override
    protected SCIMUser getFallback() {
        SCIMUser emptySCIMUser = new SCIMUser();
        emptySCIMUser.setId(userId);
        emptySCIMUser.setUserName(userId);
        return emptySCIMUser;
    }
}
