package com.t1t.digipolis.apim.cdi;

import com.t1t.digipolis.apim.core.config.ApiManagerConfig;
import com.t1t.digipolis.apim.jpa.IJpaProperties;

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
     * Constructor.
     */
    public WarApiManagerConfig() {
    }

    /**
     * @see IJpaProperties#getAllHibernateProperties()
     */
    @Override
    public Map<String, String> getAllHibernateProperties() {
        Map<String, String> rval = new HashMap<>();
        Iterator<String> keys = getConfig().getKeys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.startsWith("apiman.hibernate.")) { //$NON-NLS-1$
                String value = getConfig().getString(key);
                key = key.substring("apiman.".length()); //$NON-NLS-1$
                rval.put(key, value);
            }
        }
        return rval;
    }

}
