package com.t1t.apim.core;

import com.t1t.apim.beans.services.ServiceVersionBean;

/**
 * Validates the state of services and service versions.
 *
 */
public interface IServiceValidator {

    /**
     * <p>
     * Is the given service Ready to be published to the Gateway?  This method
     * will return true if all of the criteria for publishing is met.  The 
     * criteria includes (but is not necessarily limited to):
     * </p>
     * 
     * <ul>
     *   <li>A service implementation endpoint is set</li>
     *   <li>At least one Plan is selected for use</li>
     * </ul>
     * 
     * @param service
     * @return true if ready, else false
     * @throws Exception
     */
    boolean isReady(ServiceVersionBean service) throws Exception;

}
