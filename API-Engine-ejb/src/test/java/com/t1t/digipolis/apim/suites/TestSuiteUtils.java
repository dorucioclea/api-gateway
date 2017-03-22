package com.t1t.digipolis.apim.suites;

/**
 * Created by michallispashidis on 21/10/15.
 */

import com.t1t.digipolis.util.GatewayPathUtilitiesTest;
import com.t1t.digipolis.util.ImageSizeTest;
import com.t1t.digipolis.util.SwaggerUtilsTest;
import com.t1t.digipolis.util.URIUtilsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({GatewayPathUtilitiesTest.class, ImageSizeTest.class, SwaggerUtilsTest.class, URIUtilsTest.class})
public class TestSuiteUtils {

}
