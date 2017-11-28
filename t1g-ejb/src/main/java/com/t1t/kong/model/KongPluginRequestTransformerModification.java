
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
public class KongPluginRequestTransformerModification {

    @SerializedName("querystring")
    @Expose
    private List<String> querystring = new ArrayList<String>();
    @SerializedName("body")
    @Expose
    private List<String> body = new ArrayList<String>();
    @SerializedName("headers")
    @Expose
    private List<String> headers = new ArrayList<String>();

    /**
     * 
     * @return
     *     The querystring
     */
    public List<String> getQuerystring() {
        return querystring;
    }

    /**
     * 
     * @param querystring
     *     The querystring
     */
    public void setQuerystring(List<String> querystring) {
        this.querystring = querystring;
    }

    public KongPluginRequestTransformerModification withQuerystring(List<String> querystring) {
        this.querystring = querystring;
        return this;
    }

    /**
     * 
     * @return
     *     The body
     */
    public List<String> getBody() {
        return body;
    }

    /**
     * 
     * @param body
     *     The body
     */
    public void setBody(List<String> body) {
        this.body = body;
    }

    public KongPluginRequestTransformerModification withBody(List<String> body) {
        this.body = body;
        return this;
    }

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

    public KongPluginRequestTransformerModification withHeaders(List<String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(querystring).append(body).append(headers).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginRequestTransformerModification) == false) {
            return false;
        }
        KongPluginRequestTransformerModification rhs = ((KongPluginRequestTransformerModification) other);
        return new EqualsBuilder().append(querystring, rhs.querystring).append(body, rhs.body).append(headers, rhs.headers).isEquals();
    }

}
