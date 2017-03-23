package com.t1t.apim.beans.actions;

import java.io.Serializable;

/**
 * @author Maarten Somers
 * @since 2015
 */
public class SwaggerDocBean implements Serializable {

    private static final long serialVersionUID = -3701724587199641219L;

    private String swaggerURI;
    private String swaggerDoc;

    /**
     * Constructor.
     */
    public SwaggerDocBean() {
    }

    /**
     * @return the swagger documentation URI
     */
    public String getSwaggerURI() {
        return swaggerURI;
    }

    /**
     * @param swaggerURI the swagger documentation URI to set
     */
    public void setSwaggerURI(String swaggerURI) {
        this.swaggerURI = swaggerURI;
    }

    /**
     * @return the swagger documentation JSON string
     */
    public String getSwaggerDoc() {
        return swaggerDoc;
    }

    /**
     * @param swaggerDoc the swagger documentation JSON string to set
     */
    public void setSwaggerDoc(String swaggerDoc) {
        this.swaggerDoc = swaggerDoc;
    }
}