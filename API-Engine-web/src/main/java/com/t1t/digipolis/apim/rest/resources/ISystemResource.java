package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.services.DefaultServiceTermsBean;
import com.t1t.digipolis.apim.beans.system.SystemStatusBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * A simple System API.
 */
public interface ISystemResource {

    /**
     * This endpoint simply returns the status of the apiman system.  This is
     * a useful endpoint to use when testing a client's connection to the apiman
     * API Manager REST services.
     * @summary Get System Status
     * @statuscode 200 On success.
     * @return System status information.
     */
    public SystemStatusBean getStatus() throws GatewayAuthenticationException, StorageException;
}
