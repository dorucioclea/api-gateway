package com.t1t.digipolis.apim.beans.apps;

import java.io.Serializable;

/**
 * Created by michallispashidis on 29/11/15.
 */
public class AppIdentifier implements Serializable {
    private String orgId;
    private String appId;
    private String version;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppIdentifier)) return false;

        AppIdentifier that = (AppIdentifier) o;

        if (!orgId.equals(that.orgId)) return false;
        if (!appId.equals(that.appId)) return false;
        return version.equals(that.version);

    }

    @Override
    public int hashCode() {
        int result = orgId.hashCode();
        result = 31 * result + appId.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AppIdentifier{" +
                "orgId='" + orgId + '\'' +
                ", appId='" + appId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
