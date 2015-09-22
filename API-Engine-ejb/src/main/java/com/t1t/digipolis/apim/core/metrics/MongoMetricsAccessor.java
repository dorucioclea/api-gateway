package com.t1t.digipolis.apim.core.metrics;

import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.core.IMetricsAccessor;
import org.joda.time.DateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;

/**
 * Created by michallispashidis on 12/09/15.
 */
@ApplicationScoped
@Default
public class MongoMetricsAccessor implements IMetricsAccessor {
    @Override
    public UsageHistogramBean getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public ResponseStatsHistogramBean getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public ResponseStatsSummaryBean getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public AppUsagePerServiceBean getAppUsageForService(String organizationId, String applicationId, String version, DateTime from, DateTime to, String consumerId) {
        return null;
    }


}
