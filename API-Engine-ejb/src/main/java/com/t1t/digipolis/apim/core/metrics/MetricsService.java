package com.t1t.digipolis.apim.core.metrics;

import com.t1t.digipolis.apim.beans.metrics.HistogramIntervalType;
import com.t1t.digipolis.apim.beans.metrics.ServiceMarketInfo;
import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import com.t1t.digipolis.kong.model.MetricsResponseSummaryList;
import com.t1t.digipolis.kong.model.MetricsUsageList;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.util.ServiceLoader;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@ApplicationScoped
@Default
public class MetricsService {

    private static final Logger _LOG = LoggerFactory.getLogger(MetricsService.class);
    private static ServiceLoader<MetricsClient> loader;

    {
        loader = ServiceLoader.load(MetricsClient.class);
    }

    public MetricsUsageList getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
    }

    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return null;
    }

    public MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return null;
    }

    public MetricsConsumerUsageList getAppUsageForService(String organizationId, String applicationId, String version, HistogramIntervalType interval, DateTime from, DateTime to, String consumerId) {
        return null;
    }
    
    public ServiceMarketInfo getServiceMarketInfo(String organizationId, String serviceId, String version) {
        return null;
    }
}