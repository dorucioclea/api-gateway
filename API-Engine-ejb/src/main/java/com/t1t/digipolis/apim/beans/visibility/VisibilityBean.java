package com.t1t.digipolis.apim.beans.visibility;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * The Visibility bean tags labels (named codes) to a service. In a marketplace you can select the tags you want to activate.
 * For example, let's introduce tags, 'internal', 'external'.
 * For an interal marketplace, only service versions tagged with 'internal' will appear.
 * For an external marketplace, only service versions tagged with 'external' will appear.
 * You can activate multiple tags for a marketplace. The tag denotes visibility in the context of a marketplace.
 * The 'show' property must be set to true for a service to appear in the API explorer of a marketplace. When set to false,
 * the service will be exposed on the gateway, but will not be visible on the marketplace.
 */
@Embeddable
public class VisibilityBean implements Serializable {
    @Column(name = "code", nullable=false)
    private String code;
    @Column(nullable=false)
    private Boolean show;

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
                '}';
    }
}
