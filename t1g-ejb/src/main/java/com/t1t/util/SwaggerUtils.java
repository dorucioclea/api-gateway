package com.t1t.util;

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
}