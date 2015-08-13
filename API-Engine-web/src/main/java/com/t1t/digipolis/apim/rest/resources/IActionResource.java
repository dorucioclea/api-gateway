package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.actions.ActionBean;
import com.t1t.digipolis.apim.rest.resources.exceptions.ActionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * The Action API.  This API allows callers to perform actions on various 
 * entities - actions other than the standard REST "crud" actions.
 */
public interface IActionResource {

    /**
     * Call this endpoint in order to execute actions for apiman entities such
     * as Plans, Services, or Applications.  The type of the action must be 
     * included in the request payload.
     * @summary Execute an Entity Action
     * @param action The details about what action to execute.
     * @statuscode 204 If the action completes successfully.
     * @throws ActionException action is performed but an error occurs during processing
     */
    public void performAction(ActionBean action) throws ActionException;

}
