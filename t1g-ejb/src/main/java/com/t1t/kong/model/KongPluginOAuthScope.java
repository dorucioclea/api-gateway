
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginOAuthScope {

    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("scope_desc")
    @Expose
    private String scopeDesc;

    /**
     * 
     * @return
     *     The scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * 
     * @param scope
     *     The scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    public KongPluginOAuthScope withScope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * 
     * @return
     *     The scopeDesc
     */
    public String getScopeDesc() {
        return scopeDesc;
    }

    /**
     * 
     * @param scopeDesc
     *     The scope_desc
     */
    public void setScopeDesc(String scopeDesc) {
        this.scopeDesc = scopeDesc;
    }

    public KongPluginOAuthScope withScopeDesc(String scopeDesc) {
        this.scopeDesc = scopeDesc;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(scope).append(scopeDesc).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginOAuthScope) == false) {
            return false;
        }
        KongPluginOAuthScope rhs = ((KongPluginOAuthScope) other);
        return new EqualsBuilder().append(scope, rhs.scope).append(scopeDesc, rhs.scopeDesc).isEquals();
    }

}
