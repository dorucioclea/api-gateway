package com.t1t.digipolis.apim.suites;

/**
 * Created by michallispashidis on 21/10/15.
 */

import com.t1t.digipolis.util.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({GatewayPathUtilitiesTest.class, ImageSizeTest.class, SwaggerUtilsTest.class, URIUtilsTest.class})
public class TestSuiteUtils {

}
