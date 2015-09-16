package com.t1t.digipolis.apim.beans.authorization;

import java.io.Serializable;

/**
 * Created by michallispashidis on 9/09/15.
 */
public class AuthConsumerBean implements Serializable {
    //generated:: org.app.version.customid
    private String userId;
    private String customId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
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
        if (o == null || getClass() != o.getClass()) return false;

        AuthConsumerBean that = (AuthConsumerBean) o;

        if (!userId.equals(that.userId)) return false;
        return customId.equals(that.customId);

    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + customId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AuthConsumerBean{" +
                "userId='" + userId + '\'' +
                ", customId='" + customId + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
