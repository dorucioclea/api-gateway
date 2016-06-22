package com.t1t.digipolis.apim.common.auth;

import java.util.Date;
import java.util.Set;

/**
 * A simple authentication token.
 *
 */
public class AuthToken {
    
    private Date issuedOn;
    private Date expiresOn;
    private String principal;
    private Set<String> roles;
    private String signature;
    
    /**
     * Constructor.
     */
    public AuthToken() {
    }

    /**
     * @return the issuedOn
     */
    public Date getIssuedOn() {
        return issuedOn;
    }

    /**
     * @param issuedOn the issuedOn to set
     */
    public void setIssuedOn(Date issuedOn) {
        this.issuedOn = issuedOn;
    }

    /**
     * @return the expiresOn
     */
    public Date getExpiresOn() {
        return expiresOn;
    }

    /**
     * @param expiresOn the expiresOn to set
     */
    public void setExpiresOn(Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    /**
     * @return the principal
     */
    public String getPrincipal() {
        return principal;
    }

    /**
     * @param principal the principal to set
     */
    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    /**
     * @return the roles
     */
    public Set<String> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

}
