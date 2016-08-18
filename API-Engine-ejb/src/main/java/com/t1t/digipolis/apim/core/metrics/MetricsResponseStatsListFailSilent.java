package com.t1t.digipolis.apim.core.metrics;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.apim.beans.metrics.HistogramIntervalType;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import org.joda.time.DateTime;

import static org.bouncycastle.crypto.tls.ConnectionEnd.client;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class MetricsResponseStatsListFailSilent extends AbstractHystrixMetricsCommand<MetricsResponseStatsList> {

    private final String organizationId;
    private final String serviceId;
    private final String version;
    private final HistogramIntervalType interval;
    private final DateTime from;
    private final DateTime to;


    public MetricsResponseStatsListFailSilent(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to, Integer timeout) {
        super(HystrixCommandGroupKey.Factory.asKey("MetricsResponseStatsList"), timeout != null ? timeout : 200);

        this.organizationId = organizationId;
        this.serviceId = serviceId;
        this.version = version;
        this.interval = interval;
        this.from = from;
        this.to = to;
    }

    @Override
    protected MetricsResponseStatsList run() {
        return this.getSpi().getResponseStats(organizationId, serviceId, version, interval, from, to);
    }

    @Override
    protected MetricsResponseStatsList getFallback() {
        return null;
    }
}