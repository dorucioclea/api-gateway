package com.t1t.apim.beans.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean returned for the "Usage per Service" metric for an app.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUsagePerServiceBean implements Serializable {

    private Map<String, AppUsageBean> data = new HashMap<>();

    public Map<String, AppUsageBean> getData() {
        return data;
    }

    public void setData(Map<String, AppUsageBean> data) {
        this.data = data;
    }
}
