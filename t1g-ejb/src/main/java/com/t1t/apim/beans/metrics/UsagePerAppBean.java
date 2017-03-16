package com.t1t.apim.beans.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean returned for the "Usage per App" metric.
 *
 */
public class UsagePerAppBean {

    private Map<String, Long> data = new HashMap<>();

    /**
     * Constructor.
     */
    public UsagePerAppBean() {
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
