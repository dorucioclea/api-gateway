package com.t1t.digipolis.apim.core.noop;

import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.core.IMetricsAccessor;
import org.joda.time.DateTime;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;

/**
 * A no-op implementaiton of {@link com.t1t.digipolis.apim.core.IMetricsAccessor}.  Useful for situations where
 * no metrics are available.
 *
 */
@ApplicationScoped @Default
public class NoOpMetricsAccessor implements IMetricsAccessor {

    /**
     * Constructor.
     */
    public NoOpMetricsAccessor() {
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getUsage(String, String, String, HistogramIntervalType, DateTime, DateTime)
     */
    @Override
    public UsageHistogramBean getUsage(String organizationId, String serviceId, String version,
            HistogramIntervalType interval, DateTime from, DateTime to) {
        UsageHistogramBean rval = new UsageHistogramBean();
        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getUsagePerApp(String, String, String, DateTime, DateTime)
     */
    @Override
    public UsagePerAppBean getUsagePerApp(String organizationId, String serviceId, String version,
            DateTime from, DateTime to) {
        UsagePerAppBean rval = new UsagePerAppBean();
        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getUsagePerPlan(String, String, String, DateTime, DateTime)
     */
    @Override
    public UsagePerPlanBean getUsagePerPlan(String organizationId, String serviceId, String version,
            DateTime from, DateTime to) {
        UsagePerPlanBean rval = new UsagePerPlanBean();
        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getResponseStats(String, String, String, HistogramIntervalType, DateTime, DateTime)
     */
    @Override
    public ResponseStatsHistogramBean getResponseStats(String organizationId, String serviceId,
            String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        ResponseStatsHistogramBean rval = new ResponseStatsHistogramBean();
        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getResponseStatsSummary(String, String, String, DateTime, DateTime)
     */
    @Override
    public ResponseStatsSummaryBean getResponseStatsSummary(String organizationId, String serviceId,
            String version, DateTime from, DateTime to) {
        ResponseStatsSummaryBean rval = new ResponseStatsSummaryBean();
        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getResponseStatsPerApp(String, String, String, DateTime, DateTime)
     */
    @Override
    public ResponseStatsPerAppBean getResponseStatsPerApp(String organizationId, String serviceId,
            String version, DateTime from, DateTime to) {
        ResponseStatsPerAppBean rval = new ResponseStatsPerAppBean();
        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getResponseStatsPerPlan(String, String, String, DateTime, DateTime)
     */
    @Override
    public ResponseStatsPerPlanBean getResponseStatsPerPlan(String organizationId, String serviceId,
            String version, DateTime from, DateTime to) {
        ResponseStatsPerPlanBean rval = new ResponseStatsPerPlanBean();
        return rval;
    }

    /**
     * @see com.t1t.digipolis.apim.core.IMetricsAccessor#getAppUsagePerService(String, String, String, DateTime, DateTime)
     */
    @Override
    public AppUsagePerServiceBean getAppUsagePerService(String organizationId, String applicationId,
            String version, DateTime from, DateTime to) {
        AppUsagePerServiceBean rval = new AppUsagePerServiceBean();
        return rval;
    }
}
