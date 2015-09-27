package com.t1t.digipolis.apim.auth.controller;

import com.google.common.base.Preconditions;
import com.t1t.digipolis.apim.beans.authorization.OAuthApplicationResponse;
import com.t1t.digipolis.apim.beans.services.ServiceVersionBean;
import com.t1t.digipolis.apim.facades.OAuthFacade;
import com.t1t.digipolis.apim.facades.OrganizationFacade;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by michallispashidis on 23/09/15.
 */
@ManagedBean(name = "authController", eager = true)
@RequestScoped
public class OAuth2Controller implements Serializable {
    @Inject
    private FacesContext facesContext;
    @Inject
    private OrganizationFacade organizationFacade;
    @Inject private OAuthFacade oAuthFacade;

    private String appName;
    private String userName;
    private String appVersion;
    private String base64AppLogo;
    private List<String> scopes;

    //this managed property will read value from request parameter pageId
    @ManagedProperty(value = "#{param.client_id}")
    private String clientId;

    @ManagedProperty(value = "#{param.response_type}")
    private String responseType;

    @ManagedProperty(value = "#{param.org_id}")
    private String orgId;

    @ManagedProperty(value = "#{param.service_id}")
    private String serviceId;

    @ManagedProperty(value = "#{param.version}")
    private String version;

    @PostConstruct
    private void init() {
        //get service
        Preconditions.checkArgument(!StringUtils.isEmpty(getClientId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(getOrgId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(getServiceId()));
        Preconditions.checkArgument(!StringUtils.isEmpty(getVersion()));
        //ServiceVersionBean serviceVersion = organizationFacade.getServiceVersion(orgId, serviceId, version);
        OAuthApplicationResponse applicationOAuthInformation = oAuthFacade.getApplicationOAuthInformation(getClientId(), getOrgId(), getServiceId(), getVersion());
        System.out.println("APP: "+applicationOAuthInformation.toString());
        setAppName(applicationOAuthInformation.getConsumerResponse().getName());
        setUserName(applicationOAuthInformation.getConsumer().getUsername());
        setAppVersion(applicationOAuthInformation.getAppVersion());
        setBase64AppLogo(applicationOAuthInformation.getBase64AppLogo());
        Map<String, String> scopes = applicationOAuthInformation.getScopes();
        List<String> txScopes = new ArrayList<>();
        for(String scope:scopes.keySet()){
            txScopes.add(scope+": "+scopes.get(scope));
        }
        setScopes(txScopes);
    }

    public void onButtonCancel(){
        HttpServletResponse response = (HttpServletResponse)facesContext.getExternalContext().getResponse();
        try {
            response.sendRedirect("http://www.google.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onButtonGrant(){
        HttpServletResponse response = (HttpServletResponse)facesContext.getExternalContext().getResponse();
        try {
            response.sendRedirect("http://www.google.com");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getBase64AppLogo() {
        return base64AppLogo;
    }

    public void setBase64AppLogo(String base64AppLogo) {
        this.base64AppLogo = base64AppLogo;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
