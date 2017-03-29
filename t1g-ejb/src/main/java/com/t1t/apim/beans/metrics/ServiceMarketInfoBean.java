package com.t1t.apim.beans.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by michallispashidis on 23/09/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceMarketInfoBean implements Serializable {
    private int uptime;
    private int followers;
    private int distinctUsers;

    public int getUptime() {
        return uptime;
    }

    public void setUptime(int uptime) {
        this.uptime = uptime;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getDistinctUsers() {
        return distinctUsers;
    }

    public void setDistinctUsers(int distinctUsers) {
        this.distinctUsers = distinctUsers;
    }

    @Override
    public String toString() {
        return "ServiCeMarketInfo{" +
                "uptime=" + uptime +
                ", followers=" + followers +
                ", distinctUsers=" + distinctUsers +
                '}';
    }
}
