
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongExtraInfo {

    @SerializedName("timers")
    @Expose
    private Timers_ timers;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("configuration")
    @Expose
    private Configuration_ configuration;
    @SerializedName("lua_version")
    @Expose
    private String luaVersion;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("hostname")
    @Expose
    private String hostname;
    /**
     * 
     */
    @SerializedName("plugins")
    @Expose
    private Plugins__ plugins;

    /**
     * 
     * @return
     *     The timers
     */
    public Timers_ getTimers() {
        return timers;
    }

    /**
     * 
     * @param timers
     *     The timers
     */
    public void setTimers(Timers_ timers) {
        this.timers = timers;
    }

    public KongExtraInfo withTimers(Timers_ timers) {
        this.timers = timers;
        return this;
    }

    /**
     * 
     * @return
     *     The version
     */
    public String getVersion() {
        return version;
    }

    /**
     * 
     * @param version
     *     The version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public KongExtraInfo withVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * 
     * @return
     *     The configuration
     */
    public Configuration_ getConfiguration() {
        return configuration;
    }

    /**
     * 
     * @param configuration
     *     The configuration
     */
    public void setConfiguration(Configuration_ configuration) {
        this.configuration = configuration;
    }

    public KongExtraInfo withConfiguration(Configuration_ configuration) {
        this.configuration = configuration;
        return this;
    }

    /**
     * 
     * @return
     *     The luaVersion
     */
    public String getLuaVersion() {
        return luaVersion;
    }

    /**
     * 
     * @param luaVersion
     *     The lua_version
     */
    public void setLuaVersion(String luaVersion) {
        this.luaVersion = luaVersion;
    }

    public KongExtraInfo withLuaVersion(String luaVersion) {
        this.luaVersion = luaVersion;
        return this;
    }

    /**
     * 
     * @return
     *     The tagline
     */
    public String getTagline() {
        return tagline;
    }

    /**
     * 
     * @param tagline
     *     The tagline
     */
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public KongExtraInfo withTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    /**
     * 
     * @return
     *     The hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * 
     * @param hostname
     *     The hostname
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public KongExtraInfo withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    /**
     * 
     * @return
     *     The plugins
     */
    public Plugins__ getPlugins() {
        return plugins;
    }

    /**
     * 
     * @param plugins
     *     The plugins
     */
    public void setPlugins(Plugins__ plugins) {
        this.plugins = plugins;
    }

    public KongExtraInfo withPlugins(Plugins__ plugins) {
        this.plugins = plugins;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(timers).append(version).append(configuration).append(luaVersion).append(tagline).append(hostname).append(plugins).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongExtraInfo) == false) {
            return false;
        }
        KongExtraInfo rhs = ((KongExtraInfo) other);
        return new EqualsBuilder().append(timers, rhs.timers).append(version, rhs.version).append(configuration, rhs.configuration).append(luaVersion, rhs.luaVersion).append(tagline, rhs.tagline).append(hostname, rhs.hostname).append(plugins, rhs.plugins).isEquals();
    }

}
