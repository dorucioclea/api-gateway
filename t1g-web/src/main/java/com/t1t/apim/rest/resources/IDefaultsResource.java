package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.services.DefaultServiceTermsBean;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IDefaultsResource {

    /**
     * Retrieve the default service terms & conditions
     *
     * @return
     */
    public DefaultServiceTermsBean getDefaultServiceTerms();
}
