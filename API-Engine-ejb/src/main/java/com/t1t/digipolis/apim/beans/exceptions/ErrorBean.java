package com.t1t.digipolis.apim.beans.exceptions;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Simple error bean used to serialize error information to JSON when
 * responding to a REST call with an error.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorBean {

    private String type;
    private int errorCode;
    private String message;
    private String moreInfoUrl;
    private String stacktrace;

    /**
     * Constructor.
     */
    public ErrorBean() {
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the moreInfo
     */
    public String getMoreInfoUrl() {
        return moreInfoUrl;
    }

    /**
     * @param moreInfoUrl the moreInfo to set
     */
    public void setMoreInfoUrl(String moreInfoUrl) {
        this.moreInfoUrl = moreInfoUrl;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the stacktrace
     */
    public String getStacktrace() {
        return stacktrace;
    }

    /**
     * @param stacktrace the stacktrace to set
     */
    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "ErrorBean [type=" + type + ", errorCode=" + errorCode + ", message=" + message
                + ", moreInfoUrl=" + moreInfoUrl + ", stacktrace=" + stacktrace + "]";
    }

}
