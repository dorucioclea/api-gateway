package com.t1t.digipolis.apim.scim;

import com.t1t.digipolis.apim.AppConfig;
import com.t1t.digipolis.apim.beans.idm.ExternalUserBean;
import com.t1t.digipolis.apim.beans.scim.SCIMConfigBean;
import com.t1t.digipolis.apim.core.IUserExternalInfoService;
import com.t1t.digipolis.apim.exceptions.ExternalUserNotFoundException;
import com.t1t.digipolis.kong.model.SCIMUser;
import com.t1t.digipolis.kong.model.SCIMUserList;
import org.apache.commons.lang3.StringUtils;
import retrofit.RetrofitError;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
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
    public ExternalUserBean getUserInfoByQuery(String key, String value) throws ExternalUserNotFoundException {
        ExternalUserBean userBean = new ExternalUserBean();
        StringBuilder filter = new StringBuilder(key).append(ISCIM.SCIM_FILTER_EQ).append(value);
        SCIMUserList userInformation = null;
        try{
            userInformation = scimClient.getUserInformation(filter.toString());
        }catch (RetrofitError commerr){
            throw new ExternalUserNotFoundException("User not found with filter:"+filter);
        }
        List<SCIMUser> userList = userInformation.getResources();
        if(userList!=null && userList.size()>0){
            //we choose te first, normally you should look for a unique property
            SCIMUser refUser = userList.get(0);
            if(!StringUtils.isEmpty(refUser.getDisplayName()))userBean.setName(refUser.getDisplayName());
            if(!StringUtils.isEmpty(refUser.getId())){
                userBean.setUsername(refUser.getId());//case of correct id set as claim
            }else{
                userBean.setUsername(refUser.getUserName());//default case
            }
            if(refUser.getEmails()!=null)userBean.setEmails(refUser.getEmails());
            if(!StringUtils.isEmpty(refUser.getName().getGivenName()))userBean.setGivenname(refUser.getName().getGivenName());
            if(!StringUtils.isEmpty(refUser.getName().getFamilyName()))userBean.setSurname(refUser.getName().getFamilyName());
        }
        return userBean;
    }

    @Override
    public ExternalUserBean getUserInfoByUsername(String username) throws ExternalUserNotFoundException {
        return getUserInfoByQuery(ISCIM.SCIM_FILTER_KEY_USERNAME, username);
    }

    @Override
    public ExternalUserBean getUserInfoByMail(String email) throws ExternalUserNotFoundException {
        return getUserInfoByQuery(ISCIM.SCIM_FILTER_KEY_EMAIL, email);
    }
}
