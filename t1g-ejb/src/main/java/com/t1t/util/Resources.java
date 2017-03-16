package com.t1t.util;

import com.t1t.apim.kong.KongServiceBuilder;

import javax.inject.Inject;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * <p>
 * <p>
 * Example injection on a managed bean field:
 * </p>
 */
public class Resources {
    /**
     * REST Service Builder (Retrofit)
     */
    @Inject
    private KongServiceBuilder serviceBuilder;
}
