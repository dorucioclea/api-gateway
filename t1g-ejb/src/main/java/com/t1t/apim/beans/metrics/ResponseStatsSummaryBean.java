package com.t1t.apim.beans.metrics;


/**
 * Bean returned for the "Response Stats per App" metric.
 *
 */
public class ResponseStatsSummaryBean {

    private long total;
    private long failures;
    private long errors;

    /**
     * Constructor.
     */
    public ResponseStatsSummaryBean() {
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
