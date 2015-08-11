package com.t1t.digipolis.apim.gateway.rest;

import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.gateway.i18n.Messages;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.InputStream;
import java.net.URI;

/**
 * A REST client for accessing the Gateway API.
 */
@SuppressWarnings("javadoc") // class is temporarily delinked from its interfaces
public class GatewayClient /*implements ISystemResource, IServiceResource, IApplicationResource*/ {
    
    private static final String SYSTEM_STATUS = "/system/status"; //$NON-NLS-1$
    private static final String SERVICES = "/services"; //$NON-NLS-1$
    private static final String APPLICATIONS = "/applications"; //$NON-NLS-1$
    
    private static final ObjectMapper mapper = new ObjectMapper();
    
    private String endpoint;
    private CloseableHttpClient httpClient;
    
    /**
     * Constructor.
     * @param endpoint the endpoint
     * @param httpClient the http client
     */
    public GatewayClient(String endpoint, CloseableHttpClient httpClient) {
        this.endpoint = endpoint;
        this.httpClient = httpClient;
        
        if (this.endpoint.endsWith("/")) { //$NON-NLS-1$
            this.endpoint = this.endpoint.substring(0, this.endpoint.length() - 1);
        }
    }

    /**
     * @see io.apiman.gateway.api.rest.contract.ISystemResource#getStatus()
     */
    public SystemStatus getStatus() throws GatewayAuthenticationException {
        InputStream is = null;
        try {
            URI uri = new URI(this.endpoint + SYSTEM_STATUS);
            HttpGet get = new HttpGet(uri);
            HttpResponse response = httpClient.execute(get);
            int actualStatusCode = response.getStatusLine().getStatusCode();
            if (actualStatusCode == 401 || actualStatusCode == 403) {
                throw new GatewayAuthenticationException();
            }
            if (actualStatusCode != 200) {
                throw new Exception("System status check failed: " + actualStatusCode); //$NON-NLS-1$
            }
            is = response.getEntity().getContent();
            return mapper.reader(SystemStatus.class).readValue(is);
        } catch (GatewayAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * @see io.apiman.gateway.api.rest.contract.IServiceResource#getServiceEndpoint(String, String, String)
     */
    public ServiceEndpoint getServiceEndpoint(String organizationId, String serviceId, String version)
            throws GatewayAuthenticationException {
        InputStream is = null;
        try {
            @SuppressWarnings("nls")
            URI uri = new URI(this.endpoint + SERVICES + "/" + organizationId + "/" + serviceId + "/" + version + "/endpoint");
            HttpGet get = new HttpGet(uri);
            HttpResponse response = httpClient.execute(get);
            int actualStatusCode = response.getStatusLine().getStatusCode();
            if (actualStatusCode == 401 || actualStatusCode == 403) {
                throw new GatewayAuthenticationException();
            }
            if (actualStatusCode != 200) {
                throw new Exception("Failed to get the service endpoint: " + actualStatusCode); //$NON-NLS-1$
            }
            is = response.getEntity().getContent();
            return mapper.reader(ServiceEndpoint.class).readValue(is);
        } catch (GatewayAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * @see io.apiman.gateway.api.rest.contract.IApplicationResource#register(io.apiman.gateway.engine.beans.Application)
     */
    public void register(Application application) throws RegistrationException, GatewayAuthenticationException {
        try {
            URI uri = new URI(this.endpoint + APPLICATIONS);
            HttpPut put = new HttpPut(uri);
            put.setHeader("Content-Type", "application/json; charset=utf-8"); //$NON-NLS-1$ //$NON-NLS-2$
            String jsonPayload = mapper.writer().writeValueAsString(application);
            HttpEntity entity = new StringEntity(jsonPayload);
            put.setEntity(entity);
            HttpResponse response = httpClient.execute(put);
            int actualStatusCode = response.getStatusLine().getStatusCode();
            if (actualStatusCode == 401 || actualStatusCode == 403) {
                throw new GatewayAuthenticationException();
            }
            if (actualStatusCode == 500) {
                Header[] headers = response.getHeaders("X-API-Gateway-Error"); //$NON-NLS-1$
                if (headers != null && headers.length > 0) {
                    RegistrationException re = readRegistrationException(response);
                    throw re;
                }
            }
            if (actualStatusCode >= 300) {
                throw new Exception(Messages.i18n.format("GatewayClient.AppRegistrationFailed", actualStatusCode)); //$NON-NLS-1$
            }
        } catch (RegistrationException|GatewayAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see io.apiman.gateway.api.rest.contract.IApplicationResource#unregister(String, String, String)
     */
    public void unregister(String organizationId, String applicationId, String version)
            throws RegistrationException, GatewayAuthenticationException {
        try {
            @SuppressWarnings("nls")
            URI uri = new URI(this.endpoint + APPLICATIONS + "/" + organizationId + "/" + applicationId + "/" + version);
            HttpDelete put = new HttpDelete(uri);
            HttpResponse response = httpClient.execute(put);
            int actualStatusCode = response.getStatusLine().getStatusCode();
            if (actualStatusCode == 401 || actualStatusCode == 403) {
                throw new GatewayAuthenticationException();
            }
            if (actualStatusCode == 500) {
                Header[] headers = response.getHeaders("X-API-Gateway-Error"); //$NON-NLS-1$
                if (headers != null && headers.length > 0) {
                    RegistrationException re = readRegistrationException(response);
                    throw re;
                }
            }
            if (actualStatusCode >= 300) {
                throw new Exception(Messages.i18n.format("GatewayClient.AppUnregistrationFailed", actualStatusCode)); //$NON-NLS-1$
            }
        } catch (RegistrationException|GatewayAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see io.apiman.gateway.api.rest.contract.IServiceResource#publish(io.apiman.gateway.engine.beans.Service)
     */
    public void publish(Service service) throws PublishingException, GatewayAuthenticationException {
        try {
            URI uri = new URI(this.endpoint + SERVICES);
            HttpPut put = new HttpPut(uri);
            put.setHeader("Content-Type", "application/json; charset=utf-8"); //$NON-NLS-1$ //$NON-NLS-2$
            String jsonPayload = mapper.writer().writeValueAsString(service);
            HttpEntity entity = new StringEntity(jsonPayload);
            put.setEntity(entity);
            HttpResponse response = httpClient.execute(put);
            int actualStatusCode = response.getStatusLine().getStatusCode();
            if (actualStatusCode == 401 || actualStatusCode == 403) {
                throw new GatewayAuthenticationException();
            }
            if (actualStatusCode == 500) {
                Header[] headers = response.getHeaders("X-API-Gateway-Error"); //$NON-NLS-1$
                if (headers != null && headers.length > 0) {
                    PublishingException pe = readPublishingException(response);
                    throw pe;
                }
            }
            if (actualStatusCode >= 300) {
                throw new Exception(Messages.i18n.format("GatewayClient.ServicePublishingFailed", actualStatusCode)); //$NON-NLS-1$
            }
        } catch (PublishingException|GatewayAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see io.apiman.gateway.api.rest.contract.IServiceResource#retire(String, String, String)
     */
    public void retire(String organizationId, String serviceId, String version) throws RegistrationException, GatewayAuthenticationException {
        try {
            @SuppressWarnings("nls")
            URI uri = new URI(this.endpoint + SERVICES + "/" + organizationId + "/" + serviceId + "/" + version);
            HttpDelete put = new HttpDelete(uri);
            HttpResponse response = httpClient.execute(put);
            int actualStatusCode = response.getStatusLine().getStatusCode();
            if (actualStatusCode == 401 || actualStatusCode == 403) {
                throw new GatewayAuthenticationException();
            }
            if (actualStatusCode == 500) {
                Header[] headers = response.getHeaders("X-API-Gateway-Error"); //$NON-NLS-1$
                if (headers != null && headers.length > 0) {
                    PublishingException pe = readPublishingException(response);
                    throw pe;
                }
            }
            if (actualStatusCode >= 300) {
                throw new Exception(Messages.i18n.format("GatewayClient.ServiceRetiringFailed", actualStatusCode)); //$NON-NLS-1$
            }
        } catch (PublishingException|GatewayAuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads a publishing exception from the response.
     * @param response
     */
    private PublishingException readPublishingException(HttpResponse response) {
        InputStream is = null;
        PublishingException exception;
        try {
            is = response.getEntity().getContent();
            GatewayAuthenticationException error = mapper.reader(GatewayAuthenticationException.class).readValue(is);
            exception = new PublishingException(error.getMessage());
            // TODO parse the stack trace and set it on the exception
        } catch (Exception e) {
            exception = new PublishingException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return exception;
    }

    /**
     * Reads a registration exception from the response body.
     * @param response
     */
    private RegistrationException readRegistrationException(HttpResponse response) {
        InputStream is = null;
        RegistrationException exception;
        try {
            is = response.getEntity().getContent();
            GatewayAuthenticationException error = mapper.reader(GatewayAuthenticationException.class).readValue(is);
            exception = new RegistrationException(error.getMessage());
            // TODO parse the stack trace and set it on the exception
        } catch (Exception e) {
            exception = new RegistrationException(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return exception;
    }

}
