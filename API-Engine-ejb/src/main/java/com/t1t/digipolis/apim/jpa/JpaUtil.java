package com.t1t.digipolis.apim.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;

/**
 * Some utility methods for JPA support.
 *
 */
public final class JpaUtil {

    private static Logger logger = LoggerFactory.getLogger(JpaUtil.class);

    /**
     * Returns true if the given exception is a unique constraint violation.  This
     * is useful to detect whether someone is trying to persist an entity that
     * already exists.  It allows us to simply assume that persisting a new entity
     * will work, without first querying the DB for the existence of that entity.
     *
     * Note that my understanding is that JPA is supposed to throw an {@link EntityExistsException}
     * when the row already exists.  However, this is not always the case, based on
     * experience.  Or perhaps it only throws the exception if the entity is already
     * loaded from the DB and exists in the {@link EntityManager}.
     * @param e the exception
     * @return whether a constraint violation occurred
     */
    public static boolean isConstraintViolation(Exception e) {
        Throwable cause = e;
        while (cause != cause.getCause() && cause.getCause() != null) {
            if (cause.getClass().getSimpleName().equals("ConstraintViolationException")) //$NON-NLS-1$
                return true;
            cause = cause.getCause();
        }
        return false;
    }

    /**
     * Rolls back a transaction.  Tries to be smart and quiet about it.
     * @param entityManager the entity manager
     */
    public static void rollbackQuietly(EntityManager entityManager) {
        if (entityManager.getTransaction().isActive()/* && entityManager.getTransaction().getRollbackOnly()*/) {
            try {
                entityManager.getTransaction().rollback();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
