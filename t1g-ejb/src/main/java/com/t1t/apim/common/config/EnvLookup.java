package com.t1t.apim.common.config;

import org.apache.commons.lang.text.StrLookup;

/**
 * Allows users to AesEncrypt their properties in apiman.properties.
 *
 */
public class EnvLookup extends StrLookup {

    /**
     * @see StrLookup#lookup(String)
     */
    @Override
    public String lookup(String key) {
        String value = System.getenv(key);
        if (value == null) {
            return ""; //$NON-NLS-1$
        } else {
            return value;
        }
    }

}
