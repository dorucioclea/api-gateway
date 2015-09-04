package com.t1t.digipolis.apim.beans.user;

import java.io.Serializable;

/**
 * Created by michallispashidis on 4/09/15.
 */
public class LoginResponseBean implements Serializable {
    private String bearer;

    public LoginResponseBean() {
    }

    public LoginResponseBean(String bearer) {
        this.bearer = bearer;
    }

    public String getBearer() {
        return bearer;
    }

    public void setBearer(String bearer) {
        this.bearer = bearer;
    }

    @Override
    public String toString() {
        return "LoginResponseBean{" +
                "bearer='" + bearer + '\'' +
                '}';
    }
}
