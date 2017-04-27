package com.t1t.apim.core.metrics;

import com.t1t.kong.model.DataDogAvailableMetrics;
import com.t1t.kong.model.DataDogMetricsQuery;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by michallispashidis on 07/09/16.
 */
public interface DataDogMetricsClient {

    /*********************
     * DATADOG METRICS ***
     *********************/

    @GET("/api/v1/query")
    DataDogMetricsQuery queryMeterics(@Query("from") String from,
                                      @Query("to") String to,
                                      @Query("query") String query);

    @GET("/api/v1/metrics")
    DataDogAvailableMetrics getAvailableQueries(@Query("from") String from);
}
