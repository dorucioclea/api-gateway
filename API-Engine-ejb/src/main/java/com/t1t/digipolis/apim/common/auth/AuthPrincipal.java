package com.t1t.digipolis.apim.common.auth;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of a principal, but also includes roles.
 *
 */
public class AuthPrincipal implements Principal {
    
    private String username;
    private Set<String> roles = new HashSet<>();
    
    /**
     * Constructor.
     * 
     * @param username the username
     */
    public AuthPrincipal(String username) {
        this.username = username;
    }

    /**
     * @see Principal#getName()
     */
    @Override
    public String getName() {
        return username;
    }
    
    /**
     * Adds a role.
     * @param role the role
     */
    public void addRole(String role) {
        roles.add(role);
    }
    
    /**
     * Adds multiple roles.
     * @param roles the roles
     */
    public void addRoles(Set<String> roles) {
        this.roles.addAll(roles);
    }
    
    /**
     * @return the roles
     */
    public Set<String> getRoles() {
        return roles;
    }
}
