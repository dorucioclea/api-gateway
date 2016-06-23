package com.t1t.digipolis.apim.gateway.dto;

import java.util.HashMap;

/**
 * Extends the basic {@link HashMap} class in order to provide case insensitive 
 * lookups.
 *
 */
public class HeaderHashMap extends HashMap<String, String> {
    
    private static final long serialVersionUID = -8627183971399152775L;

    private HashMap<String, String> caseInsensitiveIndex = new HashMap<>();
    
    /**
     * Constructor.
     */
    public HeaderHashMap() {
    }
    
    /**
     * @see HashMap#get(Object)
     */
    @Override
    public String get(Object key) {
        String rval = super.get(key);
        if (rval == null) {
            String trimmedKey = trim((String)key);
            rval = caseInsensitiveIndex.get(trimmedKey.toLowerCase());
        }
        return rval;
    }

    /**
     * @see HashMap#containsKey(Object)
     */
    @Override
    public boolean containsKey(Object key) {
        boolean rval = super.containsKey(key);
        if (!rval) {
            String trimmedKey = trim((String)key);
            rval = caseInsensitiveIndex.containsKey(trimmedKey.toLowerCase());
        }
        return rval;
    }

    /**
     * @see HashMap#put(Object, Object)
     */
    @Override
    public String put(String key, String value) {
        String trimmedKey = trim(key);
        String trimmedValue = trim(value);
        String rval = super.put(trimmedKey, trimmedValue);
        caseInsensitiveIndex.put(trimmedKey.toLowerCase(), trimmedValue);
        return rval;
    }

    /**
     * @see HashMap#remove(Object)
     */
    @Override
    public String remove(Object key) {
        String trimmedKey = trim((String)key);
        caseInsensitiveIndex.remove(trimmedKey.toLowerCase());
        return super.remove(trimmedKey);
    }

    /**
     * Trim string of whitespace.
     * 
     * @param string string to trim
     * @return trimmed string, or null if null was provided.
     */
    private static String trim(String string) {
        return string == null ? null : string.trim();
    }

}
