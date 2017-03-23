package com.t1t.apim.common.config;

import org.apache.commons.lang.text.StrLookup;
import org.jboss.security.vault.SecurityVaultUtil;

/**
 * Allows users to lookup strings from the vault and use them in the
 * apiman.properties file.
 *
 */
public class VaultLookup extends StrLookup {

    /**
     * @see StrLookup#lookup(String)
     */
    @Override
    public String lookup(String key) {
        try {
            return SecurityVaultUtil.getValueAsString(key);
        } catch (Throwable t) {
            // Eat it - if something goes wrong, too bad - we're probably not running in jboss
        }
        return null;
    }

}
