package com.t1t.digipolis.apim.core.metrics;

import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.core.IMetricsAccessor;
import org.joda.time.DateTime;

/**
 * Created by michallispashidis on 12/09/15.
 */
public class MongoMetricsAccessor implements IMetricsAccessor {
    @Override
    public UsageHistogramBean getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public UsagePerAppBean getUsagePerApp(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public UsagePerPlanBean getUsagePerPlan(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
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
    public ResponseStatsPerAppBean getResponseStatsPerApp(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public ResponseStatsPerPlanBean getResponseStatsPerPlan(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public AppUsagePerServiceBean getAppUsagePerService(String organizationId, String applicationId, String version, DateTime from, DateTime to) {
        return null;
    }
}
