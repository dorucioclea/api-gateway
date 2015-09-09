package com.t1t.digipolis.apim.beans.authorization;

import java.io.Serializable;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerBean implements Serializable {
    private String uniqueUserName;//SAML2 nameid
    private String token;

    public AuthConsumerBean() {
    }

    public String getUniqueUserName() {
        return uniqueUserName;
    }

    public void setUniqueUserName(String uniqueUserName) {
        this.uniqueUserName = uniqueUserName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthConsumerBean)) return false;

        AuthConsumerBean that = (AuthConsumerBean) o;

        if (!uniqueUserName.equals(that.uniqueUserName)) return false;
        return !(token != null ? !token.equals(that.token) : that.token != null);

    }

    @Override
    public int hashCode() {
        int result = uniqueUserName.hashCode();
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AuthConsumer{" +
                "uniqueUserName='" + uniqueUserName + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
