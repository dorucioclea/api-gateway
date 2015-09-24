package com.t1t.digipolis.apim.core.metrics;

import com.t1t.digipolis.apim.IConfig;
import com.t1t.digipolis.apim.beans.metrics.*;
import com.t1t.digipolis.apim.core.IMetricsAccessor;
import com.t1t.digipolis.kong.model.*;
import com.t1t.digipolis.kong.model.MetricsConsumerUsage;
import com.t1t.digipolis.kong.model.MetricsConsumerUsageList;
import com.t1t.digipolis.kong.model.MetricsResponseStats;
import com.t1t.digipolis.kong.model.MetricsResponseStatsList;
import com.t1t.digipolis.kong.model.MetricsResponseSummary;
import com.t1t.digipolis.kong.model.MetricsResponseSummaryList;
import com.t1t.digipolis.kong.model.MetricsUsage;
import com.t1t.digipolis.kong.model.MetricsUsageList;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.util.*;

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

    //interval values
    private static final long ONE_MINUTE_MILLIS = 1 * 60 * 1000;
    private static final long ONE_HOUR_MILLIS = 1 * 60 * 60 * 1000;
    private static final long ONE_DAY_MILLIS = 1 * 24 * 60 * 60 * 1000;
    private static final long ONE_WEEK_MILLIS = 7 * 24 * 60 * 60 * 1000;
    private static final long ONE_MONTH_MILLIS = 30 * 24 * 60 * 60 * 1000;

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
        MetricsUsageList originList = httpClient.getServiceUsageFromToInterval(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis());
        //will contain the final results
        MetricsUsageList resultUsageList = new MetricsUsageList();
        //we create a epoch map in order to add specific values at random and sort the key values
        Map<Long,MetricsUsage> processingMap = new TreeMap<>();
        long toMillis = to.getMillis();
        long intervalMillis = getIntervalMillis(interval);
        //prefill map - the from value should be the first REAL metrics value we encouter (in order to sync time interval with the metrics engine)
        long fromMillis = from.getMillis();
        if(originList.getData().size()>0){
            fromMillis = new Double(originList.getData().get(0).getInterval()).longValue();
        }
        for(;fromMillis<=toMillis;fromMillis=fromMillis+intervalMillis){
            processingMap.put(fromMillis,new MetricsUsage().withCount(0d).withInterval(new Double(fromMillis)));
        }
        for(MetricsUsage originUsage:originList.getData()){
            processingMap.put(new Double(originUsage.getInterval()).longValue(), new MetricsUsage().withCount(originUsage.getCount()).withInterval(originUsage.getInterval()));
        }
        //prep result
        resultUsageList.setData(new ArrayList<MetricsUsage>(processingMap.values()));
        return resultUsageList;
    }

    @Override
    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        MetricsResponseStatsList originList = httpClient.getServiceResponseStatisticsFromToInterval(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis());
        MetricsResponseStatsList resultUsageList = new MetricsResponseStatsList();
        //we create a epoch map in order to add specific values at random and sort the key values
        Map<Long,MetricsResponseStats> processingMap = new TreeMap<>();
        long toMillis = to.getMillis();
        long intervalMillis = getIntervalMillis(interval);
        //prefill map - the from value should be the first REAL metrics value we encouter (in order to sync time interval with the metrics engine)
        long fromMillis = from.getMillis();
        if(originList.getData().size()>0){
            fromMillis = new Double(originList.getData().get(0).getDateInterval()).longValue();
        }
        for(;fromMillis<=toMillis;fromMillis=fromMillis+intervalMillis){
            processingMap.put(fromMillis, new MetricsResponseStats().withDateInterval(new Double(fromMillis))
                    .withLatencyKong(0d).withLatencyProxy(0d).withLatencyRequest(0d)
                    .withRequestsCount(0d).withRequestsWrong(0d).withResponseWrong(0d));
        }
        for(MetricsResponseStats originUsage:originList.getData()){
            processingMap.put(new Double(originUsage.getDateInterval()).longValue(), new MetricsResponseStats().withDateInterval(originUsage.getDateInterval())
            .withLatencyKong(originUsage.getLatencyKong()).withLatencyProxy(originUsage.getLatencyProxy()).withLatencyRequest(originUsage.getLatencyRequest())
            .withRequestsCount(originUsage.getRequestsCount()).withRequestsWrong(originUsage.getRequestsWrong()).withResponseWrong(originUsage.getResponseWrong()));
        }
        //prep result
        resultUsageList.setData(new ArrayList<MetricsResponseStats>(processingMap.values()));
        return resultUsageList;
    }

    @Override
    public MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        //here we only have on set of results, we don't need to add date records in order to prepare the data for a front end application
        return httpClient.getServiceResponseSummaryFromTo(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), "" + from.getMillis(), "" + to.getMillis());
    }

    @Override
    public MetricsConsumerUsageList getAppUsageForService(String organizationId, String serviceId, String version,HistogramIntervalType interval, DateTime from, DateTime to, String consumerId) {
        MetricsConsumerUsageList originList = httpClient.getServiceConsumerUsageFromToInterval(organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis(), consumerId);
        MetricsConsumerUsageList resultUsageList = new MetricsConsumerUsageList();
        //we create a epoch map in order to add specific values at random and sort the key values
        Map<Long, com.t1t.digipolis.kong.model.MetricsConsumerUsage> processingMap = new TreeMap<>();
        long toMillis = to.getMillis();
        long intervalMillis = getIntervalMillis(interval);
        //prefill map - the from value should be the first REAL metrics value we encouter (in order to sync time interval with the metrics engine)
        long fromMillis = from.getMillis();
        if(originList.getData().size()>0){
            fromMillis = new Double(originList.getData().get(0).getInterval()).longValue();
        }
        for(;fromMillis<=toMillis;fromMillis=fromMillis+intervalMillis){
            processingMap.put(fromMillis, new MetricsConsumerUsage().withInterval(new Double(fromMillis))
                    .withCount(0d));
        }
        for(MetricsConsumerUsage originUsage:originList.getData()){
            processingMap.put(new Double(originUsage.getInterval()).longValue(), new MetricsConsumerUsage().withInterval(originUsage.getInterval()).withCount(originUsage.getCount()));
        }
        //prep result
        resultUsageList.setData(new ArrayList<MetricsConsumerUsage>(processingMap.values()));
        return resultUsageList;
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

    /**
     * Returns the interval in millis for histogram calculation.
     *
     * @param interval
     * @return
     */
    private long getIntervalMillis(HistogramIntervalType interval) {
        long divBy = ONE_DAY_MILLIS;
        switch (interval) {
            case day:
                divBy = ONE_DAY_MILLIS;
                break;
            case hour:
                divBy = ONE_HOUR_MILLIS;
                break;
            case minute:
                divBy = ONE_MINUTE_MILLIS;
                break;
            case month:
                divBy = ONE_MONTH_MILLIS;
                break;
            case week:
                divBy = ONE_WEEK_MILLIS;
                break;
            default:
                break;
        }
        return divBy;
    }
}
