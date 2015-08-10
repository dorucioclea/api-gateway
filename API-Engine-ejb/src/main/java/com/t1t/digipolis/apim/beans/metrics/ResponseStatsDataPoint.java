package com.t1t.digipolis.apim.beans.metrics;

/**
 * A single data point in the usage histogram.
 *
 */
public class ResponseStatsDataPoint extends HistogramDataPoint {

    private long total;
    private long failures;
    private long errors;

    /**
     * Constructor.
     */
    public ResponseStatsDataPoint() {
    }

    /**
     * Constructor.
     * @param label
     * @param total
     * @param failures
     * @param errors
     */
    public ResponseStatsDataPoint(String label, long total, long failures, long errors) {
        super(label);
        setTotal(total);
        setFailures(failures);
        setErrors(errors);
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return the failures
     */
    public long getFailures() {
        return failures;
    }

    /**
     * @param failures the failures to set
     */
    public void setFailures(long failures) {
        this.failures = failures;
    }

    /**
     * @return the errors
     */
    public long getErrors() {
        return errors;
    }

    /**
     * @param errors the errors to set
     */
    public void setErrors(long errors) {
        this.errors = errors;
    }

}
