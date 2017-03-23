package com.t1t.apim.rest.resources;

import com.t1t.apim.beans.brandings.NewServiceBrandingBean;
import com.t1t.apim.beans.brandings.ServiceBrandingBean;
import com.t1t.apim.exceptions.NotAuthorizedException;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public interface IBrandingResource {

    public ServiceBrandingBean getServiceBranding(String id);

    public ServiceBrandingBean createNewServiceBranding(NewServiceBrandingBean branding) throws NotAuthorizedException;

    public void deleteServiceBranding(String id) throws NotAuthorizedException;

    public Set<ServiceBrandingBean> getAllServiceBrandings();
}
