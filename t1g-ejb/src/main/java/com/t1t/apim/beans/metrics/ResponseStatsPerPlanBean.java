package com.t1t.apim.beans.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean returned for the "Usage per Plan" metric.
 *
 */
public class ResponseStatsPerPlanBean {

    private Map<String, ResponseStatsDataPoint> data = new HashMap<>();

    /**
     * Constructor.
     */
    public ResponseStatsPerPlanBean() {
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
     * @param plan
     * @param total
     * @param failures
     * @param errors
     */
    public void addDataPoint(String plan, long total, long failures, long errors) {
        ResponseStatsDataPoint point = new ResponseStatsDataPoint(null, total, failures, errors);
        data.put(plan, point);
    }

}
