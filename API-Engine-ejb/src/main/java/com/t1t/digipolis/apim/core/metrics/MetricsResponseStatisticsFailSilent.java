package com.t1t.digipolis.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsResponseStatisticsFailSilent extends HystrixCommand<MetricsResponseStatsList> {
    private final MongoDBMetricsClient client;

    private final String organizationId;
    private final String serviceId;
    private final String version;
    private final String interval;
    private final String from;
    private final String to;


    public MetricsResponseStatisticsFailSilent(MongoDBMetricsClient client, String organizationId, String serviceId, String version, String interval, String from, String to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsResponseStatistics"), timeout != null ? timeout : 200);
        this.client = client;

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
        this.interval = interval;
        this.from = from;
        this.to = to;
    }

    @Override
    protected MetricsResponseStatsList run() {
        return client.getResponseStats(organizationId, serviceId, version, interval, from, to);
    }

    @Override
    protected MetricsResponseStatsList getFallback() {
        return null;
    }
}