package com.t1t.apim.common.config.options;

import java.util.Map;

/**
 * Options parser for BASIC authentication endpoint security.
 */
public class BasicAuthOptions extends AbstractOptions {
    public static final String PREFIX = "basic-auth."; //$NON-NLS-1$
    public static final String BASIC_USERNAME = PREFIX + "username"; //$NON-NLS-1$
    public static final String BASIC_PASSWORD = PREFIX + "password"; //$NON-NLS-1$
    public static final String BASIC_REQUIRE_SSL = PREFIX + "requireSSL"; //$NON-NLS-1$

    private String username;
    private String password;
    private boolean requireSSL;

    /**
     * Constructor. Parses options immediately.
     *
     * @param options the options
     */
    public BasicAuthOptions(Map<String, String> options) {
        super(options);
    }

    /**
     * @see AbstractOptions#parse(Map)
     */
    @Override
    protected void parse(Map<String, String> options) {
        setUsername(getVar(options, BASIC_USERNAME));
        setPassword(getVar(options, BASIC_PASSWORD));
        setRequireSSL(parseBool(options, BASIC_REQUIRE_SSL, true));
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the requireSSL
     */
    public boolean isRequireSSL() {
        return requireSSL;
    }

    /**
     * @param requireSSL the requireSSL to set
     */
    public void setRequireSSL(boolean requireSSL) {
        this.requireSSL = requireSSL;
    }
}
