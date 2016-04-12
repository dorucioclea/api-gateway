package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.GatewayNotFoundException;
import com.t1t.digipolis.apim.exceptions.InvalidServiceStatusException;
import com.t1t.digipolis.apim.exceptions.ServiceVersionNotFoundException;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IMigrationResource {

    /**
     * Use this endpoint to migrate a version of the API Engine to 0.6.3
     */
    public void migrateToAcl() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException;

    /**
     * Use this endpoint to change applications' versionless customId's to match their usernames
     */
    public void updateConsumersCustomId();
}

