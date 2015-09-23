package com.t1t.digipolis.apim.beans.metrics;

/**
 * Created by michallispashidis on 23/09/15.
 */
public class ServiceMarketInfo {
    private int uptime;
    private int followers;
    private int developers;

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

    public int getDevelopers() {
        return developers;
    }

    public void setDevelopers(int developers) {
        this.developers = developers;
    }

    @Override
    public String toString() {
        return "ServiCeMarketInfo{" +
                "uptime=" + uptime +
                ", followers=" + followers +
                ", developers=" + developers +
                '}';
    }
}
