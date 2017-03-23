package com.t1t.apim.cdi;

import com.t1t.apim.core.config.ApiManagerConfig;
import com.t1t.apim.jpa.IJpaProperties;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Configuration object for the API Manager.
 */
@ApplicationScoped
public class WarApiManagerConfig extends ApiManagerConfig implements IJpaProperties {

    /**
     * @see IJpaProperties#getAllHibernateProperties()
     */
    @Override
    public Map<String, String> getAllHibernateProperties() {
        Map<String, String> rval = new HashMap<>();
        Iterator<String> keys = getConfig().getKeys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.startsWith("apiengine.hibernate.")) { //$NON-NLS-1$
                String value = getConfig().getString(key);
                key = key.substring("apiengine.".length()); //$NON-NLS-1$
                rval.put(key, value);
            }
        }
        return rval;
    }

}