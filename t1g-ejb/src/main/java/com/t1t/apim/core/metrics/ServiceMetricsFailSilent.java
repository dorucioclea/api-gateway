package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.apim.beans.metrics.ServiceMetricsBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;
import org.joda.time.DateTime;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ServiceMetricsFailSilent extends AbstractHystrixMetricsCommand<ServiceMetricsBean> {

    private ServiceVersionBean service;
    private List<ApplicationVersionSummaryBean> applications;
    private DateTime from;
    private DateTime to;


    public ServiceMetricsFailSilent(ServiceVersionBean service, List<ApplicationVersionSummaryBean> applications, DateTime from, DateTime to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("ServiceMetrics"), timeout != null ? timeout : 200);

        this.service = service;
        this.applications = applications;
        this.from = from;
        this.to = to;;
    }

    @Override
    protected ServiceMetricsBean run() throws Exception {
        return null;
    }

    @Override
    protected ServiceMetricsBean getFallback() {
        return null;
    }
}