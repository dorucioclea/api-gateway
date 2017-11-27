
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
public class KongPluginIPRestriction implements KongConfigValue
{

    @SerializedName("blacklist")
    @Expose
    private List<String> blacklist = new ArrayList<String>();
    @SerializedName("whitelist")
    @Expose
    private List<String> whitelist = new ArrayList<String>();

    /**
     * 
     * @return
     *     The blacklist
     */
    public List<String> getBlacklist() {
        return blacklist;
    }

    /**
     * 
     * @param blacklist
     *     The blacklist
     */
    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
    }

    public KongPluginIPRestriction withBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
        return this;
    }

    /**
     * 
     * @return
     *     The whitelist
     */
    public List<String> getWhitelist() {
        return whitelist;
    }

    /**
     * 
     * @param whitelist
     *     The whitelist
     */
    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public KongPluginIPRestriction withWhitelist(List<String> whitelist) {
        this.whitelist = whitelist;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(blacklist).append(whitelist).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginIPRestriction) == false) {
            return false;
        }
        KongPluginIPRestriction rhs = ((KongPluginIPRestriction) other);
        return new EqualsBuilder().append(blacklist, rhs.blacklist).append(whitelist, rhs.whitelist).isEquals();
    }

}
