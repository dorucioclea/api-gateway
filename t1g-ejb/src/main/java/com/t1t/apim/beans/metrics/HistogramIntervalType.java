package com.t1t.apim.beans.metrics;

/**
 * The various intervals allowed for a histogram metric.
 *
 */
public enum HistogramIntervalType {

    month(30 * 24 * 60 * 60 * 1000), week(7 * 24 * 60 * 60 * 1000), day(1 * 24 * 60 * 60 * 1000), hour(1 * 60 * 60 * 1000), minute(1 * 60 * 1000);

    private long millis;

    HistogramIntervalType(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return millis;
    }
}
