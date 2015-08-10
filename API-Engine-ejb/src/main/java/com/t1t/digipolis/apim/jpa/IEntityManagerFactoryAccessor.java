package com.t1t.digipolis.apim.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Simple interface used to access the entity manager factory. This can be
 * provided in any number of ways based on runtime platform and other relevant
 * options.
 *
 */
public interface IEntityManagerFactoryAccessor {

    /**
     * @return gets the {@link EntityManagerFactory}
     */
    public EntityManagerFactory getEntityManagerFactory();
}
