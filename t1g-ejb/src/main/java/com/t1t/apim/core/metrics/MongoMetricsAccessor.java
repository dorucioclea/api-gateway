package com.t1t.apim.core.metrics;

import com.t1t.apim.IConfig;
import com.t1t.apim.beans.metrics.HistogramIntervalType;
import com.t1t.apim.beans.metrics.ServiceMarketInfo;
import com.t1t.apim.core.IMetricsAccessor;
import com.t1t.apim.exceptions.ExceptionFactory;
import com.t1t.kong.model.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Created by michallispashidis on 12/09/15.
 */
@Singleton
@ApplicationScoped
@Default
public class MongoMetricsAccessor implements IMetricsAccessor, Serializable {
    private static Logger log = LoggerFactory.getLogger(MongoMetricsAccessor.class.getName());
    private static MetricsClient httpClient;
    private static String metricsURI;
    private static RestMetricsBuilder restMetricsBuilder;
    private static Config config;
    private static Properties properties;
    private static Integer timeout;

    //interval values

    {
        //read properties file
        InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
        properties = new Properties();
        if(is!=null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else throw ExceptionFactory.systemErrorException("API Engine basic property file not found.");
        //read specific application config, depends on the maven profile that has been set
        config = ConfigFactory.load(properties.getProperty(IConfig.PROP_FILE_CONFIG_FILE)); if(config==null) throw ExceptionFactory.systemErrorException("API Engine log not found");
        metricsURI = "";
        if (config != null && StringUtils.isEmpty(metricsURI)) {
            metricsURI = new StringBuffer("")
                    .append(config.getString(IConfig.METRICS_SCHEME))
                    .append("://")
                    .append(config.getString(IConfig.METRICS_DNS))
                    .append((!StringUtils.isEmpty(config.getString(IConfig.METRICS_PORT))) ? ":" + config.getString(IConfig.METRICS_PORT) : "")
                    .append("/").toString();
            log.info("Metrics processor instantiated for URI: {}", metricsURI);
            //create metrics client instance
            restMetricsBuilder = new RestMetricsBuilder();
            httpClient = restMetricsBuilder.getService(metricsURI, MetricsClient.class);
            timeout = config.getInt(IConfig.HYSTRIX_METRICS_TIMEOUT_VALUE);
        }else throw ExceptionFactory.systemErrorException("MongoMetricsAccessor - Metrics are not initialized");
    }

    @Override
    public MetricsUsageList getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        MetricsUsageList originList = new MetricsServiceUsageFailSilent(httpClient, organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis(), timeout).execute();
        if (originList == null) {
            return null;
        }
        //will contain the final results
        MetricsUsageList resultUsageList = new MetricsUsageList();
        //we create a epoch map in order to add specific values at random and sort the key values
        Map<Long, MetricsUsage> processingMap = new TreeMap<>();
        long toMillis = to.getMillis();
        long intervalMillis = interval.getMillis();
        //prefill map - the from value should be the first REAL metrics value we encouter (in order to sync time interval with the metrics engine)
        long fromMillis = from.getMillis();
        if (originList.getData().size() > 0) {
            fromMillis = new Double(originList.getData().get(0).getInterval()).longValue();
        }
        for (; fromMillis <= toMillis; fromMillis = fromMillis + intervalMillis) {
            processingMap.put(fromMillis, new MetricsUsage().withCount(0d).withInterval(new Double(fromMillis)));
        }
        for (MetricsUsage originUsage : originList.getData()) {
            processingMap.put(new Double(originUsage.getInterval()).longValue(), new MetricsUsage().withCount(originUsage.getCount()).withInterval(originUsage.getInterval()));
        }
        //prep result
        resultUsageList.setData(new ArrayList<MetricsUsage>(processingMap.values()));
        return resultUsageList;
    }

