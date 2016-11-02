package com.t1t.digipolis.apim.core;

import com.t1t.digipolis.apim.beans.apps.ApplicationVersionBean;

/**
 * Validates the state of applications and application versions.
 *
 */
public interface IApplicationValidator {

    /**
     * <p>
     * Is the given application Ready to be registered with the Gateway?  This method
     * will return true if all of the criteria for registration is met.  The 
     * criteria includes (but is not necessarily limited to):
     * </p>
     * 
     * <ul>
     *   <li>At least one Service Contract exists for the application</li>
     * </ul>
     * 
     * @param application
     * @return true if ready, else false
     */
    boolean isReady(ApplicationVersionBean application);

    /**
     * <p>
     * Is the given application Ready to be registered with the Gateway?  This method
     * will return true if all of the criteria for registration is met.  The 
     * criteria includes (but is not necessarily limited to):
     * </p>
     * 
     * <ul>
     *   <li>At least one Service Contract exists for the application</li>
     * </ul>
     * 
     * <p>
     * This version of isRead() skips the check for contracts and instead
     * uses the value passed in.  This is important if, for example, a
     * contract is being created.
     * </p>
     * 
     * @param application
     * @param hasContracts
     * @return true if ready, else false
     */
    boolean isReady(ApplicationVersionBean application, boolean hasContracts);

}
