package com.t1t.digipolis.apim.beans.scim;

import java.io.Serializable;

/**
 * Created by michallispashidis on 29/11/15.
 */
public class ExternalUserRequest implements Serializable{
    private String userName;
    private String userMail;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    @Override
    public String toString() {
        return "ExternalUserRequest{" +
                "userName='" + userName + '\'' +
                ", userMail='" + userMail + '\'' +
                '}';
    }
}
