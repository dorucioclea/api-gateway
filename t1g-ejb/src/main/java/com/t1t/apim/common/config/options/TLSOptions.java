package com.t1t.apim.common.config.options;

import java.util.Map;

/**
 * Options parser for TLS/SSL.
 *
 */
public class TLSOptions extends AbstractOptions {
    public static final String PREFIX = "tls."; //$NON-NLS-1$
    public static final String TLS_TRUSTSTORE = PREFIX + "trustStore"; //$NON-NLS-1$
    public static final String TLS_TRUSTSTOREPASSWORD = PREFIX + "trustStorePassword"; //$NON-NLS-1$
    public static final String TLS_KEYSTORE = PREFIX + "keyStore"; //$NON-NLS-1$
    public static final String TLS_KEYSTOREPASSWORD = PREFIX + "keystorePassword"; //$NON-NLS-1$
    public static final String TLS_KEYALIASES = PREFIX + "keyAliases"; //$NON-NLS-1$
    public static final String TLS_KEYPASSWORD = PREFIX + "keyPassword"; //$NON-NLS-1$
    public static final String TLS_ALLOWEDPROTOCOLS = PREFIX + "allowedProtocols"; //$NON-NLS-1$
    public static final String TLS_ALLOWEDCIPHERS = PREFIX + "allowedCiphers"; //$NON-NLS-1$
    public static final String TLS_ALLOWANYHOST = PREFIX + "allowAnyHost"; //$NON-NLS-1$
    public static final String TLS_ALLOWSELFSIGNED = PREFIX + "allowSelfSigned"; //$NON-NLS-1$
    public static final String TLS_DEVMODE = PREFIX + "devMode"; //$NON-NLS-1$

    private String trustStore;
    private String trustStorePassword;
    private String clientKeyStore;
    private String keyStorePassword;
    private String keyPassword;
    private String[] allowedProtocols;
    private String[] allowedCiphers;
    private boolean allowAnyHost;
    private boolean trustSelfSigned;
    private boolean devMode;
    private String[] keyAliases;

    /**
     * Constructor. Parses options immediately.
     * @param options the options
     */
    public TLSOptions(Map<String, String> options) {
        super(options);
    }

    /**
     * @see AbstractOptions#parse(Map)
     */
    @Override
    protected void parse(Map<String, String> options) {
        trustStore = getVar(options, TLS_TRUSTSTORE);
        trustStorePassword = getVar(options, TLS_TRUSTSTOREPASSWORD);
        clientKeyStore = getVar(options, TLS_KEYSTORE);
        keyStorePassword = getVar(options, TLS_KEYSTOREPASSWORD);
        keyAliases = split(getVar(options, TLS_KEYALIASES), ',');
        keyPassword = getVar(options, TLS_KEYPASSWORD);
        allowedProtocols = split(getVar(options, TLS_ALLOWEDPROTOCOLS), ',');
        allowedCiphers = split(getVar(options, TLS_ALLOWEDCIPHERS), ',');
        allowAnyHost = parseBool(options, TLS_ALLOWANYHOST);
        trustSelfSigned = parseBool(options, TLS_ALLOWSELFSIGNED);
        devMode = parseBool(options, TLS_DEVMODE);
    }

    /**
     * @return the trustStore
     */
    public String getTrustStore() {
        return trustStore;
    }

    /**
     * @param trustStore the trustStore to set
     */
    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    /**
     * @return the trustStorePassword
     */
    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * @param trustStorePassword the trustStorePassword to set
     */
    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    /**
     * @return the clientKeyStore
     */
    public String getkeyStore() {
        return clientKeyStore;
    }

    /**
     * @param clientKeyStore the clientKeyStore to set
     */
    public void setClientKeyStore(String clientKeyStore) {
        this.clientKeyStore = clientKeyStore;
    }

    /**
     * @return the keyStorePassword
     */
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    /**
     * @param keyStorePassword the keyStorePassword to set
     */
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    /**
     * @return the keyPassword
     */
    public String getKeyPassword() {
        return keyPassword;
    }

    /**
     * @param keyPassword the keyPassword to set
     */
    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    /**
     * @return the allowedProtocols
     */
    public String[] getAllowedProtocols() {
        return allowedProtocols;
    }

    /**
     * @param allowedProtocols the allowedProtocols to set
     */
    public void setAllowedProtocols(String[] allowedProtocols) {
        this.allowedProtocols = allowedProtocols;
    }

    /**
     * @return the allowedCiphers
     */
    public String[] getAllowedCiphers() {
        return allowedCiphers;
    }

    /**
     * @param allowedCiphers the allowedCiphers to set
     */
    public void setAllowedCiphers(String[] allowedCiphers) {
        this.allowedCiphers = allowedCiphers;
    }

    /**
     * @return the allowAnyHost
     */
    public boolean isAllowAnyHost() {
        return allowAnyHost;
    }

    /**
     * @param allowAnyHost the allowAnyHost to set
     */
    public void setAllowAnyHost(boolean allowAnyHost) {
        this.allowAnyHost = allowAnyHost;
    }

    /**
     * @return the trustSelfSigned
     */
    public boolean isTrustSelfSigned() {
        return trustSelfSigned;
    }

    /**
     * @param allowSelfSigned the allowSelfSigned to set
     */
    public void setAllowSelfSigned(boolean allowSelfSigned) {
        this.trustSelfSigned = allowSelfSigned;
    }

    /**
     * @return the devMode
     */
    public boolean isDevMode() {
        return devMode;
    }

    /**
     * @param devMode the devMode to set
     */
    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    /**
     * @return the keyAliases
     */
    public String[] getKeyAliases() {
        return keyAliases;
    }

    /**
     * @param keyAliases the keyAliases to set
     */
    public void setKeyAliases(String[] keyAliases) {
        this.keyAliases = keyAliases;
    }
}
