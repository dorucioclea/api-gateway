package com.t1t.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.kong.model.MetricsResponseSummaryList;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsResponseSummaryFailSilent extends HystrixCommand<MetricsResponseSummaryList> {
    private final MetricsClient client;

    private final String organizationId;
    private final String serviceId;
    private final String version;
    private final String from;
    private final String to;


    public MetricsResponseSummaryFailSilent(MetricsClient client, String organizationId, String serviceId, String version, String from, String to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsResponseSummary"), timeout != null ? timeout : 200);
        this.client = client;

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
        this.from = from;
        this.to = to;
    }

    @Override
    protected MetricsResponseSummaryList run() {
        return client.getServiceResponseSummaryFromTo(organizationId, serviceId, version, from, to);
    }

    @Override
    protected MetricsResponseSummaryList getFallback() {
        return null;
    }
}