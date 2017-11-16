package com.t1t.apim.beans.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.summary.ServiceVersionSummaryBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean returned for the "Usage per Service" metric for an app.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUsagePerServiceBean implements Serializable {

    private Map<ServiceVersionSummaryBean, ServiceMetricsBean> data = new HashMap<>();

    public Map<ServiceVersionSummaryBean, ServiceMetricsBean> getData() {
        return data;
    }

    public void setData(Map<ServiceVersionSummaryBean, ServiceMetricsBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AppUsagePerServiceBean{" +
                "data=" + data +
                '}';
    }
}
