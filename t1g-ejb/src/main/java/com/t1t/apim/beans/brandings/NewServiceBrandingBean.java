package com.t1t.apim.beans.brandings;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class NewServiceBrandingBean implements Serializable {

    private String name;

    public NewServiceBrandingBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewServiceBrandingBean that = (NewServiceBrandingBean) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NewServiceBrandingBean{" +
                "name='" + name + '\'' +
                '}';
    }
}