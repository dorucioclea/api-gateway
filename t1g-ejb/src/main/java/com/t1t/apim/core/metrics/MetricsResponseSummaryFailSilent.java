package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.kong.model.MetricsResponseSummaryList;
import org.joda.time.DateTime;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsResponseSummaryFailSilent extends AbstractHystrixMetricsCommand<MetricsResponseSummaryList> {

    private final String organizationId;
    private final String serviceId;
    private final String version;
    private final DateTime from;
    private final DateTime to;


    public MetricsResponseSummaryFailSilent(String organizationId, String serviceId, String version, DateTime from, DateTime to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsResponseSummary"), timeout != null ? timeout : 200);

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
        this.from = from;
        this.to = to;
    }

    @Override
    protected MetricsResponseSummaryList run() {
        return this.getSpi().getResponseStatsSummary(organizationId, serviceId, version, from, to);
    }

    @Override
    protected MetricsResponseSummaryList getFallback() {
        return null;
    }
}