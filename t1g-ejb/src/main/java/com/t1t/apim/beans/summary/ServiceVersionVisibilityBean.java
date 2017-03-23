package com.t1t.apim.beans.summary;

import com.t1t.apim.beans.visibility.VisibilityBean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by michallispashidis on 12/02/16.
 */
public class ServiceVersionVisibilityBean implements Serializable {
    private Map<String,VisibilityBean> availableMarketplaces;

    public Map<String, VisibilityBean> getAvailableMarketplaces() {
        return availableMarketplaces;
    }

    public void setAvailableMarketplaces(Map<String, VisibilityBean> availableMarketplaces) {
        this.availableMarketplaces = availableMarketplaces;
    }

    @Override
    public String toString() {
        return "ServiceVersionAvailabilityBean{" +
                "availableMarketplaces=" + availableMarketplaces +
                '}';
    }
}
