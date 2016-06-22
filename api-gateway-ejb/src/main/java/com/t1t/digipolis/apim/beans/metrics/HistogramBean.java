package com.t1t.digipolis.apim.beans.metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for histogram beans.
 *
 */
public abstract class HistogramBean<T extends HistogramDataPoint> {

    private List<T> data = new ArrayList<>();

    /**
     * @return the data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * Adds a single data point.
     * @param point
     */
    public void addDataPoint(T point) {
        getData().add(point);
    }

}
