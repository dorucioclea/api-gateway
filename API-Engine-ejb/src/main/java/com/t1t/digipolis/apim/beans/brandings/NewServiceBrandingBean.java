package com.t1t.digipolis.apim.beans.brandings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class NewServiceBrandingBean {

    private String name;
    private String basePath;

    public NewServiceBrandingBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewServiceBrandingBean that = (NewServiceBrandingBean) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return basePath != null ? basePath.equals(that.basePath) : that.basePath == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (basePath != null ? basePath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewServiceBrandingBean{" +
                "name='" + name + '\'' +
                ", basePath='" + basePath + '\'' +
                '}';
    }
}