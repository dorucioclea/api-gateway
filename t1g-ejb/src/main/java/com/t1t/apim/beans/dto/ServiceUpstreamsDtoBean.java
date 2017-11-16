package com.t1t.apim.beans.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.services.SchemeType;
import com.t1t.apim.beans.services.ServiceUpstreamTargetBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceUpstreamsDtoBean implements Serializable {

    private SchemeType scheme;
    private List<ServiceUpstreamTargetBean> targets;
    private String path;

    public SchemeType getScheme() {
        return scheme;
    }

    public void setScheme(SchemeType scheme) {
        this.scheme = scheme;
    }

    public List<ServiceUpstreamTargetBean> getTargets() {
        return targets;
    }

    public void setTargets(List<ServiceUpstreamTargetBean> targets) {
        this.targets = targets;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceUpstreamsDtoBean)) return false;

        ServiceUpstreamsDtoBean that = (ServiceUpstreamsDtoBean) o;

        if (scheme != that.scheme) return false;
        if (targets != null ? !targets.equals(that.targets) : that.targets != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = scheme != null ? scheme.hashCode() : 0;
        result = 31 * result + (targets != null ? targets.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceUpstreamDtoBean{" +
                "scheme=" + scheme +
                ", targets=" + targets +
                ", path='" + path + '\'' +
                '}';
    }
}