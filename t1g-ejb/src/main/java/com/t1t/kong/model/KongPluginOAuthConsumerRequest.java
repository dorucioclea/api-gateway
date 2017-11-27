
package com.t1t.kong.model;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginOAuthConsumerRequest implements KongConfigValue
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("client_secret")
    @Expose
    private String clientSecret;
    @SerializedName("redirect_uri")
    @Expose
    private Set<String> redirectUri = new LinkedHashSet<String>();

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

    public KongPluginOAuthConsumerRequest withId(String id) {
        this.id = id;
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

    public KongPluginOAuthConsumerRequest withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 
     * @param clientId
     *     The client_id
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public KongPluginOAuthConsumerRequest withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * 
     * @return
     *     The clientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * 
     * @param clientSecret
     *     The client_secret
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public KongPluginOAuthConsumerRequest withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * 
     * @return
     *     The redirectUri
     */
    public Set<String> getRedirectUri() {
        return redirectUri;
    }

    /**
     * 
     * @param redirectUri
     *     The redirect_uri
     */
    public void setRedirectUri(Set<String> redirectUri) {
        this.redirectUri = redirectUri;
    }

    public KongPluginOAuthConsumerRequest withRedirectUri(Set<String> redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(clientId).append(clientSecret).append(redirectUri).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginOAuthConsumerRequest) == false) {
            return false;
        }
        KongPluginOAuthConsumerRequest rhs = ((KongPluginOAuthConsumerRequest) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(clientId, rhs.clientId).append(clientSecret, rhs.clientSecret).append(redirectUri, rhs.redirectUri).isEquals();
    }

}
