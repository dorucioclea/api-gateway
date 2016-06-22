package com.t1t.digipolis.apim.beans.summary;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Models the entire API registry for a single application version.  This is typically
 * used to get a list of all APIs that can be consumed by a single version of a single
 * application.  Most importantly it includes the live endpoint information and API
 * keys for all of the app's service contracts/APIs.
 *
 */
@XmlRootElement(name = "apiRegistry")
@XmlAccessorType(XmlAccessType.FIELD)
public class ApiRegistryBean implements Serializable {

    private static final long serialVersionUID = 7369169626368271089L;

    private List<ApiEntryBean> apis = new ArrayList<>();

    /**
     * Constructor.
     */
    public ApiRegistryBean() {
    }

    /**
     * @return the apis
     */
    public List<ApiEntryBean> getApis() {
        return apis;
    }

    /**
     * @param apis the apis to set
     */
    public void setApis(List<ApiEntryBean> apis) {
        this.apis = apis;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        final int maxLen = 10;
        return "ApiRegistryBean [apis="
                + (apis != null ? apis.subList(0, Math.min(apis.size(), maxLen)) : null) + "]";
    }
}
