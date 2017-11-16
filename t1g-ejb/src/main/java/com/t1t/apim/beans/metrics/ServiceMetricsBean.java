package com.t1t.apim.beans.metrics;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceMetricsBean implements Serializable {

    private List<String> serviceData;
    private Map<ApplicationVersionSummaryBean, List<String>> applicationData;

    public List<String> getServiceData() {
        return serviceData;
    }

    public void setServiceData(List<String> serviceData) {
        this.serviceData = serviceData;
    }

    public Map<ApplicationVersionSummaryBean, List<String>> getApplicationData() {
        return applicationData;
    }

    public void setApplicationData(Map<ApplicationVersionSummaryBean, List<String>> applicationData) {
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