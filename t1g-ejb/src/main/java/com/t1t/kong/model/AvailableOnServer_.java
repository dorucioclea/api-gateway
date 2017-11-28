
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class AvailableOnServer_ {

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
    @SerializedName("request-transformer")
    @Expose
    private Boolean requestTransformer;
    @SerializedName("http-log")
    @Expose
    private Boolean httpLog;
    @SerializedName("loggly")
    @Expose
    private Boolean loggly;
    @SerializedName("response-transformer")
    @Expose
    private Boolean responseTransformer;
    @SerializedName("basic-auth")
    @Expose
    private Boolean basicAuth;
    @SerializedName("tcp-log")
    @Expose
    private Boolean tcpLog;
    @SerializedName("key-auth")
    @Expose
    private Boolean keyAuth;
    @SerializedName("oauth2")
    @Expose
    private Boolean oauth2;
    @SerializedName("acl")
    @Expose
    private Boolean acl;
    @SerializedName("galileo")
    @Expose
    private Boolean galileo;
    @SerializedName("udp-log")
    @Expose
    private Boolean udpLog;
    @SerializedName("cors")
    @Expose
    private Boolean cors;
    @SerializedName("file-log")
    @Expose
    private Boolean fileLog;
    @SerializedName("ip-restriction")
    @Expose
    private Boolean ipRestriction;
    @SerializedName("datadog")
    @Expose
    private Boolean datadog;
    @SerializedName("request-size-limiting")
    @Expose
    private Boolean requestSizeLimiting;
    @SerializedName("bot-detection")
    @Expose
    private Boolean botDetection;
    @SerializedName("aws-lambda")
    @Expose
    private Boolean awsLambda;
    @SerializedName("statsd")
    @Expose
    private Boolean statsd;
    @SerializedName("response-ratelimiting")
    @Expose
    private Boolean responseRatelimiting;
    @SerializedName("hmac-auth")
    @Expose
    private Boolean hmacAuth;
    @SerializedName("jwt-up")
    @Expose
    private Boolean jwtUp;
    @SerializedName("json-threat-protection")
    @Expose
    private Boolean jsonThreatProtection;
    @SerializedName("hal")
    @Expose
    private Boolean hal;

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

    public AvailableOnServer_ withSyslog(Boolean syslog) {
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

    public AvailableOnServer_ withLdapAuth(Boolean ldapAuth) {
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

    public AvailableOnServer_ withRateLimiting(Boolean rateLimiting) {
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

    public AvailableOnServer_ withCorrelationId(Boolean correlationId) {
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

    public AvailableOnServer_ withJwt(Boolean jwt) {
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

    public AvailableOnServer_ withRunscope(Boolean runscope) {
        this.runscope = runscope;
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

    public AvailableOnServer_ withRequestTransformer(Boolean requestTransformer) {
        this.requestTransformer = requestTransformer;
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

    public AvailableOnServer_ withHttpLog(Boolean httpLog) {
        this.httpLog = httpLog;
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

    public AvailableOnServer_ withLoggly(Boolean loggly) {
        this.loggly = loggly;
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

    public AvailableOnServer_ withResponseTransformer(Boolean responseTransformer) {
        this.responseTransformer = responseTransformer;
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

    public AvailableOnServer_ withBasicAuth(Boolean basicAuth) {
        this.basicAuth = basicAuth;
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

    public AvailableOnServer_ withTcpLog(Boolean tcpLog) {
        this.tcpLog = tcpLog;
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

    public AvailableOnServer_ withKeyAuth(Boolean keyAuth) {
        this.keyAuth = keyAuth;
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

    public AvailableOnServer_ withOauth2(Boolean oauth2) {
        this.oauth2 = oauth2;
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

    public AvailableOnServer_ withAcl(Boolean acl) {
        this.acl = acl;
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

    public AvailableOnServer_ withGalileo(Boolean galileo) {
        this.galileo = galileo;
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

    public AvailableOnServer_ withUdpLog(Boolean udpLog) {
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

    public AvailableOnServer_ withCors(Boolean cors) {
        this.cors = cors;
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

    public AvailableOnServer_ withFileLog(Boolean fileLog) {
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

    public AvailableOnServer_ withIpRestriction(Boolean ipRestriction) {
        this.ipRestriction = ipRestriction;
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

    public AvailableOnServer_ withDatadog(Boolean datadog) {
        this.datadog = datadog;
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

    public AvailableOnServer_ withRequestSizeLimiting(Boolean requestSizeLimiting) {
        this.requestSizeLimiting = requestSizeLimiting;
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

    public AvailableOnServer_ withBotDetection(Boolean botDetection) {
        this.botDetection = botDetection;
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

    public AvailableOnServer_ withAwsLambda(Boolean awsLambda) {
        this.awsLambda = awsLambda;
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

    public AvailableOnServer_ withStatsd(Boolean statsd) {
        this.statsd = statsd;
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

    public AvailableOnServer_ withResponseRatelimiting(Boolean responseRatelimiting) {
        this.responseRatelimiting = responseRatelimiting;
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

    public AvailableOnServer_ withHmacAuth(Boolean hmacAuth) {
        this.hmacAuth = hmacAuth;
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

    public AvailableOnServer_ withJwtUp(Boolean jwtUp) {
        this.jwtUp = jwtUp;
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

    public AvailableOnServer_ withJsonThreatProtection(Boolean jsonThreatProtection) {
        this.jsonThreatProtection = jsonThreatProtection;
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

    public AvailableOnServer_ withHal(Boolean hal) {
        this.hal = hal;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(syslog).append(ldapAuth).append(rateLimiting).append(correlationId).append(jwt).append(runscope).append(requestTransformer).append(httpLog).append(loggly).append(responseTransformer).append(basicAuth).append(tcpLog).append(keyAuth).append(oauth2).append(acl).append(galileo).append(udpLog).append(cors).append(fileLog).append(ipRestriction).append(datadog).append(requestSizeLimiting).append(botDetection).append(awsLambda).append(statsd).append(responseRatelimiting).append(hmacAuth).append(jwtUp).append(jsonThreatProtection).append(hal).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AvailableOnServer_) == false) {
            return false;
        }
        AvailableOnServer_ rhs = ((AvailableOnServer_) other);
        return new EqualsBuilder().append(syslog, rhs.syslog).append(ldapAuth, rhs.ldapAuth).append(rateLimiting, rhs.rateLimiting).append(correlationId, rhs.correlationId).append(jwt, rhs.jwt).append(runscope, rhs.runscope).append(requestTransformer, rhs.requestTransformer).append(httpLog, rhs.httpLog).append(loggly, rhs.loggly).append(responseTransformer, rhs.responseTransformer).append(basicAuth, rhs.basicAuth).append(tcpLog, rhs.tcpLog).append(keyAuth, rhs.keyAuth).append(oauth2, rhs.oauth2).append(acl, rhs.acl).append(galileo, rhs.galileo).append(udpLog, rhs.udpLog).append(cors, rhs.cors).append(fileLog, rhs.fileLog).append(ipRestriction, rhs.ipRestriction).append(datadog, rhs.datadog).append(requestSizeLimiting, rhs.requestSizeLimiting).append(botDetection, rhs.botDetection).append(awsLambda, rhs.awsLambda).append(statsd, rhs.statsd).append(responseRatelimiting, rhs.responseRatelimiting).append(hmacAuth, rhs.hmacAuth).append(jwtUp, rhs.jwtUp).append(jsonThreatProtection, rhs.jsonThreatProtection).append(hal, rhs.hal).isEquals();
    }

}
