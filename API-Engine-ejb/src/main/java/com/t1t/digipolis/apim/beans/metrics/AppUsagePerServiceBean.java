package com.t1t.digipolis.apim.beans.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean returned for the "Usage per Service" metric for an app.
 *
 */
public class AppUsagePerServiceBean {

    private Map<String, Long> data = new HashMap<>();

    /**
     * Constructor.
     */
    public AppUsagePerServiceBean() {
    }

    /**
     * @return the data
     */
    public Map<String, Long> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<String, Long> data) {
        this.data = data;
    }

}
