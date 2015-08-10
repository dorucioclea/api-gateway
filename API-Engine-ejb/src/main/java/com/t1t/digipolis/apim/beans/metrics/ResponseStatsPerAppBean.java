package com.t1t.digipolis.apim.beans.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean returned for the "Response Stats per App" metric.
 *
 */
public class ResponseStatsPerAppBean {

    private Map<String, ResponseStatsDataPoint> data = new HashMap<>();

    /**
     * Constructor.
     */
    public ResponseStatsPerAppBean() {
    }

    /**
     * @return the data
     */
    public Map<String, ResponseStatsDataPoint> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<String, ResponseStatsDataPoint> data) {
        this.data = data;
    }

    /**
     * @param app
     * @param total
     * @param failures
     * @param errors
     */
    public void addDataPoint(String app, long total, long failures, long errors) {
        ResponseStatsDataPoint point = new ResponseStatsDataPoint(null, total, failures, errors);
        data.put(app, point);
    }

}
