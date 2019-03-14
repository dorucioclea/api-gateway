package com.t1t.apim.security;

import com.t1t.apim.beans.apps.AppIdentifier;
import com.t1t.apim.core.exceptions.StorageException;

/**
 * The security context used by the REST API to determine whether the
 * application has appropriate access to access the authorization customizations on
 * the API Engine.
 */
public interface ISecurityAppContext {

    /**
     * @return the authenticated application consumer.
     */
    String getApplication();

    String setCurrentApplication(String application) throws StorageException;

    AppIdentifier getApplicationIdentifier();

    String getApplicationPrefix();

    String getNonManagedApplication();

    String setNonManagedApplication(String application);

}
