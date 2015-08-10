package com.t1t.digipolis.apim.common.auth;

/**
 * When signing an auth token, a shared secret is required.  This interface allows 
 * runtime discovery and acquisition of a shared secret.
 *
 */
public interface ISharedSecretSource {
    
    /**
     * @return the shared secret.
     */
    public String getSharedSecret();

}
