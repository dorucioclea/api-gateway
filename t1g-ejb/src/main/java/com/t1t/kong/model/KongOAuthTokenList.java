
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
public class KongOAuthTokenList {

    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("offset")
    @Expose
    private String offset;
    @SerializedName("data")
    @Expose
    private List<KongOAuthToken> data = new ArrayList<KongOAuthToken>();
    @SerializedName("total")
    @Expose
    private Long total;

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

    public KongOAuthTokenList withNext(String next) {
        this.next = next;
        return this;
    }

    /**
     * 
     * @return
     *     The offset
     */
    public String getOffset() {
        return offset;
    }

    /**
     * 
     * @param offset
     *     The offset
     */
    public void setOffset(String offset) {
        this.offset = offset;
    }

    public KongOAuthTokenList withOffset(String offset) {
        this.offset = offset;
        return this;
    }

    /**
     * 
     * @return
     *     The data
     */
    public List<KongOAuthToken> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<KongOAuthToken> data) {
        this.data = data;
    }

    public KongOAuthTokenList withData(List<KongOAuthToken> data) {
        this.data = data;
        return this;
    }

    /**
     * 
     * @return
     *     The total
     */
    public Long getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The total
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    public KongOAuthTokenList withTotal(Long total) {
        this.total = total;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(next).append(offset).append(data).append(total).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongOAuthTokenList) == false) {
            return false;
        }
        KongOAuthTokenList rhs = ((KongOAuthTokenList) other);
        return new EqualsBuilder().append(next, rhs.next).append(offset, rhs.offset).append(data, rhs.data).append(total, rhs.total).isEquals();
    }

}
