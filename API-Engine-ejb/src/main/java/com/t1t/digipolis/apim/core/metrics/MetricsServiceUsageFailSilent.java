package com.t1t.digipolis.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.apim.exceptions.ExceptionFactory;
import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;
import com.t1t.digipolis.kong.model.MetricsUsageList;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsServiceUsageFailSilent extends HystrixCommand<MetricsUsageList> {
    private final MetricsClient client;

    private final String organizationId;
    private final String serviceId;
    private final String version;
    private final String interval;
    private final String from;
    private final String to;


    public MetricsServiceUsageFailSilent(MetricsClient client, String organizationId, String serviceId, String version, String interval, String from, String to) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsServiceUsage"), 2000);
        this.client = client;

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
        this.interval = interval;
        this.from = from;
        this.to = to;
    }

    @Override
    protected MetricsUsageList run() {
        return client.getServiceUsageFromToInterval(organizationId, serviceId, version, interval, from, to);
    }

    @Override
    protected MetricsUsageList getFallback() {
        return null;
    }
}