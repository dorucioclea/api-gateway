package com.t1t.digipolis.apim.beans.user;

import java.io.Serializable;

/**
 * Created by michallispashidis on 4/09/15.
 */
public class LoginRequestBean implements Serializable {
    private String login;
    private String password;

    public LoginRequestBean() {
    }

    public LoginRequestBean(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "login='" + login + '\'' +
                ", password='"+ "*****" + '\'' +
                '}';
    }
}
