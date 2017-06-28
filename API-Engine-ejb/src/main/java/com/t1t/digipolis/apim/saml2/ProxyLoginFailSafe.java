package com.t1t.digipolis.apim.saml2;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.t1t.digipolis.apim.idp.IDPClient;
import retrofit.client.Response;

/**
 * Created by michallispashidis on 2/12/15.
 */
public class ProxyLoginFailSafe extends HystrixCommand<retrofit.client.Response> {
    private final IDPClient idpClient;
    private final String username;
    private final String password;

    public ProxyLoginFailSafe(IDPClient idpClient, String username, String password){
        super(HystrixCommandGroupKey.Factory.asKey("IDPProxyLoginGroup"));
        this.idpClient=idpClient;
        this.username=username;
        this.password=password;
    }

    @Override
    protected Response run() throws Exception {
        return idpClient.authenticateUser("password", username, password, "authenticate");
    }

    @Override
    protected Response getFallback() {
        return null;
    }
}
