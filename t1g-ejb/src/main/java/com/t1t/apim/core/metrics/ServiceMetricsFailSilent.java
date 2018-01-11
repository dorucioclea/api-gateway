package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.apim.beans.metrics.ServiceMetricsBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import com.t1t.apim.beans.summary.ApplicationVersionSummaryBean;
import com.t1t.apim.exceptions.ExceptionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
public class ServiceMetricsFailSilent extends AbstractHystrixMetricsCommand<ServiceMetricsBean> {

    private static final Logger log = LoggerFactory.getLogger(ServiceMetricsFailSilent.class);

    private ServiceVersionBean service;
    private List<ApplicationVersionSummaryBean> applications;
    private DateTime from;
    private DateTime to;


    public ServiceMetricsFailSilent(ServiceVersionBean service, List<ApplicationVersionSummaryBean> applications, DateTime from, DateTime to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("ServiceMetrics"), timeout != null ? timeout : 5000);

        setTimeout(timeout != null ? timeout : 5000);
        this.service = service;
        this.applications = applications;
        this.from = from;
        this.to = to;
    }

    @Override
    protected ServiceMetricsBean run() throws Exception {
        return this.getSpi().getServiceMetrics(service, applications, from, to);
    }

    @Override
    protected ServiceMetricsBean getFallback() {
        log.info("Hystrix fallback method called for Service Uptime Metrics");
        ServiceMetricsBean metrics = new ServiceMetricsBean();
        metrics.setException(ExceptionFactory.metricsQueryTimeOutException(getTimeout()));
        return metrics;
    }
}