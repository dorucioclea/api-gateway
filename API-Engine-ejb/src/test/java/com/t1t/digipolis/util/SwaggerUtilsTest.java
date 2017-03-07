package com.t1t.digipolis.util;

import io.swagger.models.Swagger;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by michallispashidis on 8/10/15.
 */
public class SwaggerUtilsTest {
    @Rule
    public ExpectedException thrown= ExpectedException.none();
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Rule
    public TestName name = new TestName();
/*    @Rule
    public ExternalResource rule = new ExternalResource() {
        @Override protected void before() throws Throwable {
            resource = new Resource();
            resource.open();
            System.out.println(name.getMethodName());
        }

        @Override protected void after()  {
            resource.close();
            System.out.println("\n");
        }
    };*/

    @Test
    public void testGetSwaggerPaths() throws Exception {
        Swagger swaggerdefs = SwaggerUtils.getSwaggerObject(getClass().getResourceAsStream("/swaggerdefs/swagger_security.json"));
        assertNotNull(swaggerdefs);
        SwaggerUtils.printSwaggerPaths(swaggerdefs);
        SwaggerUtils.printSwaggerSecurity(swaggerdefs);
    }

    @Test
    public void testGetSwaggerSecurity() throws Exception {
        Swagger swaggerdefs = SwaggerUtils.getSwaggerObject(getClass().getResourceAsStream("/swaggerdefs/swagger_simple.json"));
        assertNotNull(swaggerdefs);
        SwaggerUtils.printSwaggerPaths(swaggerdefs);
        thrown.expect(NullPointerException.class);
        SwaggerUtils.printSwaggerSecurity(swaggerdefs);
    }

    @Test
    public void testPrintSwaggerPaths() throws Exception {
        InputStream is = getClass().getResourceAsStream("/swaggerdefs/swagger_translation.json");
        String jsonTxt = IOUtils.toString(is);
        //System.out.println(jsonTxt);
        JSONObject json = new JSONObject(jsonTxt);
        String a = json.getString("host");
        JSONArray arr = json.getJSONArray("schemes");
        JSONObject externalDocs = json.getJSONObject("externalDocs");
        String docUrl = externalDocs.getString("url");
        System.out.println("host:" + a);
        System.out.println("schema's:"+arr);
        System.out.println("doc url:"+docUrl);

        //update json
        externalDocs = new JSONObject();
        externalDocs.put("url","http://malakia.com");
        json.remove("externalDocs");
        json.put("externalDocs",externalDocs);

        File testfile = folder.newFile("testfile.json");
        if(!testfile.exists())testfile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(testfile));
        bw.write(json.toString());
        bw.close();
    }
}