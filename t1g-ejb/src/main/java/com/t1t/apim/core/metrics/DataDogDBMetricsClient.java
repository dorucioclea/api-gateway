package com.t1t.apim.core.metrics;

import com.t1t.kong.model.DataDogMetricsQuery;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by michallispashidis on 07/09/16.
 */
public interface DataDogDBMetricsClient {

    /*********************
     * DATADOG METRICS ***
     *********************/
    //https://app.datadoghq.com/api/v1/query?api_key=***REMOVED***&application_key=***REMOVED***&query=avg:kong.bza_citygis_v1.request.count{host:rasu094.rte.antwerpen.local}.as_count()&from=1473160678&to=1473247056

    @GET("/api/v1/query")
    DataDogMetricsQuery queryMeterics(@Query("api_key") String apiKey,
                                      @Query("application_key") String appKey,
                                      @Query("from") String from,
                                      @Query("to") String to,
                                      @Query("query") String query);
}
