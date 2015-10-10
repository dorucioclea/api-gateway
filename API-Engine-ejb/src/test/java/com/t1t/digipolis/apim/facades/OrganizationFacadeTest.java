package com.t1t.digipolis.apim.facades;

import com.t1t.digipolis.apim.beans.orgs.NewOrganizationBean;
import org.jglue.cdiunit.CdiRunner;
import org.jglue.cdiunit.ejb.SupportEjb;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertNotNull;

/**
 * Created by michallispashidis on 9/09/15.
 */
@RunWith(CdiRunner.class) // Runs the test with CDI-Unit
@SupportEjb
public class OrganizationFacadeTest {
    @Inject
    private OrganizationFacade organizationFacade;
    protected static EntityManagerFactory emf;

    @Inject
    protected EntityManager em;

    @BeforeClass
    public static void createEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("h2-api-engine");
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
        emf.close();
    }

    @Before
    public void beginTransaction() {
        organizationFacade.setEm(em);
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        if (em.isOpen()) {
            em.close();
        }
    }

    @Test
    public void testDbConnection() {
        assertNotNull(em);
    }

    @Test
    public void testCreate() throws Exception {
        NewOrganizationBean newOrg = new NewOrganizationBean();
        newOrg.setName("TestOrg");
        newOrg.setDescription("This is a junit test org");
    }
}