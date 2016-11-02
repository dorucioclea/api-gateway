package com.t1t.digipolis.apim.rest.resources;

import com.t1t.digipolis.apim.beans.services.DefaultServiceTermsBean;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IDefaultsResource {

    /**
     * Retrieve the default service terms & conditions
     * @return
     */
    public DefaultServiceTermsBean getDefaultServiceTerms();
}
