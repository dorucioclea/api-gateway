package com.t1t.apim.beans.metrics;


/**
 * Bean returned for the "Response Type" histogram metric. The data returned is
 * a set of data points over a histogram date range. The period of each bucket
 * is dependent upon the granularity specified in the request.
 *
 */
public class ResponseStatsHistogramBean extends HistogramBean<ResponseStatsDataPoint> {

}
