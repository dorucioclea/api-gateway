package com.t1t.apim.scim;

import com.t1t.apim.AppConfig;
import com.t1t.apim.beans.idm.ExternalUserBean;
import com.t1t.apim.beans.scim.SCIMConfigBean;
import com.t1t.apim.core.IUserExternalInfoService;
import com.t1t.apim.exceptions.ExternalUserNotFoundException;
import com.t1t.kong.model.SCIMUser;
import com.t1t.kong.model.SCIMUserList;
import org.apache.commons.lang3.StringUtils;
import retrofit.RetrofitError;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by michallispashidis on 23/11/15.
 * We leave the code in order to support later on the roadmap a SCIM protocol for user info
 */
@ApplicationScoped
@Default
public class SCIMUserInfoProvider implements IUserExternalInfoService {
    private SCIMClient scimClient;
    @Inject private AppConfig config;

    @PostConstruct
    public void initSCIMCommunication() {
        //get SCIM configuration
        SCIMConfigBean scimConfigBean = new SCIMConfigBean();
        scimConfigBean.setEndpoint(config.getIDPSCIMEndpoint());
        scimConfigBean.setUsername(config.getIDPSCIMUserLogin());
        scimConfigBean.setPassword(config.getIDPSCIMUserPassword());
        scimClient = new SCIMServiceBuilder().getService(scimConfigBean, SCIMClient.class);
    }

    @Override
    public ExternalUserBean getUserInfoByQuery(String key, String value) throws ExternalUserNotFoundException {
        ExternalUserBean userBean = new ExternalUserBean();
        //FailSilent - command pattern
        StringBuilder filter = new StringBuilder(key).append(ISCIM.SCIM_FILTER_EQ).append(value);
        SCIMUserList userInformation = null;
        try {
            userInformation = new SCIMUserListFailSilent(filter.toString(), value, scimClient).execute();
        } catch (RetrofitError commerr) {
            throw new ExternalUserNotFoundException("User not found with filter:" + filter);
        }
        List<SCIMUser> userList = userInformation.getResources();
        if (userList != null && userList.size() > 0) {
            //we choose te first, normally you should look for a unique property
            userBean = mapToExternalUserBean(userList.get(0));
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

    @Override
    public ExternalUserBean getUserInfoByUserId(String userId) throws ExternalUserNotFoundException {
        //FailSilent - command pattern
        return mapToExternalUserBean(new SCIMUserFailSilent(scimClient,userId).execute());
    }

    /**
     * Maps the external user on an internal Userbean (DTO)
     *
     * @param refUser
     * @return
     */
    private ExternalUserBean mapToExternalUserBean(SCIMUser refUser) {
        ExternalUserBean userBean = new ExternalUserBean();
        //we choose te first, normally you should look for a unique property
        if (!StringUtils.isEmpty(refUser.getId()))
            userBean.setAccountId(refUser.getId());//user id for example user@domain.com
        if (!StringUtils.isEmpty(refUser.getDisplayName())) userBean.setName(refUser.getDisplayName());//full name
        if (!StringUtils.isEmpty(refUser.getUserName()))
            userBean.setUsername(refUser.getUserName());//user id without domain for example user
        if (refUser.getName()!=null){
            if (!StringUtils.isEmpty(refUser.getName().getGivenName()))
                userBean.setGivenname(refUser.getName().getGivenName());//givenname
            if (!StringUtils.isEmpty(refUser.getName().getFamilyName()))
                userBean.setSurname(refUser.getName().getFamilyName());//surname
        }
        return userBean;
    }
}
