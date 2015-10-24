package com.t1t.digipolis.apim.suites;

import com.t1t.digipolis.apim.facades.OrganizationFacadeTest;
import com.t1t.digipolis.apim.facades.RoleFacadeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by michallispashidis on 22/10/15.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({RoleFacadeTest.class, OrganizationFacadeTest.class})
public class TestSuiteFacades {
}
