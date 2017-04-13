package com.t1t.apim.beans.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceMetricsBean implements Serializable {

    private List<JSONObject> serviceData;
    private Map<ApplicationVersionSummaryBean, List<JSONObject>> applicationData;

    public List<JSONObject> getServiceData() {
        return serviceData;
    }

    public void setServiceData(List<JSONObject> serviceData) {
        this.serviceData = serviceData;
    }

    public Map<ApplicationVersionSummaryBean, List<JSONObject>> getApplicationData() {
        return applicationData;
    }

    public void setApplicationData(Map<ApplicationVersionSummaryBean, List<JSONObject>> applicationData) {
        this.applicationData = applicationData;
    }

    @Override
    public String toString() {
        return "ServiceMetricsBean{" +
                "serviceData=" + serviceData +
                ", applicationData=" + applicationData +
                '}';
    }
}