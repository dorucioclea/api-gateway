package com.t1t.apim.scim;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.kong.model.SCIMUser;
import com.t1t.kong.model.SCIMUserList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michallispashidis on 2/12/15.
 */
public class SCIMUserListFailSilent extends HystrixCommand<SCIMUserList> {
    private final String filter;
    private final String value;
    private final SCIMClient scimClient;

    protected SCIMUserListFailSilent(String filter, String value, SCIMClient scimClient){
        super(HystrixCommandGroupKey.Factory.asKey("SCIMUserListGroup"));
        this.filter=filter;
        this.value=value;
        this.scimClient=scimClient;
    }

    @Override
    protected SCIMUserList run() throws Exception {
        return scimClient.getUserInformation(filter);
    }

    @Override
    protected SCIMUserList getFallback() {
        SCIMUserList scimUserListObject = new SCIMUserList();
        SCIMUser scimUser = new SCIMUser();
        scimUser.setId(value);
        scimUser.setUserName(value);
        List<SCIMUser>scimUserList = new ArrayList<>();
        scimUserList.add(scimUser);
        scimUserListObject.setResources(scimUserList);
        return scimUserListObject;
    }
}
