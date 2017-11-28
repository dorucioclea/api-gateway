
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
public class KongPluginJsonThreatProtection implements KongConfigValue
{

    @SerializedName("source")
    @Expose
    private Source source;
    /**
     * 
     */
    @SerializedName("container_depth")
    @Expose
    private Long containerDepth = 0L;
    /**
     * 
     */
    @SerializedName("string_value_length")
    @Expose
    private Long stringValueLength = 0L;
    /**
     * 
     */
    @SerializedName("array_element_count")
    @Expose
    private Long arrayElementCount = 0L;
    /**
     * 
     */
    @SerializedName("object_entry_count")
    @Expose
    private Long objectEntryCount = 0L;
    /**
     * 
     */
    @SerializedName("object_entry_name_length")
    @Expose
    private Long objectEntryNameLength = 0L;

    /**
     * 
     * @return
     *     The source
     */
    public Source getSource() {
        return source;
    }

    /**
     * 
     * @param source
     *     The source
     */
    public void setSource(Source source) {
        this.source = source;
    }

    public KongPluginJsonThreatProtection withSource(Source source) {
        this.source = source;
        return this;
    }

    /**
     * 
     * @return
     *     The containerDepth
     */
    public Long getContainerDepth() {
        return containerDepth;
    }

    /**
     * 
     * @param containerDepth
     *     The container_depth
     */
    public void setContainerDepth(Long containerDepth) {
        this.containerDepth = containerDepth;
    }

    public KongPluginJsonThreatProtection withContainerDepth(Long containerDepth) {
        this.containerDepth = containerDepth;
        return this;
    }

    /**
     * 
     * @return
     *     The stringValueLength
     */
    public Long getStringValueLength() {
        return stringValueLength;
    }

    /**
     * 
     * @param stringValueLength
     *     The string_value_length
     */
    public void setStringValueLength(Long stringValueLength) {
        this.stringValueLength = stringValueLength;
    }

    public KongPluginJsonThreatProtection withStringValueLength(Long stringValueLength) {
        this.stringValueLength = stringValueLength;
        return this;
    }

    /**
     * 
     * @return
     *     The arrayElementCount
     */
    public Long getArrayElementCount() {
        return arrayElementCount;
    }

    /**
     * 
     * @param arrayElementCount
     *     The array_element_count
     */
    public void setArrayElementCount(Long arrayElementCount) {
        this.arrayElementCount = arrayElementCount;
    }

    public KongPluginJsonThreatProtection withArrayElementCount(Long arrayElementCount) {
        this.arrayElementCount = arrayElementCount;
        return this;
    }

    /**
     * 
     * @return
     *     The objectEntryCount
     */
    public Long getObjectEntryCount() {
        return objectEntryCount;
    }

    /**
     * 
     * @param objectEntryCount
     *     The object_entry_count
     */
    public void setObjectEntryCount(Long objectEntryCount) {
        this.objectEntryCount = objectEntryCount;
    }

    public KongPluginJsonThreatProtection withObjectEntryCount(Long objectEntryCount) {
        this.objectEntryCount = objectEntryCount;
        return this;
    }

    /**
     * 
     * @return
     *     The objectEntryNameLength
     */
    public Long getObjectEntryNameLength() {
        return objectEntryNameLength;
    }

    /**
     * 
     * @param objectEntryNameLength
     *     The object_entry_name_length
     */
    public void setObjectEntryNameLength(Long objectEntryNameLength) {
        this.objectEntryNameLength = objectEntryNameLength;
    }

    public KongPluginJsonThreatProtection withObjectEntryNameLength(Long objectEntryNameLength) {
        this.objectEntryNameLength = objectEntryNameLength;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(source).append(containerDepth).append(stringValueLength).append(arrayElementCount).append(objectEntryCount).append(objectEntryNameLength).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginJsonThreatProtection) == false) {
            return false;
        }
        KongPluginJsonThreatProtection rhs = ((KongPluginJsonThreatProtection) other);
        return new EqualsBuilder().append(source, rhs.source).append(containerDepth, rhs.containerDepth).append(stringValueLength, rhs.stringValueLength).append(arrayElementCount, rhs.arrayElementCount).append(objectEntryCount, rhs.objectEntryCount).append(objectEntryNameLength, rhs.objectEntryNameLength).isEquals();
    }

    @Generated("org.jsonschema2pojo")
    public static enum Source {

        @SerializedName("message")
        MESSAGE("message"),
        @SerializedName("response")
        RESPONSE("response"),
        @SerializedName("request")
        REQUEST("request");
        private final String value;
        private static Map<String, Source> constants = new HashMap<String, Source>();

        static {
            for (Source c: values()) {
                constants.put(c.value, c);
            }
        }

        private Source(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static Source fromValue(String value) {
            Source constant = constants.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