    @Override
    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        MetricsResponseStatsList originList = new MetricsResponseStatisticsFailSilent(httpClient, organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis(), timeout).execute();
        if (originList == null) {
            return null;
        }
        MetricsResponseStatsList resultUsageList = new MetricsResponseStatsList();
        //we create a epoch map in order to add specific values at random and sort the key values
        Map<Long, MetricsResponseStats> processingMap = new TreeMap<>();
        long toMillis = to.getMillis();
        long intervalMillis = interval.getMillis();
        //prefill map - the from value should be the first REAL metrics value we encouter (in order to sync time interval with the metrics engine)
        long fromMillis = from.getMillis();
        if (originList.getData().size() > 0) {
            fromMillis = new Double(originList.getData().get(0).getDateInterval()).longValue();
        }
        for (; fromMillis <= toMillis; fromMillis = fromMillis + intervalMillis) {
            processingMap.put(fromMillis, new MetricsResponseStats().withDateInterval(new Double(fromMillis))
                    .withLatencyKong(0d).withLatencyProxy(0d).withLatencyRequest(0d)
                    .withRequestsCount(0d).withRequestsWrong(0d).withResponseWrong(0d));
        }
        for (MetricsResponseStats originUsage : originList.getData()) {
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
        return new MetricsResponseSummaryFailSilent(httpClient, organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), "" + from.getMillis(), "" + to.getMillis(), timeout).execute();

    }

    @Override
    public MetricsConsumerUsageList getAppUsageForService(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to, String consumerId) {
        MetricsConsumerUsageList originList = new MetricsConsumerUsageFailSilent(httpClient, organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), interval.toString(), "" + from.getMillis(), "" + to.getMillis(), consumerId, timeout).execute();
        if (originList == null) {
            return null;
        }
        MetricsConsumerUsageList resultUsageList = new MetricsConsumerUsageList();
        //we create a epoch map in order to add specific values at random and sort the key values
        Map<Long, MetricsConsumerUsage> processingMap = new TreeMap<>();
        long toMillis = to.getMillis();
        long intervalMillis = interval.getMillis();
        //prefill map - the from value should be the first REAL metrics value we encouter (in order to sync time interval with the metrics engine)
        long fromMillis = from.getMillis();
        if (originList.getData().size() > 0) {
            fromMillis = new Double(originList.getData().get(0).getInterval()).longValue();
        }
        for (; fromMillis <= toMillis; fromMillis = fromMillis + intervalMillis) {
            processingMap.put(fromMillis, new MetricsConsumerUsage().withInterval(new Double(fromMillis))
                    .withCount(0d));
        }
        for (MetricsConsumerUsage originUsage : originList.getData()) {
            processingMap.put(new Double(originUsage.getInterval()).longValue(), new MetricsConsumerUsage().withInterval(originUsage.getInterval()).withCount(originUsage.getCount()));
        }
        //prep result
        resultUsageList.setData(new ArrayList<MetricsConsumerUsage>(processingMap.values()));
        return resultUsageList;
    }

    @Override
    public ServiceMarketInfo getServiceMarketInfo(String organizationId, String serviceId, String version) {
        //distinct active users
        MetricsServiceConsumerList conList = new MetricsServiceConsumersFailSilent(httpClient, organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), timeout).execute();
        if (conList == null) {
            return null;
        }
        int distinctUsers = conList.getData().size();
        //uptime - conventionally last month/by week
        DateTime to = new DateTime();
        DateTime from = to.minusMonths(1);
        MetricsResponseSummaryList summList = new MetricsResponseSummaryFailSilent(httpClient, organizationId.toLowerCase(), serviceId.toLowerCase(), version.toLowerCase(), "" + from.getMillis(), "" + to.getMillis(), timeout).execute();
        if (summList == null) {
            return null;
        }
        List<MetricsResponseSummary> res = summList.getData();
        int uptime = 100;
        if (res != null && res.size() > 0) {
            double req_count = res.get(0).getRequestsCount();
            double res_wrong = res.get(0).getResponseWrong();
            //if res_wrong = 0 no mean to calculate
            if (req_count > res_wrong && res_wrong > 0)
                uptime = ((Double) (100 - ((res_wrong / req_count) * 100))).intValue();
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