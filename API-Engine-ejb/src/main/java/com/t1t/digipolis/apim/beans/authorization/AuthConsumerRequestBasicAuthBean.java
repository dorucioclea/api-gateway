package com.t1t.digipolis.apim.beans.authorization;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerRequestBasicAuthBean extends AbstractAuthConsumerRequest {
    private String userLoginName;
    private String userLoginPassword;

    public String getUserLoginPassword() {
        return userLoginPassword;
    }

    public void setUserLoginPassword(String userLoginPassword) {
        this.userLoginPassword = userLoginPassword;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }
}
