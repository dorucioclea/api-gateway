package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.actions.ActionBean;
import com.t1t.apim.beans.actions.SwaggerDocBean;
import com.t1t.apim.exceptions.ActionException;

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


    /**
     * Call this endpoint in order to fetch a remote Swagger Documentation JSON file.
     * @summary Fetch remote Swagger Documentation
     * @param swaggerDocBean Bean containing the uri of the swagger documentation to fetch.
     * @statuscode 200 If the action completes successfully.
     * @throws ActionException action is performed but an error occurs during processing
     */
    public SwaggerDocBean fetchSwaggerDoc(SwaggerDocBean swaggerDocBean) throws ActionException;
}
