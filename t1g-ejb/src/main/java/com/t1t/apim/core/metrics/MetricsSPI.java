package com.t1t.apim.core.metrics;

import com.t1t.apim.beans.metrics.AppUsageBean;
import com.t1t.apim.beans.metrics.ServiceMarketInfoBean;
import com.t1t.apim.beans.metrics.ServiceUsageBean;
import com.t1t.apim.beans.services.ServiceVersionBean;
import org.joda.time.DateTime;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 * An interface used to access metrics information.  Typically metrics are
 * recorded at runtime by the API Gateway into some sort of data store (e.g.
 * mongodb).  Implementations of this interface
 * must know how to extract the metrics information in useful and common ways
 * from that data store.
 */
public interface MetricsSPI {

    /**
     * Get all available metrics for a given service
     * @param organizationId
     * @param serviceId
     * @param version
     * @return
     */
    ServiceUsageBean getServiceUsage(ServiceVersionBean service);

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
    AppUsageBean getAppUsageForService(ServiceVersionBean service, String consumerId, DateTime from, DateTime to);

    /**
     * Provides the service marketplace information.
     * <ul>
     *     <li>Uptime percentage</li>
     *     <li>Amount of distinct users</li>
     *     <li>Amount of followers</li>
     * </ul>
     * @param organizationId
     * @param serviceId
     * @param version
     * @return
     */
    ServiceMarketInfoBean getServiceMarketInfo(ServiceVersionBean service);
}
