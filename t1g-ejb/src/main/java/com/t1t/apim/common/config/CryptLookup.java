package com.t1t.apim.common.config;

import com.t1t.util.AesEncrypter;
import org.apache.commons.lang.text.StrLookup;

/**
 * Allows users to AesEncrypt their properties in apiman.properties.
 *
 */
public class CryptLookup extends StrLookup {

    /**
     * @see StrLookup#lookup(String)
     */
    @Override
    public String lookup(String key) {
        String cval = "$CRYPT::" + key; //$NON-NLS-1$
        return AesEncrypter.decrypt(cval);
    }

}
