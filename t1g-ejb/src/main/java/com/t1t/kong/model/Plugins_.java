
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Plugins_ {

    @SerializedName("syslog")
    @Expose
    private Boolean syslog;
    @SerializedName("ldap-auth")
    @Expose
    private Boolean ldapAuth;
    @SerializedName("rate-limiting")
    @Expose
    private Boolean rateLimiting;
    @SerializedName("correlation-id")
    @Expose
    private Boolean correlationId;
    @SerializedName("jwt")
    @Expose
    private Boolean jwt;
    @SerializedName("runscope")
    @Expose
    private Boolean runscope;
    @SerializedName("hmac-auth")
    @Expose
    private Boolean hmacAuth;
    @SerializedName("http-log")
    @Expose
    private Boolean httpLog;
    @SerializedName("tcp-log")
    @Expose
    private Boolean tcpLog;
    @SerializedName("response-transformer")
    @Expose
    private Boolean responseTransformer;
    @SerializedName("statsd")
    @Expose
    private Boolean statsd;
    @SerializedName("basic-auth")
    @Expose
    private Boolean basicAuth;
    @SerializedName("request-transformer")
    @Expose
    private Boolean requestTransformer;
    @SerializedName("bot-detection")
    @Expose
    private Boolean botDetection;
    @SerializedName("request-size-limiting")
    @Expose
    private Boolean requestSizeLimiting;
    @SerializedName("galileo")
    @Expose
    private Boolean galileo;
    @SerializedName("datadog")
    @Expose
    private Boolean datadog;
    @SerializedName("acl")
    @Expose
    private Boolean acl;
    @SerializedName("file-log")
    @Expose
    private Boolean fileLog;
    @SerializedName("ip-restriction")
    @Expose
    private Boolean ipRestriction;
    @SerializedName("udp-log")
    @Expose
    private Boolean udpLog;
    @SerializedName("cors")
    @Expose
    private Boolean cors;
    @SerializedName("loggly")
    @Expose
    private Boolean loggly;
    @SerializedName("aws-lambda")
    @Expose
    private Boolean awsLambda;
    @SerializedName("oauth2")
    @Expose
    private Boolean oauth2;
    @SerializedName("response-ratelimiting")
    @Expose
    private Boolean responseRatelimiting;
    @SerializedName("key-auth")
    @Expose
    private Boolean keyAuth;
    @SerializedName("jwt-up")
    @Expose
    private Boolean jwtUp;
    @SerializedName("hal")
    @Expose
    private Boolean hal;
    @SerializedName("json-threat-protection")
    @Expose
    private Boolean jsonThreatProtection;

    /**
     * 
     * @return
     *     The syslog
     */
    public Boolean getSyslog() {
        return syslog;
    }

    /**
     * 
     * @param syslog
     *     The syslog
     */
    public void setSyslog(Boolean syslog) {
        this.syslog = syslog;
    }

    public Plugins_ withSyslog(Boolean syslog) {
        this.syslog = syslog;
        return this;
    }

    /**
     * 
     * @return
     *     The ldapAuth
     */
    public Boolean getLdapAuth() {
        return ldapAuth;
    }

    /**
     * 
     * @param ldapAuth
     *     The ldap-auth
     */
    public void setLdapAuth(Boolean ldapAuth) {
        this.ldapAuth = ldapAuth;
    }

    public Plugins_ withLdapAuth(Boolean ldapAuth) {
        this.ldapAuth = ldapAuth;
        return this;
    }

    /**
     * 
     * @return
     *     The rateLimiting
     */
    public Boolean getRateLimiting() {
        return rateLimiting;
    }

    /**
     * 
     * @param rateLimiting
     *     The rate-limiting
     */
    public void setRateLimiting(Boolean rateLimiting) {
        this.rateLimiting = rateLimiting;
    }

    public Plugins_ withRateLimiting(Boolean rateLimiting) {
        this.rateLimiting = rateLimiting;
        return this;
    }

    /**
     * 
     * @return
     *     The correlationId
     */
    public Boolean getCorrelationId() {
        return correlationId;
    }

    /**
     * 
     * @param correlationId
     *     The correlation-id
     */
    public void setCorrelationId(Boolean correlationId) {
        this.correlationId = correlationId;
    }

    public Plugins_ withCorrelationId(Boolean correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    /**
     * 
     * @return
     *     The jwt
     */
    public Boolean getJwt() {
        return jwt;
    }

    /**
     * 
     * @param jwt
     *     The jwt
     */
    public void setJwt(Boolean jwt) {
        this.jwt = jwt;
    }

    public Plugins_ withJwt(Boolean jwt) {
        this.jwt = jwt;
        return this;
    }

    /**
     * 
     * @return
     *     The runscope
     */
    public Boolean getRunscope() {
        return runscope;
    }

    /**
     * 
     * @param runscope
     *     The runscope
     */
    public void setRunscope(Boolean runscope) {
        this.runscope = runscope;
    }

    public Plugins_ withRunscope(Boolean runscope) {
        this.runscope = runscope;
        return this;
    }

    /**
     * 
     * @return
     *     The hmacAuth
     */
    public Boolean getHmacAuth() {
        return hmacAuth;
    }

    /**
     * 
     * @param hmacAuth
     *     The hmac-auth
     */
    public void setHmacAuth(Boolean hmacAuth) {
        this.hmacAuth = hmacAuth;
    }

    public Plugins_ withHmacAuth(Boolean hmacAuth) {
        this.hmacAuth = hmacAuth;
        return this;
    }

    /**
     * 
     * @return
     *     The httpLog
     */
    public Boolean getHttpLog() {
        return httpLog;
    }

    /**
     * 
     * @param httpLog
     *     The http-log
     */
    public void setHttpLog(Boolean httpLog) {
        this.httpLog = httpLog;
    }

    public Plugins_ withHttpLog(Boolean httpLog) {
        this.httpLog = httpLog;
        return this;
    }

    /**
     * 
     * @return
     *     The tcpLog
     */
    public Boolean getTcpLog() {
        return tcpLog;
    }

    /**
     * 
     * @param tcpLog
     *     The tcp-log
     */
    public void setTcpLog(Boolean tcpLog) {
        this.tcpLog = tcpLog;
    }

    public Plugins_ withTcpLog(Boolean tcpLog) {
        this.tcpLog = tcpLog;
        return this;
    }

    /**
     * 
     * @return
     *     The responseTransformer
     */
    public Boolean getResponseTransformer() {
        return responseTransformer;
    }

    /**
     * 
     * @param responseTransformer
     *     The response-transformer
     */
    public void setResponseTransformer(Boolean responseTransformer) {
        this.responseTransformer = responseTransformer;
    }

    public Plugins_ withResponseTransformer(Boolean responseTransformer) {
        this.responseTransformer = responseTransformer;
        return this;
    }

    /**
     * 
     * @return
     *     The statsd
     */
    public Boolean getStatsd() {
        return statsd;
    }

    /**
     * 
     * @param statsd
     *     The statsd
     */
    public void setStatsd(Boolean statsd) {
        this.statsd = statsd;
    }

    public Plugins_ withStatsd(Boolean statsd) {
        this.statsd = statsd;
        return this;
    }

    /**
     * 
     * @return
     *     The basicAuth
     */
    public Boolean getBasicAuth() {
        return basicAuth;
    }

    /**
     * 
     * @param basicAuth
     *     The basic-auth
     */
    public void setBasicAuth(Boolean basicAuth) {
        this.basicAuth = basicAuth;
    }

    public Plugins_ withBasicAuth(Boolean basicAuth) {
        this.basicAuth = basicAuth;
        return this;
    }

    /**
     * 
     * @return
     *     The requestTransformer
     */
    public Boolean getRequestTransformer() {
        return requestTransformer;
    }

    /**
     * 
     * @param requestTransformer
     *     The request-transformer
     */
    public void setRequestTransformer(Boolean requestTransformer) {
        this.requestTransformer = requestTransformer;
    }

    public Plugins_ withRequestTransformer(Boolean requestTransformer) {
        this.requestTransformer = requestTransformer;
        return this;
    }

    /**
     * 
     * @return
     *     The botDetection
     */
    public Boolean getBotDetection() {
        return botDetection;
    }

    /**
     * 
     * @param botDetection
     *     The bot-detection
     */
    public void setBotDetection(Boolean botDetection) {
        this.botDetection = botDetection;
    }

    public Plugins_ withBotDetection(Boolean botDetection) {
        this.botDetection = botDetection;
        return this;
    }

    /**
     * 
     * @return
     *     The requestSizeLimiting
     */
    public Boolean getRequestSizeLimiting() {
        return requestSizeLimiting;
    }

    /**
     * 
     * @param requestSizeLimiting
     *     The request-size-limiting
     */
    public void setRequestSizeLimiting(Boolean requestSizeLimiting) {
        this.requestSizeLimiting = requestSizeLimiting;
    }

    public Plugins_ withRequestSizeLimiting(Boolean requestSizeLimiting) {
        this.requestSizeLimiting = requestSizeLimiting;
        return this;
    }

    /**
     * 
     * @return
     *     The galileo
     */
    public Boolean getGalileo() {
        return galileo;
    }

    /**
     * 
     * @param galileo
     *     The galileo
     */
    public void setGalileo(Boolean galileo) {
        this.galileo = galileo;
    }

    public Plugins_ withGalileo(Boolean galileo) {
        this.galileo = galileo;
        return this;
    }

    /**
     * 
     * @return
     *     The datadog
     */
    public Boolean getDatadog() {
        return datadog;
    }

    /**
     * 
     * @param datadog
     *     The datadog
     */
    public void setDatadog(Boolean datadog) {
        this.datadog = datadog;
    }

    public Plugins_ withDatadog(Boolean datadog) {
        this.datadog = datadog;
        return this;
    }

    /**
     * 
     * @return
     *     The acl
     */
    public Boolean getAcl() {
        return acl;
    }

    /**
     * 
     * @param acl
     *     The acl
     */
    public void setAcl(Boolean acl) {
        this.acl = acl;
    }

    public Plugins_ withAcl(Boolean acl) {
        this.acl = acl;
        return this;
    }

    /**
     * 
     * @return
     *     The fileLog
     */
    public Boolean getFileLog() {
        return fileLog;
    }

    /**
     * 
     * @param fileLog
     *     The file-log
     */
    public void setFileLog(Boolean fileLog) {
        this.fileLog = fileLog;
    }

    public Plugins_ withFileLog(Boolean fileLog) {
        this.fileLog = fileLog;
        return this;
    }

    /**
     * 
     * @return
     *     The ipRestriction
     */
    public Boolean getIpRestriction() {
        return ipRestriction;
    }

    /**
     * 
     * @param ipRestriction
     *     The ip-restriction
     */
    public void setIpRestriction(Boolean ipRestriction) {
        this.ipRestriction = ipRestriction;
    }

    public Plugins_ withIpRestriction(Boolean ipRestriction) {
        this.ipRestriction = ipRestriction;
        return this;
    }

    /**
     * 
     * @return
     *     The udpLog
     */
    public Boolean getUdpLog() {
        return udpLog;
    }

    /**
     * 
     * @param udpLog
     *     The udp-log
     */
    public void setUdpLog(Boolean udpLog) {
        this.udpLog = udpLog;
    }

    public Plugins_ withUdpLog(Boolean udpLog) {
        this.udpLog = udpLog;
        return this;
    }

    /**
     * 
     * @return
     *     The cors
     */
    public Boolean getCors() {
        return cors;
    }

    /**
     * 
     * @param cors
     *     The cors
     */
    public void setCors(Boolean cors) {
        this.cors = cors;
    }

    public Plugins_ withCors(Boolean cors) {
        this.cors = cors;
        return this;
    }

    /**
     * 
     * @return
     *     The loggly
     */
    public Boolean getLoggly() {
        return loggly;
    }

    /**
     * 
     * @param loggly
     *     The loggly
     */
    public void setLoggly(Boolean loggly) {
        this.loggly = loggly;
    }

    public Plugins_ withLoggly(Boolean loggly) {
        this.loggly = loggly;
        return this;
    }

    /**
     * 
     * @return
     *     The awsLambda
     */
    public Boolean getAwsLambda() {
        return awsLambda;
    }

    /**
     * 
     * @param awsLambda
     *     The aws-lambda
     */
    public void setAwsLambda(Boolean awsLambda) {
        this.awsLambda = awsLambda;
    }

    public Plugins_ withAwsLambda(Boolean awsLambda) {
        this.awsLambda = awsLambda;
        return this;
    }

    /**
     * 
     * @return
     *     The oauth2
     */
    public Boolean getOauth2() {
        return oauth2;
    }

    /**
     * 
     * @param oauth2
     *     The oauth2
     */
    public void setOauth2(Boolean oauth2) {
        this.oauth2 = oauth2;
    }

    public Plugins_ withOauth2(Boolean oauth2) {
        this.oauth2 = oauth2;
        return this;
    }

    /**
     * 
     * @return
     *     The responseRatelimiting
     */
    public Boolean getResponseRatelimiting() {
        return responseRatelimiting;
    }

    /**
     * 
     * @param responseRatelimiting
     *     The response-ratelimiting
     */
    public void setResponseRatelimiting(Boolean responseRatelimiting) {
        this.responseRatelimiting = responseRatelimiting;
    }

    public Plugins_ withResponseRatelimiting(Boolean responseRatelimiting) {
        this.responseRatelimiting = responseRatelimiting;
        return this;
    }

    /**
     * 
     * @return
     *     The keyAuth
     */
    public Boolean getKeyAuth() {
        return keyAuth;
    }

    /**
     * 
     * @param keyAuth
     *     The key-auth
     */
    public void setKeyAuth(Boolean keyAuth) {
        this.keyAuth = keyAuth;
    }

    public Plugins_ withKeyAuth(Boolean keyAuth) {
        this.keyAuth = keyAuth;
        return this;
    }

    /**
     * 
     * @return
     *     The jwtUp
     */
    public Boolean getJwtUp() {
        return jwtUp;
    }

    /**
     * 
     * @param jwtUp
     *     The jwt-up
     */
    public void setJwtUp(Boolean jwtUp) {
        this.jwtUp = jwtUp;
    }

    public Plugins_ withJwtUp(Boolean jwtUp) {
        this.jwtUp = jwtUp;
        return this;
    }

    /**
     * 
     * @return
     *     The hal
     */
    public Boolean getHal() {
        return hal;
    }

    /**
     * 
     * @param hal
     *     The hal
     */
    public void setHal(Boolean hal) {
        this.hal = hal;
    }

    public Plugins_ withHal(Boolean hal) {
        this.hal = hal;
        return this;
    }

    /**
     * 
     * @return
     *     The jsonThreatProtection
     */
    public Boolean getJsonThreatProtection() {
        return jsonThreatProtection;
    }

    /**
     * 
     * @param jsonThreatProtection
     *     The json-threat-protection
     */
    public void setJsonThreatProtection(Boolean jsonThreatProtection) {
        this.jsonThreatProtection = jsonThreatProtection;
    }

    public Plugins_ withJsonThreatProtection(Boolean jsonThreatProtection) {
        this.jsonThreatProtection = jsonThreatProtection;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(syslog).append(ldapAuth).append(rateLimiting).append(correlationId).append(jwt).append(runscope).append(hmacAuth).append(httpLog).append(tcpLog).append(responseTransformer).append(statsd).append(basicAuth).append(requestTransformer).append(botDetection).append(requestSizeLimiting).append(galileo).append(datadog).append(acl).append(fileLog).append(ipRestriction).append(udpLog).append(cors).append(loggly).append(awsLambda).append(oauth2).append(responseRatelimiting).append(keyAuth).append(jwtUp).append(hal).append(jsonThreatProtection).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Plugins_) == false) {
            return false;
        }
        Plugins_ rhs = ((Plugins_) other);
        return new EqualsBuilder().append(syslog, rhs.syslog).append(ldapAuth, rhs.ldapAuth).append(rateLimiting, rhs.rateLimiting).append(correlationId, rhs.correlationId).append(jwt, rhs.jwt).append(runscope, rhs.runscope).append(hmacAuth, rhs.hmacAuth).append(httpLog, rhs.httpLog).append(tcpLog, rhs.tcpLog).append(responseTransformer, rhs.responseTransformer).append(statsd, rhs.statsd).append(basicAuth, rhs.basicAuth).append(requestTransformer, rhs.requestTransformer).append(botDetection, rhs.botDetection).append(requestSizeLimiting, rhs.requestSizeLimiting).append(galileo, rhs.galileo).append(datadog, rhs.datadog).append(acl, rhs.acl).append(fileLog, rhs.fileLog).append(ipRestriction, rhs.ipRestriction).append(udpLog, rhs.udpLog).append(cors, rhs.cors).append(loggly, rhs.loggly).append(awsLambda, rhs.awsLambda).append(oauth2, rhs.oauth2).append(responseRatelimiting, rhs.responseRatelimiting).append(keyAuth, rhs.keyAuth).append(jwtUp, rhs.jwtUp).append(hal, rhs.hal).append(jsonThreatProtection, rhs.jsonThreatProtection).isEquals();
    }

}
