package com.t1t.digipolis.apim.core.metrics;

import com.t1t.digipolis.apim.IConfig;
import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.core.IMetricsAccessor;
import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import com.t1t.digipolis.kong.model.MetricsResponseSummary;
import com.t1t.digipolis.kong.model.MetricsResponseSummaryList;
import com.t1t.digipolis.kong.model.MetricsUsageList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.util.List;

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
    public MetricsUsageList getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return httpClient.getServiceUsageFromToInterval(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis());
    }

    @Override
    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return httpClient.getServiceResponseStatisticsFromToInterval(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis());
    }

    @Override
    public MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return httpClient.getServiceResponseSummaryFromTo(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), "" + from.getMillis(), "" + to.getMillis());
    }

    @Override
    public MetricsConsumerUsageList getAppUsageForService(String organizationId, String serviceId, String version,HistogramIntervalType interval, DateTime from, DateTime to, String consumerId) {
        return httpClient.getServiceConsumerUsageFromToInterval(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis(), consumerId);
    }

    @Override
    public ServiceMarketInfo getServiceMarketInfo(String organizationId, String serviceId, String version) {
        //distinct active users
        int distinctUsers = httpClient.getServiceConsumers(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase()).getData().size();
        //uptime - conventionally last month/by week
        DateTime to = new DateTime();
        DateTime from = to.minusMonths(1);
        MetricsResponseSummaryList summList = httpClient.getServiceResponseSummaryFromTo(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), "" + from.getMillis(), "" + to.getMillis());
        List<MetricsResponseSummary> res = summList.getData();
        int uptime = 100;
        if(res!=null && res.size()>0){
            double req_count = res.get(0).getRequestsCount();
            double res_wrong = res.get(0).getResponseWrong();
            //if res_wrong = 0 no mean to calculate
            if(req_count>res_wrong && res_wrong>0) uptime = ((Double)((res_wrong/req_count)*100)).intValue();
            else uptime = 100;
        }

        ServiceMarketInfo info = new ServiceMarketInfo();
        info.setDevelopers(distinctUsers);
        info.setUptime(uptime);
        //TODO followers
        info.setFollowers(0);
        return info;
    }


}
