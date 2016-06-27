package com.t1t.digipolis.apim.beans.visibility;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * The Visibility bean tags (named codes) a service. The tag is a prefix of an existing managed app.
 * The Visibility bean is a talbe denoting visibility of a service version for different marketplaces.
 * Each marketplace consumer is a managed applciations, with a predefined 'prefix'.
 * For example, let's introduce tags, 'int', 'ext'; which are prefixes for internal and external marketplace.
 * For an interal marketplace, only service versions tagged with 'int' will appear.
 * For an external marketplace, only service versions tagged with 'ext' will appear.
 * The tag denotes visibility in the context of a marketplace.
 * The 'show' property must be set to true for a service to appear in the API explorer of a marketplace. When set to false,
 * The service will be exposed on the gateway, but will not be visible on the marketplace.
 */
@Embeddable
public class VisibilityBean implements Serializable {
    @Column(name = "code", nullable=false)
    private String code;
    @Column(nullable=false)
    private Boolean show;
    @Transient
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
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
        if (!(o instanceof VisibilityBean)) return false;

        VisibilityBean that = (VisibilityBean) o;

        if (!code.equals(that.code)) return false;
        return show.equals(that.show);

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + show.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "VisibilityBean{" +
                "code='" + code + '\'' +
                ", show=" + show +
                ", name='" + name + '\'' +
                '}';
    }
}
