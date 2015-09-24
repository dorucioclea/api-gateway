package com.t1t.digipolis.apim.auth.rest.resources;

import com.t1t.digipolis.apim.beans.system.SystemStatusBean;

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
    public SystemStatusBean getStatus();

}