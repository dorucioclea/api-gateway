
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
public class KongPluginResponseTransformerModification {

    @SerializedName("headers")
    @Expose
    private List<String> headers = new ArrayList<String>();
    @SerializedName("json")
    @Expose
    private List<String> json = new ArrayList<String>();

    /**
     * 
     * @return
     *     The headers
     */
    public List<String> getHeaders() {
        return headers;
    }

    /**
     * 
     * @param headers
     *     The headers
     */
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public KongPluginResponseTransformerModification withHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 
     * @return
     *     The json
     */
    public List<String> getJson() {
        return json;
    }

    /**
     * 
     * @param json
     *     The json
     */
    public void setJson(List<String> json) {
        this.json = json;
    }

    public KongPluginResponseTransformerModification withJson(List<String> json) {
        this.json = json;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(headers).append(json).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginResponseTransformerModification) == false) {
            return false;
        }
        KongPluginResponseTransformerModification rhs = ((KongPluginResponseTransformerModification) other);
        return new EqualsBuilder().append(headers, rhs.headers).append(json, rhs.json).isEquals();
    }

}
