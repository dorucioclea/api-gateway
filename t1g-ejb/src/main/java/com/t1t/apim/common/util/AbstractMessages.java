package com.t1t.apim.common.util;

import java.text.MessageFormat;
import java.util.*;

/**
 * Base class for i18n messages classes.
 */
public class AbstractMessages {

    public static final List<String> FORMATS = Collections.unmodifiableList(Arrays.asList("java.properties")); //$NON-NLS-1$

    private static Map<String, ResourceBundle> bundles = new HashMap<>();
    private static ThreadLocal<Locale> tlocale = new ThreadLocal<>();
    private Class<? extends AbstractMessages> clazz;

    /**
     * Constructor.
     *
     * @param c the class
     */
    public AbstractMessages(Class<? extends AbstractMessages> c) {
        clazz = c;
    }

    public static void clearLocale() {
        tlocale.set(null);
    }

    /**
     * Gets a bundle.  First tries to find one in the cache, then loads it if
     * it can't find one.
     */
    private ResourceBundle getBundle() {
        String bundleKey = getBundleKey();
        if (bundles.containsKey(bundleKey)) {
            return bundles.get(bundleKey);
        } else {
            ResourceBundle bundle = loadBundle();
            bundles.put(bundleKey, bundle);
            return bundle;
        }
    }

    /**
     * Gets the key to use into the cache of bundles.  The key is made up of the
     * fully qualified class name and the locale.
     */
    private String getBundleKey() {
        Locale locale = getLocale();
        return clazz.getName() + "::" + locale.toString(); //$NON-NLS-1$
    }

    /**
     * Loads the resource bundle.
     */
    private ResourceBundle loadBundle() {
        String pkg = clazz.getPackage().getName();
        Locale locale = getLocale();
        return PropertyResourceBundle.getBundle(pkg + ".messages", locale, clazz.getClassLoader(), new ResourceBundle.Control() { //$NON-NLS-1$
            @Override
            public List<String> getFormats(String baseName) {
                return FORMATS;
            }
        });
    }

    /**
     * Gets the locale to use when finding a bundle.  The locale to use is either from the
     * thread local value, if set, or else the system default locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        if (tlocale.get() != null) {
            return tlocale.get();
        } else {
            return Locale.getDefault();
        }
    }

    public static void setLocale(Locale locale) {
        tlocale.set(locale);
    }

    /**
     * Look up a message in the i18n resource message bundle by key, then format the
     * message with the given params and return the result.
     *
     * @param key    the key
     * @param params the parameters
     * @return formatted string
     */
    public String format(String key, Object... params) {
        ResourceBundle bundle = getBundle();
        if (bundle.containsKey(key)) {
            String msg = bundle.getString(key);
            return MessageFormat.format(msg, params);
        } else {
            return MessageFormat.format("!!{0}!!", key); //$NON-NLS-1$
        }
    }

    /**
     * @return all strings in the bundle
     */
    public Map<String, String> all() {
        Map<String, String> rval = new TreeMap<>();
        ResourceBundle bundle = getBundle();
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            rval.put(key, bundle.getString(key));
        }
        return rval;
    }

}
