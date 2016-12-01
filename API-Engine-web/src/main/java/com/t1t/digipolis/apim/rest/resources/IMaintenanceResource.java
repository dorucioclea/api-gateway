package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.operation.OperatingBean;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;
import org.jboss.resteasy.spi.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IMaintenanceResource {

    public void enableMaintenanceMode(String message) throws NotAuthorizedException;

    public void disableMaintenanceMode() throws NotAuthorizedException;

    public void updateMaintenanceMessage(String message) throws NotAuthorizedException;

}