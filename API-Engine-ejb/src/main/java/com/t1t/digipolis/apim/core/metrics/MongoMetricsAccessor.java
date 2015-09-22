package com.t1t.digipolis.apim.core.metrics;

import com.t1t.digipolis.apim.IConfig;
import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.core.IMetricsAccessor;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.apim.kong.RestServiceBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;

/**
 * Created by michallispashidis on 12/09/15.
 */
@ApplicationScoped
@Default
public class MongoMetricsAccessor implements IMetricsAccessor {
    private static Logger log = LoggerFactory.getLogger(MongoMetricsAccessor.class.getName());
    private static MetricsClient httpClient;
    private static Config config;
    private static String metricsURI;
    private static RestMetricsBuilder restMetricsBuilder;

    static {
        metricsURI = null;
        config = ConfigFactory.load();
        if(config!=null){
            metricsURI = new StringBuffer("")
                    .append(config.getString(IConfig.METRICS_SCHEME))
                    .append("://")
                    .append(config.getString(IConfig.METRICS_DNS))
                    .append((!StringUtils.isEmpty(config.getString(IConfig.METRICS_PORT)))?":"+config.getString(IConfig.METRICS_PORT):"")
                    .append("/").toString();
        }
        //create metrics client instance
        restMetricsBuilder = new RestMetricsBuilder();
        httpClient = restMetricsBuilder.getService(metricsURI, MetricsClient.class);
    }

    @Override
    public UsageHistogramBean getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        httpClient.getServiceUsageFromToInterval(organizationId,serviceId,version,interval.toString(),""+from.getMillis(),""+to.getMillis());
        return null;
    }

    @Override
    public ResponseStatsHistogramBean getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        httpClient.getServiceResponseStatisticsFromToInterval(organizationId,serviceId,version,interval.toString(),""+from.getMillis(),""+to.getMillis());
        return null;
    }

    @Override
    public ResponseStatsSummaryBean getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        httpClient.getServiceResponseSummaryFromTo(organizationId,serviceId,version,""+from.getMillis(),""+to.getMillis());
        return null;
    }

    @Override
    public AppUsagePerServiceBean getAppUsageForService(String organizationId, String applicationId, String version,HistogramIntervalType interval, DateTime from, DateTime to, String consumerId) {
        httpClient.getServiceConsumerUsageFromToInterval(organizationId,applicationId,version,interval.toString(),""+from.getMillis(),""+to.getMillis(),consumerId);
        return null;
    }


}
