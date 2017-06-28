package com.t1t.digipolis.apim.beans.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceAccountTokenRequest implements Serializable {

    private String impersonateUser;

    public String getImpersonateUser() {
        return impersonateUser;
    }

    public void setImpersonateUser(String impersonateUser) {
        this.impersonateUser = impersonateUser;
    }

    @Override
    public String toString() {
        return "ServiceAccountTokenRequest{" +
                "impersonateUser='" + impersonateUser + '\'' +
                '}';
    }
}