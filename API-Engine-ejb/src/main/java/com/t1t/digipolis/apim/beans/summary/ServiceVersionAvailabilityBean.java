package com.t1t.digipolis.apim.beans.summary;
import com.t1t.digipolis.apim.beans.services.AvailabilityBean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by michallispashidis on 12/02/16.
 */
public class ServiceVersionAvailabilityBean implements Serializable {
    private Map<String,AvailabilityBean> availableMarketplaces;

    public Map<String, AvailabilityBean> getAvailableMarketplaces() {
        return availableMarketplaces;
    }

    public void setAvailableMarketplaces(Map<String, AvailabilityBean> availableMarketplaces) {
        this.availableMarketplaces = availableMarketplaces;
    }

    @Override
    public String toString() {
        return "ServiceVersionAvailabilityBean{" +
                "availableMarketplaces=" + availableMarketplaces +
                '}';
    }
}
