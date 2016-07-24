package com.t1t.digipolis.apim.jpa;

import java.util.Map;

/**
 * Interface for accessing JPA/hibernate properties.  Must be provided by the
 * platform.
 *
 */
public interface IJpaProperties {

    /**
     * @return all configured hibernate properties
     */
    Map<String, String> getAllHibernateProperties();

}
