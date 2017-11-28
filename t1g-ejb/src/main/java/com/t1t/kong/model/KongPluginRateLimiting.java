
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginRateLimiting implements KongConfigValue
{

    @SerializedName("day")
    @Expose
    private Long day;
    @SerializedName("minute")
    @Expose
    private Long minute;
    @SerializedName("second")
    @Expose
    private Long second;
    @SerializedName("hour")
    @Expose
    private Long hour;
    @SerializedName("month")
    @Expose
    private Long month;
    @SerializedName("year")
    @Expose
    private Long year;

    /**
     * 
     * @return
     *     The day
     */
    public Long getDay() {
        return day;
    }

    /**
     * 
     * @param day
     *     The day
     */
    public void setDay(Long day) {
        this.day = day;
    }

    public KongPluginRateLimiting withDay(Long day) {
        this.day = day;
        return this;
    }

    /**
     * 
     * @return
     *     The minute
     */
    public Long getMinute() {
        return minute;
    }

    /**
     * 
     * @param minute
     *     The minute
     */
    public void setMinute(Long minute) {
        this.minute = minute;
    }

    public KongPluginRateLimiting withMinute(Long minute) {
        this.minute = minute;
        return this;
    }

    /**
     * 
     * @return
     *     The second
     */
    public Long getSecond() {
        return second;
    }

    /**
     * 
     * @param second
     *     The second
     */
    public void setSecond(Long second) {
        this.second = second;
    }

    public KongPluginRateLimiting withSecond(Long second) {
        this.second = second;
        return this;
    }

    /**
     * 
     * @return
     *     The hour
     */
    public Long getHour() {
        return hour;
    }

    /**
     * 
     * @param hour
     *     The hour
     */
    public void setHour(Long hour) {
        this.hour = hour;
    }

    public KongPluginRateLimiting withHour(Long hour) {
        this.hour = hour;
        return this;
    }

    /**
     * 
     * @return
     *     The month
     */
    public Long getMonth() {
        return month;
    }

    /**
     * 
     * @param month
     *     The month
     */
    public void setMonth(Long month) {
        this.month = month;
    }

    public KongPluginRateLimiting withMonth(Long month) {
        this.month = month;
        return this;
    }

    /**
     * 
     * @return
     *     The year
     */
    public Long getYear() {
        return year;
    }

    /**
     * 
     * @param year
     *     The year
     */
    public void setYear(Long year) {
        this.year = year;
    }

    public KongPluginRateLimiting withYear(Long year) {
        this.year = year;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(day).append(minute).append(second).append(hour).append(month).append(year).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginRateLimiting) == false) {
            return false;
        }
        KongPluginRateLimiting rhs = ((KongPluginRateLimiting) other);
        return new EqualsBuilder().append(day, rhs.day).append(minute, rhs.minute).append(second, rhs.second).append(hour, rhs.hour).append(month, rhs.month).append(year, rhs.year).isEquals();
    }

}
