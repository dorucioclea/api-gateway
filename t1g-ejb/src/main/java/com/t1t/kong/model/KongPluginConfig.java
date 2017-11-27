
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginConfig {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("api_id")
    @Expose
    private String apiId;
    @SerializedName("consumer_id")
    @Expose
    private String consumerId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("config")
    @Expose
    private Object config;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("created_at")
    @Expose
    private Double createdAt;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    public KongPluginConfig withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The apiId
     */
    public String getApiId() {
        return apiId;
    }

    /**
     * 
     * @param apiId
     *     The api_id
     */
    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public KongPluginConfig withApiId(String apiId) {
        this.apiId = apiId;
        return this;
    }

    /**
     * 
     * @return
     *     The consumerId
     */
    public String getConsumerId() {
        return consumerId;
    }

    /**
     * 
     * @param consumerId
     *     The consumer_id
     */
    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public KongPluginConfig withConsumerId(String consumerId) {
        this.consumerId = consumerId;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    public KongPluginConfig withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The config
     */
    public Object getConfig() {
        return config;
    }

    /**
     * 
     * @param config
     *     The config
     */
    public void setConfig(Object config) {
        this.config = config;
    }

    public KongPluginConfig withConfig(Object config) {
        this.config = config;
        return this;
    }

    /**
     * 
     * @return
     *     The enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * 
     * @param enabled
     *     The enabled
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public KongPluginConfig withEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public Double getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
    }

    public KongPluginConfig withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(apiId).append(consumerId).append(name).append(config).append(enabled).append(createdAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginConfig) == false) {
            return false;
        }
        KongPluginConfig rhs = ((KongPluginConfig) other);
        return new EqualsBuilder().append(id, rhs.id).append(apiId, rhs.apiId).append(consumerId, rhs.consumerId).append(name, rhs.name).append(config, rhs.config).append(enabled, rhs.enabled).append(createdAt, rhs.createdAt).isEquals();
    }

}
