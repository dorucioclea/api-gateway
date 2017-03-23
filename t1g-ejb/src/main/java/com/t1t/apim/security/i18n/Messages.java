package com.t1t.apim.security.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Message bundle accessor for i18n.
 *
 */
public class Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".messages"); //$NON-NLS-1$

    /**
     * Constructor.
     */
    private Messages() {
    }

    /**
     * Gets a string from the bundle.
     * @param key the key
     * @return the resolved string or !key! if missing
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
}
