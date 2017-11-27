
package com.t1t.kong.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginAWSLambda implements KongConfigValue
{

    @SerializedName("aws_key")
    @Expose
    private String awsKey;
    @SerializedName("aws_secret")
    @Expose
    private String awsSecret;
    @SerializedName("function_name")
    @Expose
    private String functionName;
    @SerializedName("aws_region")
    @Expose
    private AwsRegion awsRegion;
    @SerializedName("qualifier")
    @Expose
    private String qualifier;
    @SerializedName("invocation_type")
    @Expose
    private InvocationType invocationType;
    @SerializedName("log_type")
    @Expose
    private LogType logType;
    @SerializedName("timeout")
    @Expose
    private Long timeout;
    @SerializedName("keepalive")
    @Expose
    private Long keepalive;

    /**
     * 
     * @return
     *     The awsKey
     */
    public String getAwsKey() {
        return awsKey;
    }

    /**
     * 
     * @param awsKey
     *     The aws_key
     */
    public void setAwsKey(String awsKey) {
        this.awsKey = awsKey;
    }

    public KongPluginAWSLambda withAwsKey(String awsKey) {
        this.awsKey = awsKey;
        return this;
    }

    /**
     * 
     * @return
     *     The awsSecret
     */
    public String getAwsSecret() {
        return awsSecret;
    }

    /**
     * 
     * @param awsSecret
     *     The aws_secret
     */
    public void setAwsSecret(String awsSecret) {
        this.awsSecret = awsSecret;
    }

    public KongPluginAWSLambda withAwsSecret(String awsSecret) {
        this.awsSecret = awsSecret;
        return this;
    }

    /**
     * 
     * @return
     *     The functionName
     */
    public String getFunctionName() {
        return functionName;
    }

    /**
     * 
     * @param functionName
     *     The function_name
     */
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public KongPluginAWSLambda withFunctionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    /**
     * 
     * @return
     *     The awsRegion
     */
    public AwsRegion getAwsRegion() {
        return awsRegion;
    }

    /**
     * 
     * @param awsRegion
     *     The aws_region
     */
    public void setAwsRegion(AwsRegion awsRegion) {
        this.awsRegion = awsRegion;
    }

    public KongPluginAWSLambda withAwsRegion(AwsRegion awsRegion) {
        this.awsRegion = awsRegion;
        return this;
    }

    /**
     * 
     * @return
     *     The qualifier
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * 
     * @param qualifier
     *     The qualifier
     */
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public KongPluginAWSLambda withQualifier(String qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    /**
     * 
     * @return
     *     The invocationType
     */
    public InvocationType getInvocationType() {
        return invocationType;
    }

    /**
     * 
     * @param invocationType
     *     The invocation_type
     */
    public void setInvocationType(InvocationType invocationType) {
        this.invocationType = invocationType;
    }

    public KongPluginAWSLambda withInvocationType(InvocationType invocationType) {
        this.invocationType = invocationType;
        return this;
    }

    /**
     * 
     * @return
     *     The logType
     */
    public LogType getLogType() {
        return logType;
    }

    /**
     * 
     * @param logType
     *     The log_type
     */
    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public KongPluginAWSLambda withLogType(LogType logType) {
        this.logType = logType;
        return this;
    }

    /**
     * 
     * @return
     *     The timeout
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * 
     * @param timeout
     *     The timeout
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public KongPluginAWSLambda withTimeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 
     * @return
     *     The keepalive
     */
    public Long getKeepalive() {
        return keepalive;
    }

    /**
     * 
     * @param keepalive
     *     The keepalive
     */
    public void setKeepalive(Long keepalive) {
        this.keepalive = keepalive;
    }

    public KongPluginAWSLambda withKeepalive(Long keepalive) {
        this.keepalive = keepalive;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(awsKey).append(awsSecret).append(functionName).append(awsRegion).append(qualifier).append(invocationType).append(logType).append(timeout).append(keepalive).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginAWSLambda) == false) {
            return false;
        }
        KongPluginAWSLambda rhs = ((KongPluginAWSLambda) other);
        return new EqualsBuilder().append(awsKey, rhs.awsKey).append(awsSecret, rhs.awsSecret).append(functionName, rhs.functionName).append(awsRegion, rhs.awsRegion).append(qualifier, rhs.qualifier).append(invocationType, rhs.invocationType).append(logType, rhs.logType).append(timeout, rhs.timeout).append(keepalive, rhs.keepalive).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public static enum AwsRegion {

        @SerializedName("us-east-1")
        US_EAST_1("us-east-1"),
        @SerializedName("us-east-2")
        US_EAST_2("us-east-2"),
        @SerializedName("ap-northeast-1")
        AP_NORTHEAST_1("ap-northeast-1"),
        @SerializedName("ap-northeast-2")
        AP_NORTHEAST_2("ap-northeast-2"),
        @SerializedName("us-west-2")
        US_WEST_2("us-west-2"),
        @SerializedName("ap-southeast-1")
        AP_SOUTHEAST_1("ap-southeast-1"),
        @SerializedName("ap-southeast-2")
        AP_SOUTHEAST_2("ap-southeast-2"),
        @SerializedName("eu-central-1")
        EU_CENTRAL_1("eu-central-1"),
        @SerializedName("eu-west-1")
        EU_WEST_1("eu-west-1");
        private final String value;
        private static Map<String, AwsRegion> constants = new HashMap<String, AwsRegion>();

        static {
            for (AwsRegion c: values()) {
                constants.put(c.value, c);
            }
        }

        private AwsRegion(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static AwsRegion fromValue(String value) {
            AwsRegion constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public static enum InvocationType {

        @SerializedName("RequestResponse")
        REQUEST_RESPONSE("RequestResponse"),
        @SerializedName("Event")
        EVENT("Event"),
        @SerializedName("DryRun")
        DRY_RUN("DryRun");
        private final String value;
        private static Map<String, InvocationType> constants = new HashMap<String, InvocationType>();

        static {
            for (InvocationType c: values()) {
                constants.put(c.value, c);
            }
        }

        private InvocationType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static InvocationType fromValue(String value) {
            InvocationType constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    @Generated("org.jsonschema2pojo")
    public static enum LogType {

        @SerializedName("Tail")
        TAIL("Tail"),
        @SerializedName("None")
        NONE("None");
        private final String value;
        private static Map<String, LogType> constants = new HashMap<String, LogType>();

        static {
            for (LogType c: values()) {
                constants.put(c.value, c);
            }
        }

        private LogType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static LogType fromValue(String value) {
            LogType constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
