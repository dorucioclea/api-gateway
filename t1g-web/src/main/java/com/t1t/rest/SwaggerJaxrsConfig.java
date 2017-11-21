package com.t1t.rest;

import com.t1t.apim.AppConfigBean;
import com.t1t.apim.T1G;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.*;

import javax.inject.Inject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "SwaggerJaxrsConfig", loadOnStartup = 2)
public class SwaggerJaxrsConfig extends HttpServlet {

    @Inject
    @T1G
    private AppConfigBean config;

    @Override
    public void init(ServletConfig servletConfig) {
        try {
            super.init(servletConfig);
            BeanConfig beanConfig = new BeanConfig();
            beanConfig.setTitle("Trust1Gateway");
            beanConfig.setVersion(config.getVersion());
            beanConfig.setBasePath("t1g-web/v1");
            beanConfig.setResourcePackage("com.t1t.rest.resources");
            beanConfig.setScan(true);

            //information
            Info info = new Info()
                    .title("Trust1Gateway")
                    .description("Description")
                    .termsOfService("TERMS")
                    .contact(new Contact().email("info@trust1team.com"))
                    .license(new License().name("Trust1Gateway").url("license@url.com"));
            ServletContext context = servletConfig.getServletContext();

            List<Scheme> schemes = new ArrayList<>();
            schemes.add(Scheme.HTTP);
            schemes.add(Scheme.HTTPS);

            //configuration
            Swagger swagger = new Swagger().info(info);
            swagger.externalDocs(new ExternalDocs("Find out more about the Trust1Gateway", "http://trust1team.com"));
            swagger.schemes(schemes);
            swagger.host("localhost:8080");
            swagger.basePath("t1g-web/v1");
            context.setAttribute("swagger", swagger);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}