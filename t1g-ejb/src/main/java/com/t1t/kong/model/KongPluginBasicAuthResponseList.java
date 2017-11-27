
package com.t1t.kong.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginBasicAuthResponseList {

    @SerializedName("data")
    @Expose
    private List<KongPluginBasicAuthResponse> data = new ArrayList<KongPluginBasicAuthResponse>();
    @SerializedName("next")
    @Expose
    private String next;

    /**
     * 
     * @return
     *     The data
     */
    public List<KongPluginBasicAuthResponse> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<KongPluginBasicAuthResponse> data) {
        this.data = data;
    }

    public KongPluginBasicAuthResponseList withData(List<KongPluginBasicAuthResponse> data) {
        this.data = data;
        return this;
    }

    /**
     * 
     * @return
     *     The next
     */
    public String getNext() {
        return next;
    }

    /**
     * 
     * @param next
     *     The next
     */
    public void setNext(String next) {
        this.next = next;
    }

    public KongPluginBasicAuthResponseList withNext(String next) {
        this.next = next;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(data).append(next).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginBasicAuthResponseList) == false) {
            return false;
        }
        KongPluginBasicAuthResponseList rhs = ((KongPluginBasicAuthResponseList) other);
        return new EqualsBuilder().append(data, rhs.data).append(next, rhs.next).isEquals();
    }

}
