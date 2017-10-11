package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.system.SystemStatusBean;
import com.t1t.apim.core.exceptions.StorageException;
import com.t1t.apim.gateway.GatewayAuthenticationException;

/**
 * A simple System API.
 */
public interface ISystemResource {

    /**
     * This endpoint simply returns the status of the apiman system.  This is
     * a useful endpoint to use when testing a client's connection to the apiman
     * API Manager REST services.
     *
     * @return System status information.
     * @summary Get System Status
     * @statuscode 200 On success.
     */
    public SystemStatusBean getStatus() throws GatewayAuthenticationException, StorageException;
}
