package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.AbstractRestException;
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
     * Obsolete for later versions
     */
    //public void migrateToAcl() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException;

    /**
     * Use this endpoint to change applications' versionless customId's to match their usernames
     */
    //public void updateConsumersCustomId() throws AbstractRestException;

    /**
     * Sync the local db with the Kong gateway in case of:
     * <ul>
     *     <li>In case of inconsistency force sync DB->gateways</li>
     *     <li>Zero Downtime Deployment</li>
     * </ul>
     *//*
    public void syncGateways() throws AbstractRestException;*/

    /**
     * Rebuild and populate a gateway when empty.
     *
     * @throws ServiceVersionNotFoundException
     * @throws InvalidServiceStatusException
     * @throws GatewayNotFoundException
     * @throws StorageException
     */
    public void rebuild() throws ServiceVersionNotFoundException, InvalidServiceStatusException, GatewayNotFoundException, StorageException;

    /**
     * Split all organizations (shared in version v0.7.x and lower) to be contextualized.
     *
     * @throws AbstractRestException
     */
    //public void splitOrgs() throws Exception;

    /**
     * Issue JWT credentials for all consumers, if a consumer has already JWT credentials, skip.
     * This functionality is mainly to enrich existing consumers with JWT credentials. For end-users this is already done,
     * for applications, this was not the case until v0.8.1
     * @throws AbstractRestException
     */
    public void issueJWT() throws AbstractRestException, StorageException;

    /**
     * Sync the gateway api plugin id's with corresponding service policies
     */
    public void updatePoliciesWithGatewayPluginIds();

    /**
     * Sync or create credentials for applications
     */
    public void syncOrCreateConsumerCredentials();

    /**
     * Apply the default service policies to every service version
     * @throws AbstractRestException
     */
    public void applyDefaultPoliciesToServiceVersions() throws AbstractRestException;

}

