package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IMaintenanceResource {

    public void enableMaintenanceMode(String message) throws NotAuthorizedException;

    public void disableMaintenanceMode() throws NotAuthorizedException;

    public void updateMaintenanceMessage(String message) throws NotAuthorizedException;

}