package com.t1t.digipolis.apim.beans.authorization;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerRequestBasicAuthBean extends AbstractAuthConsumerRequest {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthConsumerRequestBasicAuthBean{" +
                "password='" + password + '\'' +
                '}';
    }
}
