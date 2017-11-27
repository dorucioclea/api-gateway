
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginBasicAuthResponse {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("custom_id")
    @Expose
    private String customId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
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

    public KongPluginBasicAuthResponse withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The customId
     */
    public String getCustomId() {
        return customId;
    }

    /**
     * 
     * @param customId
     *     The custom_id
     */
    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public KongPluginBasicAuthResponse withCustomId(String customId) {
        this.customId = customId;
        return this;
    }

    /**
     * 
     * @return
     *     The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public KongPluginBasicAuthResponse withUsername(String username) {
        this.username = username;
        return this;
    }

    /**
     * 
     * @return
     *     The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public KongPluginBasicAuthResponse withPassword(String password) {
        this.password = password;
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

    public KongPluginBasicAuthResponse withCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(customId).append(username).append(password).append(createdAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginBasicAuthResponse) == false) {
            return false;
        }
        KongPluginBasicAuthResponse rhs = ((KongPluginBasicAuthResponse) other);
        return new EqualsBuilder().append(id, rhs.id).append(customId, rhs.customId).append(username, rhs.username).append(password, rhs.password).append(createdAt, rhs.createdAt).isEquals();
    }

}
