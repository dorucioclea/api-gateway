package com.t1t.digipolis.util;

import io.swagger.models.Swagger;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 8/10/15.
 */
public class SwaggerUtilsTest {

    @Test
    public void testGetSwaggerPaths() throws Exception {

    }

    @Test
    public void testPrintSwaggerPaths() throws Exception {
        Swagger swaggerdefs = SwaggerUtils.getSwaggerObject(getClass().getResourceAsStream("/swaggerdefs/swagger_security.json"));
        assertNotNull(swaggerdefs);
        SwaggerUtils.printSwaggerPaths(swaggerdefs);
        SwaggerUtils.printSwaggerSecurity(swaggerdefs);
    }
}