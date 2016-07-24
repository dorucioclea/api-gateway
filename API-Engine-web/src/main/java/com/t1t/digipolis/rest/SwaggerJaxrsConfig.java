package com.t1t.digipolis.rest;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.net.InetAddress;
import java.net.UnknownHostException;

@WebServlet(name = "SwaggerJaxrsConfig", loadOnStartup = 2)
public class SwaggerJaxrsConfig extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) {
        try {
            super.init(servletConfig);
            BeanConfig beanConfig = new BeanConfig();
            beanConfig.setTitle("API Engine");
            beanConfig.setVersion("v1");
            beanConfig.setBasePath("API-Engine-web/v1");
            beanConfig.setResourcePackage("com.t1t.digipolis.rest.resources");
            beanConfig.setScan(true);

            //information
            Info info = new Info()
                    .title("API Engine")
                    .description("Description")
                    .termsOfService("TERMS")
                    .contact(new Contact().email("info@digipolis.be"))
                    .license(new License().name("API-Engine").url("license@url.com"));
            ServletContext context = servletConfig.getServletContext();

            //configuration
            Swagger swagger = new Swagger().info(info);
            swagger.externalDocs(new ExternalDocs("Find out more about the API Engine", "http://trust1team.com"));
            swagger.scheme(Scheme.HTTP);
            swagger.host("localhost:8080");
            swagger.basePath("api-engine/v1");
            context.setAttribute("swagger", swagger);
        } catch (ServletException e) {
            e.printStackTrace();
        }

    }

    public static String getWebappUrl(ServletConfig servletConfig, boolean ssl) {
        String protocol = ssl ? "https" : "http";
        String host = getHostName();
        String context = servletConfig.getServletContext().getServletContextName();
        return protocol + "://" + host + "/" + context;
    }

    public static String getHostName() {
        String[] hostnames = getHostNames();
        if (hostnames.length == 0) return "localhost";
        if (hostnames.length == 1) return hostnames[0];
        for (int i = 0; i < hostnames.length; i++) {
            if (!"localhost".equals(hostnames[i])) return hostnames[i];
        }
        return hostnames[0];
    }

    public static String[] getHostNames() {
        String localhostName;
        try {
            localhostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return new String[]{"localhost"};
        }
        InetAddress ia[];
        try {
            ia = InetAddress.getAllByName(localhostName);
        } catch (UnknownHostException ex) {
            return new String[]{localhostName};
        }
        String[] sa = new String[ia.length];
        for (int i = 0; i < ia.length; i++) {
            sa[i] = ia[i].getHostName();
        }
        return sa;
    }
}