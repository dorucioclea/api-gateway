package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.services.DefaultServiceTermsBean;
import com.t1t.digipolis.apim.exceptions.NotAuthorizedException;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IAdminResource {

    /**
     * Update/set the default service terms & conditions
     * @param bean
     * @throws NotAuthorizedException
     */
    public DefaultServiceTermsBean updateDefaultServiceTerms(DefaultServiceTermsBean bean) throws NotAuthorizedException;
}