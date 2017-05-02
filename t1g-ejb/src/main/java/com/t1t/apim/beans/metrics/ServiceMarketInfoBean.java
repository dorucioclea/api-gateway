package com.t1t.apim.beans.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by michallispashidis on 23/09/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceMarketInfoBean implements Serializable {
    private Integer uptime;
    private Integer followers;
    private Integer distinctUsers;

    public Integer getUptime() {
        return uptime;
    }

    public void setUptime(Integer uptime) {
        this.uptime = uptime;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getDistinctUsers() {
        return distinctUsers;
    }

    public void setDistinctUsers(Integer distinctUsers) {
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
