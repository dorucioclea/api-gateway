package com.t1t.digipolis.apim.core;

import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import com.t1t.digipolis.kong.model.MetricsResponseSummaryList;
import com.t1t.digipolis.kong.model.MetricsUsageList;
import org.joda.time.DateTime;

/**
 * An interface used to access metrics information.  Typically metrics are
 * recorded at runtime by the API Gateway into some sort of data store (e.g.
 * mongodb).  Implementations of this interface
 * must know how to extract the metrics information in useful and common ways
 * from that data store.
 */
public interface IMetricsAccessor {

    /**
     * Query the metrics store for the total # of requests made to the service
     * per time period within the date range.  This will return an array with one
     * entry per bucket in the requested interval, even if the bucket has zero
     * results.  So, for example, if the request is for the last 90 days with an
     * interval of 'day', the result will be an array with 90 entries, each entry
     * with a label and a value >= 0.
     *
     * @param organizationId
     * @param serviceId
     * @param version
     * @param interval
     * @param from
     * @param to
     */
    MetricsUsageList getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to);

    /**
     * Query the metrics store for a histogram of response statistics, including total
     * number of responses, # of error responses,
     *
     * @param organizationId
     * @param serviceId
     * @param version
     * @param interval
     * @param from
     * @param to
     */
    MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to);

    /**
     * Query the metrics store for response type stats (total, errors, failures) for a given
     * service over a specified time range.  Returns a total summary of stats.
     *
     * @param organizationId
     * @param serviceId
     * @param version
     * @param from
     * @param to
     */
    MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to);

    /**
     * Query the metrics store for # of requests made to a service broken
     * down by plan.  For exclusively public services this will return no data.
     *
     * @param organizationId
     * @param applicationId
     * @param version
     * @param from
     * @param to
     */
    MetricsConsumerUsageList getAppUsageForService(String organizationId, String applicationId, String version, HistogramIntervalType interval, DateTime from, DateTime to, String consumerId);

    /**
     * Provides the service marketplace information.
     * <ul>
     *     <li>Uptime percentage</li>
     *     <li>Amount of developers</li>
     *     <li>Amount of followers</li>
     * </ul>
     * @param organizationId
     * @param serviceId
     * @param version
     * @return
     */
    ServiceMarketInfo getServiceMarketInfo(String organizationId, String serviceId, String version);

}
