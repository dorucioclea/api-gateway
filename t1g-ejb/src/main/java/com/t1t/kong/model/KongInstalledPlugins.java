
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
public class KongInstalledPlugins {

    @SerializedName("enabled_plugins")
    @Expose
    private List<String> enabledPlugins = new ArrayList<String>();

    /**
     * 
     * @return
     *     The enabledPlugins
     */
    public List<String> getEnabledPlugins() {
        return enabledPlugins;
    }

    /**
     * 
     * @param enabledPlugins
     *     The enabled_plugins
     */
    public void setEnabledPlugins(List<String> enabledPlugins) {
        this.enabledPlugins = enabledPlugins;
    }

    public KongInstalledPlugins withEnabledPlugins(List<String> enabledPlugins) {
        this.enabledPlugins = enabledPlugins;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(enabledPlugins).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongInstalledPlugins) == false) {
            return false;
        }
        KongInstalledPlugins rhs = ((KongInstalledPlugins) other);
        return new EqualsBuilder().append(enabledPlugins, rhs.enabledPlugins).isEquals();
    }

}
