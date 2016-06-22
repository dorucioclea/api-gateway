package com.t1t.digipolis.apim.beans.apps;

import java.io.Serializable;

/**
 * Created by michallispashidis on 29/11/15.
 */
public class AppIdentifier implements Serializable {
    private String scope;
    private String appId;
    private String version;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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

        if (!scope.equals(that.scope)) return false;
        if (!appId.equals(that.appId)) return false;
        return version.equals(that.version);

    }

    @Override
    public int hashCode() {
        int result = scope.hashCode();
        result = 31 * result + appId.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AppIdentifier{" +
                "scope='" + scope + '\'' +
                ", appId='" + appId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
