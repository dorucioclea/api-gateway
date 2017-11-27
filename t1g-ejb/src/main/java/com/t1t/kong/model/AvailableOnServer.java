
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class AvailableOnServer {

    @SerializedName("key-auth")
    @Expose
    private Object keyAuth;
    @SerializedName("ldap-auth")
    @Expose
    private Object ldapAuth;
    @SerializedName("hal")
    @Expose
    private Object hal;
    @SerializedName("cors")
    @Expose
    private Object cors;
    @SerializedName("jwt-up")
    @Expose
    private Object jwtUp;
    @SerializedName("jwt")
    @Expose
    private Object jwt;
    @SerializedName("request-transformer")
    @Expose
    private Object requestTransformer;
    @SerializedName("json-threat-protection")
    @Expose
    private Object jsonThreatProtection;
    @SerializedName("oauth2")
    @Expose
    private Object oauth2;
    @SerializedName("http-log")
    @Expose
    private Object httpLog;
    @SerializedName("acl")
    @Expose
    private Object acl;
    @SerializedName("syslog")
    @Expose
    private Object syslog;
    @SerializedName("rate-limiting")
    @Expose
    private Object rateLimiting;
    @SerializedName("correlation-id")
    @Expose
    private Object correlationId;
    @SerializedName("udp-log")
    @Expose
    private Object udpLog;
    @SerializedName("runscope")
    @Expose
    private Object runscope;
    @SerializedName("tcp-log")
    @Expose
    private Object tcpLog;
    @SerializedName("hmac-auth")
    @Expose
    private Object hmacAuth;
    @SerializedName("response-transformer")
    @Expose
    private Object responseTransformer;
    @SerializedName("basic-auth")
    @Expose
    private Object basicAuth;
    @SerializedName("galileo")
    @Expose
    private Object galileo;
    @SerializedName("file-log")
    @Expose
    private Object fileLog;
    @SerializedName("ip-restriction")
    @Expose
    private Object ipRestriction;
    @SerializedName("request-size-limiting")
    @Expose
    private Object requestSizeLimiting;
    @SerializedName("ssl")
    @Expose
    private Object ssl;
    @SerializedName("datadog")
    @Expose
    private Object datadog;
    @SerializedName("loggly")
    @Expose
    private Object loggly;
    @SerializedName("statsd")
    @Expose
    private Object statsd;
    @SerializedName("response-ratelimiting")
    @Expose
    private Object responseRatelimiting;
    @SerializedName("bot-detection")
    @Expose
    private Object botDetection;

    /**
     * 
     * @return
     *     The keyAuth
     */
    public Object getKeyAuth() {
        return keyAuth;
    }

    /**
     * 
     * @param keyAuth
     *     The key-auth
     */
    public void setKeyAuth(Object keyAuth) {
        this.keyAuth = keyAuth;
    }

    public AvailableOnServer withKeyAuth(Object keyAuth) {
        this.keyAuth = keyAuth;
        return this;
    }

    /**
     * 
     * @return
     *     The ldapAuth
     */
    public Object getLdapAuth() {
        return ldapAuth;
    }

    /**
     * 
     * @param ldapAuth
     *     The ldap-auth
     */
    public void setLdapAuth(Object ldapAuth) {
        this.ldapAuth = ldapAuth;
    }

    public AvailableOnServer withLdapAuth(Object ldapAuth) {
        this.ldapAuth = ldapAuth;
        return this;
    }

    /**
     * 
     * @return
     *     The hal
     */
    public Object getHal() {
        return hal;
    }

    /**
     * 
     * @param hal
     *     The hal
     */
    public void setHal(Object hal) {
        this.hal = hal;
    }

    public AvailableOnServer withHal(Object hal) {
        this.hal = hal;
        return this;
    }

    /**
     * 
     * @return
     *     The cors
     */
    public Object getCors() {
        return cors;
    }

    /**
     * 
     * @param cors
     *     The cors
     */
    public void setCors(Object cors) {
        this.cors = cors;
    }

    public AvailableOnServer withCors(Object cors) {
        this.cors = cors;
        return this;
    }

    /**
     * 
     * @return
     *     The jwtUp
     */
    public Object getJwtUp() {
        return jwtUp;
    }

    /**
     * 
     * @param jwtUp
     *     The jwt-up
     */
    public void setJwtUp(Object jwtUp) {
        this.jwtUp = jwtUp;
    }

    public AvailableOnServer withJwtUp(Object jwtUp) {
        this.jwtUp = jwtUp;
        return this;
    }

    /**
     * 
     * @return
     *     The jwt
     */
    public Object getJwt() {
        return jwt;
    }

    /**
     * 
     * @param jwt
     *     The jwt
     */
    public void setJwt(Object jwt) {
        this.jwt = jwt;
    }

    public AvailableOnServer withJwt(Object jwt) {
        this.jwt = jwt;
        return this;
    }

    /**
     * 
     * @return
     *     The requestTransformer
     */
    public Object getRequestTransformer() {
        return requestTransformer;
    }

    /**
     * 
     * @param requestTransformer
     *     The request-transformer
     */
    public void setRequestTransformer(Object requestTransformer) {
        this.requestTransformer = requestTransformer;
    }

    public AvailableOnServer withRequestTransformer(Object requestTransformer) {
        this.requestTransformer = requestTransformer;
        return this;
    }

    /**
     * 
     * @return
     *     The jsonThreatProtection
     */
    public Object getJsonThreatProtection() {
        return jsonThreatProtection;
    }

    /**
     * 
     * @param jsonThreatProtection
     *     The json-threat-protection
     */
    public void setJsonThreatProtection(Object jsonThreatProtection) {
        this.jsonThreatProtection = jsonThreatProtection;
    }

    public AvailableOnServer withJsonThreatProtection(Object jsonThreatProtection) {
        this.jsonThreatProtection = jsonThreatProtection;
        return this;
    }

    /**
     * 
     * @return
     *     The oauth2
     */
    public Object getOauth2() {
        return oauth2;
    }

    /**
     * 
     * @param oauth2
     *     The oauth2
     */
    public void setOauth2(Object oauth2) {
        this.oauth2 = oauth2;
    }

    public AvailableOnServer withOauth2(Object oauth2) {
        this.oauth2 = oauth2;
        return this;
    }

    /**
     * 
     * @return
     *     The httpLog
     */
    public Object getHttpLog() {
        return httpLog;
    }

    /**
     * 
     * @param httpLog
     *     The http-log
     */
    public void setHttpLog(Object httpLog) {
        this.httpLog = httpLog;
    }

    public AvailableOnServer withHttpLog(Object httpLog) {
        this.httpLog = httpLog;
        return this;
    }

    /**
     * 
     * @return
     *     The acl
     */
    public Object getAcl() {
        return acl;
    }

    /**
     * 
     * @param acl
     *     The acl
     */
    public void setAcl(Object acl) {
        this.acl = acl;
    }

    public AvailableOnServer withAcl(Object acl) {
        this.acl = acl;
        return this;
    }

    /**
     * 
     * @return
     *     The syslog
     */
    public Object getSyslog() {
        return syslog;
    }

    /**
     * 
     * @param syslog
     *     The syslog
     */
    public void setSyslog(Object syslog) {
        this.syslog = syslog;
    }

    public AvailableOnServer withSyslog(Object syslog) {
        this.syslog = syslog;
        return this;
    }

    /**
     * 
     * @return
     *     The rateLimiting
     */
    public Object getRateLimiting() {
        return rateLimiting;
    }

    /**
     * 
     * @param rateLimiting
     *     The rate-limiting
     */
    public void setRateLimiting(Object rateLimiting) {
        this.rateLimiting = rateLimiting;
    }

    public AvailableOnServer withRateLimiting(Object rateLimiting) {
        this.rateLimiting = rateLimiting;
        return this;
    }

    /**
     * 
     * @return
     *     The correlationId
     */
    public Object getCorrelationId() {
        return correlationId;
    }

    /**
     * 
     * @param correlationId
     *     The correlation-id
     */
    public void setCorrelationId(Object correlationId) {
        this.correlationId = correlationId;
    }

    public AvailableOnServer withCorrelationId(Object correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    /**
     * 
     * @return
     *     The udpLog
     */
    public Object getUdpLog() {
        return udpLog;
    }

    /**
     * 
     * @param udpLog
     *     The udp-log
     */
    public void setUdpLog(Object udpLog) {
        this.udpLog = udpLog;
    }

    public AvailableOnServer withUdpLog(Object udpLog) {
        this.udpLog = udpLog;
        return this;
    }

    /**
     * 
     * @return
     *     The runscope
     */
    public Object getRunscope() {
        return runscope;
    }

    /**
     * 
     * @param runscope
     *     The runscope
     */
    public void setRunscope(Object runscope) {
        this.runscope = runscope;
    }

    public AvailableOnServer withRunscope(Object runscope) {
        this.runscope = runscope;
        return this;
    }

    /**
     * 
     * @return
     *     The tcpLog
     */
    public Object getTcpLog() {
        return tcpLog;
    }

    /**
     * 
     * @param tcpLog
     *     The tcp-log
     */
    public void setTcpLog(Object tcpLog) {
        this.tcpLog = tcpLog;
    }

    public AvailableOnServer withTcpLog(Object tcpLog) {
        this.tcpLog = tcpLog;
        return this;
    }

    /**
     * 
     * @return
     *     The hmacAuth
     */
    public Object getHmacAuth() {
        return hmacAuth;
    }

    /**
     * 
     * @param hmacAuth
     *     The hmac-auth
     */
    public void setHmacAuth(Object hmacAuth) {
        this.hmacAuth = hmacAuth;
    }

    public AvailableOnServer withHmacAuth(Object hmacAuth) {
        this.hmacAuth = hmacAuth;
        return this;
    }

    /**
     * 
     * @return
     *     The responseTransformer
     */
    public Object getResponseTransformer() {
        return responseTransformer;
    }

    /**
     * 
     * @param responseTransformer
     *     The response-transformer
     */
    public void setResponseTransformer(Object responseTransformer) {
        this.responseTransformer = responseTransformer;
    }

    public AvailableOnServer withResponseTransformer(Object responseTransformer) {
        this.responseTransformer = responseTransformer;
        return this;
    }

    /**
     * 
     * @return
     *     The basicAuth
     */
    public Object getBasicAuth() {
        return basicAuth;
    }

    /**
     * 
     * @param basicAuth
     *     The basic-auth
     */
    public void setBasicAuth(Object basicAuth) {
        this.basicAuth = basicAuth;
    }

    public AvailableOnServer withBasicAuth(Object basicAuth) {
        this.basicAuth = basicAuth;
        return this;
    }

    /**
     * 
     * @return
     *     The galileo
     */
    public Object getGalileo() {
        return galileo;
    }

    /**
     * 
     * @param galileo
     *     The galileo
     */
    public void setGalileo(Object galileo) {
        this.galileo = galileo;
    }

    public AvailableOnServer withGalileo(Object galileo) {
        this.galileo = galileo;
        return this;
    }

    /**
     * 
     * @return
     *     The fileLog
     */
    public Object getFileLog() {
        return fileLog;
    }

    /**
     * 
     * @param fileLog
     *     The file-log
     */
    public void setFileLog(Object fileLog) {
        this.fileLog = fileLog;
    }

    public AvailableOnServer withFileLog(Object fileLog) {
        this.fileLog = fileLog;
        return this;
    }

    /**
     * 
     * @return
     *     The ipRestriction
     */
    public Object getIpRestriction() {
        return ipRestriction;
    }

    /**
     * 
     * @param ipRestriction
     *     The ip-restriction
     */
    public void setIpRestriction(Object ipRestriction) {
        this.ipRestriction = ipRestriction;
    }

    public AvailableOnServer withIpRestriction(Object ipRestriction) {
        this.ipRestriction = ipRestriction;
        return this;
    }

    /**
     * 
     * @return
     *     The requestSizeLimiting
     */
    public Object getRequestSizeLimiting() {
        return requestSizeLimiting;
    }

    /**
     * 
     * @param requestSizeLimiting
     *     The request-size-limiting
     */
    public void setRequestSizeLimiting(Object requestSizeLimiting) {
        this.requestSizeLimiting = requestSizeLimiting;
    }

    public AvailableOnServer withRequestSizeLimiting(Object requestSizeLimiting) {
        this.requestSizeLimiting = requestSizeLimiting;
        return this;
    }

    /**
     * 
     * @return
     *     The ssl
     */
    public Object getSsl() {
        return ssl;
    }

    /**
     * 
     * @param ssl
     *     The ssl
     */
    public void setSsl(Object ssl) {
        this.ssl = ssl;
    }

    public AvailableOnServer withSsl(Object ssl) {
        this.ssl = ssl;
        return this;
    }

    /**
     * 
     * @return
     *     The datadog
     */
    public Object getDatadog() {
        return datadog;
    }

    /**
     * 
     * @param datadog
     *     The datadog
     */
    public void setDatadog(Object datadog) {
        this.datadog = datadog;
    }

    public AvailableOnServer withDatadog(Object datadog) {
        this.datadog = datadog;
        return this;
    }

    /**
     * 
     * @return
     *     The loggly
     */
    public Object getLoggly() {
        return loggly;
    }

    /**
     * 
     * @param loggly
     *     The loggly
     */
    public void setLoggly(Object loggly) {
        this.loggly = loggly;
    }

    public AvailableOnServer withLoggly(Object loggly) {
        this.loggly = loggly;
        return this;
    }

    /**
     * 
     * @return
     *     The statsd
     */
    public Object getStatsd() {
        return statsd;
    }

    /**
     * 
     * @param statsd
     *     The statsd
     */
    public void setStatsd(Object statsd) {
        this.statsd = statsd;
    }

    public AvailableOnServer withStatsd(Object statsd) {
        this.statsd = statsd;
        return this;
    }

    /**
     * 
     * @return
     *     The responseRatelimiting
     */
    public Object getResponseRatelimiting() {
        return responseRatelimiting;
    }

    /**
     * 
     * @param responseRatelimiting
     *     The response-ratelimiting
     */
    public void setResponseRatelimiting(Object responseRatelimiting) {
        this.responseRatelimiting = responseRatelimiting;
    }

    public AvailableOnServer withResponseRatelimiting(Object responseRatelimiting) {
        this.responseRatelimiting = responseRatelimiting;
        return this;
    }

    /**
     * 
     * @return
     *     The botDetection
     */
    public Object getBotDetection() {
        return botDetection;
    }

    /**
     * 
     * @param botDetection
     *     The bot-detection
     */
    public void setBotDetection(Object botDetection) {
        this.botDetection = botDetection;
    }

    public AvailableOnServer withBotDetection(Object botDetection) {
        this.botDetection = botDetection;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(keyAuth).append(ldapAuth).append(hal).append(cors).append(jwtUp).append(jwt).append(requestTransformer).append(jsonThreatProtection).append(oauth2).append(httpLog).append(acl).append(syslog).append(rateLimiting).append(correlationId).append(udpLog).append(runscope).append(tcpLog).append(hmacAuth).append(responseTransformer).append(basicAuth).append(galileo).append(fileLog).append(ipRestriction).append(requestSizeLimiting).append(ssl).append(datadog).append(loggly).append(statsd).append(responseRatelimiting).append(botDetection).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AvailableOnServer) == false) {
            return false;
        }
        AvailableOnServer rhs = ((AvailableOnServer) other);
        return new EqualsBuilder().append(keyAuth, rhs.keyAuth).append(ldapAuth, rhs.ldapAuth).append(hal, rhs.hal).append(cors, rhs.cors).append(jwtUp, rhs.jwtUp).append(jwt, rhs.jwt).append(requestTransformer, rhs.requestTransformer).append(jsonThreatProtection, rhs.jsonThreatProtection).append(oauth2, rhs.oauth2).append(httpLog, rhs.httpLog).append(acl, rhs.acl).append(syslog, rhs.syslog).append(rateLimiting, rhs.rateLimiting).append(correlationId, rhs.correlationId).append(udpLog, rhs.udpLog).append(runscope, rhs.runscope).append(tcpLog, rhs.tcpLog).append(hmacAuth, rhs.hmacAuth).append(responseTransformer, rhs.responseTransformer).append(basicAuth, rhs.basicAuth).append(galileo, rhs.galileo).append(fileLog, rhs.fileLog).append(ipRestriction, rhs.ipRestriction).append(requestSizeLimiting, rhs.requestSizeLimiting).append(ssl, rhs.ssl).append(datadog, rhs.datadog).append(loggly, rhs.loggly).append(statsd, rhs.statsd).append(responseRatelimiting, rhs.responseRatelimiting).append(botDetection, rhs.botDetection).isEquals();
    }

}
