package com.t1t.digipolis.apim.beans.metrics;

import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean returned for the "Usage per Service" metric for an app.
 *
 */
public class AppUsagePerServiceBean {

    private Map<String, MetricsConsumerUsageList> data = new HashMap<>();

    /**
     * Constructor.
     */
    public AppUsagePerServiceBean() {
    }

    public Map<String, MetricsConsumerUsageList> getData() {
        return data;
    }

    public void setData(Map<String, MetricsConsumerUsageList> data) {
        this.data = data;
    }
}
