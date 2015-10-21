package com.t1t.digipolis.util;

import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.core.exceptions.StorageException;
import io.swagger.models.Scheme;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import io.swagger.util.Json;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
        //Swagger swaggerdefs = SwaggerUtils.getSwaggerObject(getClass().getResourceAsStream("/swaggerdefs/swagger_translation.json"));

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

        File testfile = new File("testfile.json");
        if(!testfile.exists())testfile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(testfile));
        bw.write(json.toString());
        bw.close();

/*        assertNotNull(swaggerdefs);
        SwaggerUtils.printSwaggerPaths(swaggerdefs);
        SwaggerUtils.printSwaggerSecurity(swaggerdefs);*/

    }
}