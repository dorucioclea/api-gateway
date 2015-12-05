package com.t1t.digipolis.apim.idp;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import java.io.UnsupportedEncodingException;

/**
 * Created by michallispashidis on 07/08/2015.
 * Application scoped bean, adding the header information to server instance call.
 */
public class IDPRestServiceBuilder {
    private static Logger _LOG = LoggerFactory.getLogger(IDPRestServiceBuilder.class.getName());

    public IDPRestServiceBuilder() {
    }

    /**
     * Provides the basic authentication header based on the username and password provided in the configuration.
     *
     * @param config
     * @return
     */
    private static synchronized String getBasicAuthValue(RestIDPConfigBean config) {
        String authHeader = "";
        try {
            String username = config.getUsername();
            String password = config.getPassword();
            String up = username + ":" + password; //$NON-NLS-1$
            String base64 = null; //$NON-NLS-1$
            base64 = new String(Base64.encodeBase64(up.getBytes("UTF-8")));
            authHeader = "Basic " + base64; //$NON-NLS-1$
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return authHeader;
    }

    /**
     * Returns the service requestes throught the restAdapter.
     *
     * @param iFace
     * @param <T>
     * @return
     */
    public <T> T getSecureService(RestIDPConfigBean config, Class<T> iFace) {
        //optional GSON converter
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        StringBuilder url = new StringBuilder(config.getEndpoint());
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(url.toString())
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        _LOG.info("retrofit - IDP direct request:{}",msg);
                    }
                })
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade requestFacade) {
                        String authHeader = getBasicAuthValue(config);
                        requestFacade.addHeader("Authorization", getBasicAuthValue(config));
                    }
                })
                //.setClient(getSafeClient())
                .build();
        _LOG.info("IDP connection string:{}", url.toString());
        return restAdapter.create(iFace);
    }

/*    private OkClient getSafeClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
*//*            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // TODO Auto-generated method stub
                    if (hostname.еquals("https://somevendor.com"))
                        return true;
                    else
                        return false;
                }
            });*//*

            return new OkClient(okHttpClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }*/

}
