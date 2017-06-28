package com.t1t.digipolis.util;

import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.auth.SecuritySchemeDefinition;
import io.swagger.parser.SwaggerParser;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by michallispashidis on 8/10/15.
 */
public class SwaggerUtils {
    private static final Logger log = LoggerFactory.getLogger(SwaggerUtils.class.getName());

    public static Swagger getSwaggerObject(InputStream stream) throws IOException {
        return new SwaggerParser().parse(IOUtils.toString(stream));
    }

    public static Swagger getSwaggerObject(File file) throws IOException {
        return getSwaggerObject(new FileInputStream(file));
    }

    public static Map<String, Path> getSwaggerPaths(Swagger swaggerObject){
        return swaggerObject.getPaths();
    }

    public static void printSwaggerPaths(Swagger swaggerObject){
        log.info(""+swaggerObject.getInfo().getTitle());
        for(String key:swaggerObject.getPaths().keySet()){
            Path path = swaggerObject.getPaths().get(key);
            path.getOperations().forEach(op -> {log.info(op.getSummary());});
        }
    }

    public static void printSwaggerSecurity(Swagger swaggerObject){
        log.info("Security info for {}",swaggerObject.getInfo().getTitle());
        for(String key:swaggerObject.getSecurityDefinitions().keySet()){
            SecuritySchemeDefinition securitySchemeDefinition = swaggerObject.getSecurityDefinitions().get(key);
            log.info("Type:{}", securitySchemeDefinition.getType());
        }
    }

}
