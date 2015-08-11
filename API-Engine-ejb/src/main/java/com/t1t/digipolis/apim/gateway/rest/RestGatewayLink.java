package com.t1t.digipolis.apim.gateway.rest;

import com.t1t.digipolis.apim.beans.gateways.GatewayBean;
import com.t1t.digipolis.apim.beans.gateways.RestGatewayConfigBean;
import com.t1t.digipolis.apim.common.util.AesEncrypter;
import com.t1t.digipolis.apim.gateway.GatewayAuthenticationException;
import com.t1t.digipolis.apim.gateway.IGatewayLink;
import com.t1t.digipolis.apim.gateway.dto.Application;
import com.t1t.digipolis.apim.gateway.dto.Service;
import com.t1t.digipolis.apim.gateway.dto.ServiceEndpoint;
import com.t1t.digipolis.apim.gateway.dto.SystemStatus;
import com.t1t.digipolis.apim.gateway.dto.exceptions.PublishingException;
import com.t1t.digipolis.apim.gateway.dto.exceptions.RegistrationException;
import com.t1t.digipolis.apim.gateway.i18n.Messages;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * An implementation of a Gateway Link that uses the Gateway's simple REST
 * API to publish Services.
 */
public class RestGatewayLink implements IGatewayLink {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static HostnameVerifier allHostsValid;
    private static SSLConnectionSocketFactory sslConnectionFactory;
    static {
        allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            });
            sslConnectionFactory = new SSLConnectionSocketFactory(builder.build(), allHostsValid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    private GatewayBean gateway;
    private CloseableHttpClient httpClient;
    private GatewayClient gatewayClient;
    private RestGatewayConfigBean config;

    /**
     * Constructor.
     * @param gateway the gateway
     */
    public RestGatewayLink(final GatewayBean gateway) {
        try {
            this.gateway = gateway;
            String cfg = gateway.getConfiguration();
            setConfig((RestGatewayConfigBean) mapper.reader(RestGatewayConfigBean.class).readValue(cfg));
            getConfig().setPassword(AesEncrypter.decrypt(getConfig().getPassword()));
            httpClient = HttpClientBuilder.create()
                    .setSSLHostnameVerifier(allHostsValid)
                    .setSSLSocketFactory(sslConnectionFactory)
                    .addInterceptorFirst(new HttpRequestInterceptor() {
                        @Override
                        public void process(HttpRequest request, HttpContext context) throws HttpException,
                                IOException {
                            configureBasicAuth(request);
                        }
                    }).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see IGatewayLink#close()
     */
    @Override
    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            // TODO log the error?
        }
    }

    /**
     * Checks that the gateway is up.
     */
    private boolean isGatewayUp() throws GatewayAuthenticationException {
        SystemStatus status = getClient().getStatus();
        return status.isUp();
    }

    /**
     * @see IGatewayLink#getStatus()
     */
    @Override
    public SystemStatus getStatus() throws GatewayAuthenticationException {
        return getClient().getStatus();
    }

    /**
     * @see IGatewayLink#getServiceEndpoint(String, String, String)
     */
    @Override
    public ServiceEndpoint getServiceEndpoint(String organizationId, String serviceId, String version)
            throws GatewayAuthenticationException {
        return getClient().getServiceEndpoint(organizationId, serviceId, version);
    }

    /**
     * @see IGatewayLink#publishService(Service)
     */
    @Override
    public void publishService(Service service) throws PublishingException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new PublishingException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().publish(service);
    }

    /**
     * @see IGatewayLink#retireService(Service)
     */
    @Override
    public void retireService(Service service) throws PublishingException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new PublishingException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().retire(service.getOrganizationId(), service.getServiceId(), service.getVersion());
    }

    /**
     * @see IGatewayLink#registerApplication(Application)
     */
    @Override
    public void registerApplication(Application application) throws RegistrationException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new RegistrationException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().register(application);
    }

    /**
     * @see IGatewayLink#unregisterApplication(Application)
     */
    @Override
    public void unregisterApplication(Application application) throws RegistrationException, GatewayAuthenticationException {
        if (!isGatewayUp()) {
            throw new RegistrationException(Messages.i18n.format("RestGatewayLink.GatewayNotRunning")); //$NON-NLS-1$
        }
        getClient().unregister(application.getOrganizationId(), application.getApplicationId(), application.getVersion());
    }

    /**
     * Configures BASIC authentication for the request.
     * @param request
     */
    protected void configureBasicAuth(HttpRequest request) {
        try {
            String username = getConfig().getUsername();
            String password = getConfig().getPassword();
            String up = username + ":" + password; //$NON-NLS-1$
            String base64 = new String(Base64.encodeBase64(up.getBytes("UTF-8"))); //$NON-NLS-1$
            String authHeader = "Basic " + base64; //$NON-NLS-1$
            request.setHeader("Authorization", authHeader); //$NON-NLS-1$
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the gateway client
     */
    protected GatewayClient getClient() {
        if (gatewayClient == null) {
            gatewayClient = createClient();
        }
        return gatewayClient;
    }

    /**
     * @return a newly created rest gateway client
     */
    private GatewayClient createClient() {
        String gatewayEndpoint = getConfig().getEndpoint();
        return new GatewayClient(gatewayEndpoint, httpClient);
    }

    /**
     * @return the config
     */
    public RestGatewayConfigBean getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(RestGatewayConfigBean config) {
        this.config = config;
    }

}
