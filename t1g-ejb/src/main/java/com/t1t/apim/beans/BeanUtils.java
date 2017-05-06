package com.t1t.apim.beans;

/**
 * Some simple bean utils.
 *
 */
public class BeanUtils {
    /**
     * Creates a bean id from the given bean name.  This essentially removes any
     * non "word" characters from the name.
     * Hypens are not allowed, because they are reserved in order to append a prefix for an organization,
     * depending on the scope (marketplace where the org has been created).
     * 
     * @param name the name
     * @return the id
     */
    public static final String idFromName(String name) {
        //contextualize
        return name.replaceAll("[^\\w-\\.]", "").toLowerCase(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**p
     * Validates that a version string is OK - doesn't contain any
     * illegal characters.
     * @param version the version
     * @return true if valid, else false
     */
    public static final boolean isValidVersion(String version) {
        return idFromName(version).equals(version);
    }
}
