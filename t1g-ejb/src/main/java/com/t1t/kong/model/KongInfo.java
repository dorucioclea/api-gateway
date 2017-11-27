
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongInfo {

    @SerializedName("timers")
    @Expose
    private Timers timers;
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("configuration")
    @Expose
    private Configuration configuration;
    @SerializedName("lua_version")
    @Expose
    private String luaVersion;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("hostname")
    @Expose
    private String hostname;
    @SerializedName("plugins")
    @Expose
    private Plugins plugins;

    /**
     * 
     * @return
     *     The timers
     */
    public Timers getTimers() {
        return timers;
    }

    /**
     * 
     * @param timers
     *     The timers
     */
    public void setTimers(Timers timers) {
        this.timers = timers;
    }

    public KongInfo withTimers(Timers timers) {
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

    public KongInfo withVersion(String version) {
        this.version = version;
        return this;
    }

    /**
     * 
     * @return
     *     The configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * 
     * @param configuration
     *     The configuration
     */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public KongInfo withConfiguration(Configuration configuration) {
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

    public KongInfo withLuaVersion(String luaVersion) {
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

    public KongInfo withTagline(String tagline) {
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

    public KongInfo withHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    /**
     * 
     * @return
     *     The plugins
     */
    public Plugins getPlugins() {
        return plugins;
    }

    /**
     * 
     * @param plugins
     *     The plugins
     */
    public void setPlugins(Plugins plugins) {
        this.plugins = plugins;
    }

    public KongInfo withPlugins(Plugins plugins) {
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
        if ((other instanceof KongInfo) == false) {
            return false;
        }
        KongInfo rhs = ((KongInfo) other);
        return new EqualsBuilder().append(timers, rhs.timers).append(version, rhs.version).append(configuration, rhs.configuration).append(luaVersion, rhs.luaVersion).append(tagline, rhs.tagline).append(hostname, rhs.hostname).append(plugins, rhs.plugins).isEquals();
    }

}
