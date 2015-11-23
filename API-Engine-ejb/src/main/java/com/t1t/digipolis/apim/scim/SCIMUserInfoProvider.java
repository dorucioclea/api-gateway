package com.t1t.digipolis.apim.scim;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.idm.ExternalUserBean;
import com.t1t.digipolis.apim.beans.scim.SCIMConfigBean;
import com.t1t.digipolis.apim.core.IUserExternalInfoService;
import com.t1t.digipolis.apim.exceptions.ExternalUserNotFoundException;
import com.t1t.digipolis.kong.model.SCIMUser;
import com.t1t.digipolis.kong.model.SCIMUserList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by michallispashidis on 23/11/15.
 */
@ApplicationScoped
@Default
public class SCIMUserInfoProvider implements IUserExternalInfoService {
    private SCIMServiceBuilder scimServiceBuilder;
    private SCIMClient scimClient;
    @Inject private AppConfig config;

    @PostConstruct
    public void initSCIMCommunication(){
        //get SCIM configuration
        SCIMConfigBean scimConfigBean = new SCIMConfigBean();
        scimConfigBean.setEndpoint(config.getIDPSCIMEndpoint());
        scimConfigBean.setUsername(config.getIDPSCIMUserLogin());
        scimConfigBean.setPassword(config.getIDPSCIMUserPassword());
        scimServiceBuilder = new SCIMServiceBuilder();
        scimClient = scimServiceBuilder.getService(scimConfigBean,SCIMClient.class);
    }
    
    @Override
    public ExternalUserBean getUserInfo(String key, String value) throws ExternalUserNotFoundException {
        ExternalUserBean userBean = new ExternalUserBean();
        StringBuilder filter = new StringBuilder(key).append(ISCIM.SCIM_FILTER_EQ).append(value);
        SCIMUserList userInformation = scimClient.getUserInformation(filter.toString());
        List<SCIMUser> userList = new ArrayList<>();
        if(userList!=null && userList.size()>0){
            //we choose te first, normally you should look for a unique property
            SCIMUser refUser = userList.get(0);
            userBean.setName(refUser.getUserName());
            userBean.setEmails(refUser.getEmails());
            userBean.setGivenname(refUser.getName().getGivenName());
            userBean.setSurname(refUser.getName().getFamilyName());
            userBean.setUsername(refUser.getDisplayName());
            userBean.setCreatedon(refUser.getMeta().getCreated());
            userBean.setLastModified(refUser.getMeta().getLastModified());
        }
        return userBean;
    }
}
