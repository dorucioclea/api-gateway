package com.t1t.digipolis.apim.core.metrics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.t1t.digipolis.apim.beans.metrics.HistogramIntervalType;
import com.t1t.digipolis.apim.beans.metrics.ServiceMarketInfo;
import com.t1t.digipolis.kong.model.*;
import org.joda.time.DateTime;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by michallispashidis on 07/09/16.
 */
public class DataDogMetricsSP implements MetricsSPI, Serializable {
    //private static Logger log = LoggerFactory.getLogger(DataDogMetricsSP.class.getName());
    private static DataDogDBMetricsClient httpClient;

/*    {
        //read properties file
        InputStream is = getClass().getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        if(is!=null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else throw new RuntimeException("API Engine basic property file not found.");
        //read specific application config, depends on the maven profile that has been set
        Config config = ConfigFactory.load(properties.getProperty(IConfig.PROP_FILE_CONFIG_FILE));
        if(config ==null) throw new RuntimeException("API Engine config not found");
        String metricsURI = "";
        if (config != null && StringUtils.isEmpty(metricsURI)) {
            metricsURI = new StringBuffer("")
                    .append(config.getString(IConfig.METRICS_SCHEME))
                    .append("://")
                    .append(config.getString(IConfig.METRICS_DNS))
                    .append((!StringUtils.isEmpty(config.getString(IConfig.METRICS_PORT))) ? ":" + config.getString(IConfig.METRICS_PORT) : "")
                    .append("/").toString();
            log.info("DataDog Metrics processor instantiated for URI: {}", metricsURI);
            //create metrics client instance
            RestDataDogMetricsBuilder restMongoMetricsBuilder = new RestDataDogMetricsBuilder();
            httpClient = restMongoMetricsBuilder.getService(metricsURI, DataDogDBMetricsClient.class);
        }else throw new RuntimeException("DataDogMetricsAccessor - Metrics are not initialized");
    }*/

    public static void main (String [] args){
        RestDataDogMetricsBuilder restMongoMetricsBuilder = new RestDataDogMetricsBuilder();
        httpClient = restMongoMetricsBuilder.getService("https://app.datadoghq.com", DataDogDBMetricsClient.class);
        DataDogMetricsSP client = new DataDogMetricsSP();
        client.getUsage("","","",null,null,null);
    }

    @Override
    public MetricsUsageList getUsage(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        JsonObject usage = httpClient.getUsage();
        JsonArray series = usage.getAsJsonArray("series");
        List<Arrays> yourList;
        if(series.size()>0){
            JsonObject series0 = series.get(0).getAsJsonObject();
            JsonArray pointlist = series0.getAsJsonArray("pointlist");
            Type listType = new TypeToken<List<Object>>() {}.getType();
            yourList = new Gson().fromJson(series0.getAsJsonArray("pointlist"), listType);

            //compose response
            MetricsUsageList metricsUsageList = new MetricsUsageList();
            //metricsUsageList.setData();
        }

        if (usage == null) {
            return null;
        }

        //get value list

        //get interval

        //get length


        //will contain the final results
        MetricsUsageList resultUsageList = new MetricsUsageList();
        //we create a epoch map in order to add specific values at random and sort the key values
        Map<Long, MetricsUsage> processingMap = new TreeMap<>();
        long intervalMillis = 300;//interval
        //prefill map - the from value should be the first REAL metrics value we encouter (in order to sync time interval with the metrics engine)
        long fromMillis = from.getMillis();

/*        for (MetricsUsage originUsage : originList.getData()) {
            processingMap.put(new Double(originUsage.getInterval()).longValue(), new MetricsUsage().withCount(originUsage.getCount()).withInterval(originUsage.getInterval()));
        }*/
        //prep result
        resultUsageList.setData(new ArrayList<MetricsUsage>(processingMap.values()));
        return resultUsageList;
    }

    @Override
    public MetricsResponseStatsList getResponseStats(String organizationId, String serviceId, String version, HistogramIntervalType interval, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public MetricsResponseSummaryList getResponseStatsSummary(String organizationId, String serviceId, String version, DateTime from, DateTime to) {
        return null;
    }

    @Override
    public MetricsConsumerUsageList getAppUsageForService(String organizationId, String applicationId, String version, HistogramIntervalType interval, DateTime from, DateTime to, String consumerId) {
        return null;
    }

    @Override
    public ServiceMarketInfo getServiceMarketInfo(String organizationId, String serviceId, String version) {
        return null;
    }
}
