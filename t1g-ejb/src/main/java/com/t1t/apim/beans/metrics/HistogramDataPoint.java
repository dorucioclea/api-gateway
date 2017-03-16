package com.t1t.apim.beans.metrics;

/**
 * Base class for histogram data points.
 *
 */
public abstract class HistogramDataPoint {

    private String label;

    /**
     * Constructor.
     */
    public HistogramDataPoint() {
    }

    /**
     * Constructor.
     * @param label
     */
    public HistogramDataPoint(String label) {
        setLabel(label);
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
