
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginResponseTransformer implements KongConfigValue
{

    @SerializedName("remove")
    @Expose
    private KongPluginResponseTransformerModification remove;
    @SerializedName("add")
    @Expose
    private KongPluginResponseTransformerModification add;
    @SerializedName("replace")
    @Expose
    private KongPluginResponseTransformerModification replace;
    @SerializedName("append")
    @Expose
    private KongPluginResponseTransformerModification append;

    /**
     * 
     * @return
     *     The remove
     */
    public KongPluginResponseTransformerModification getRemove() {
        return remove;
    }

    /**
     * 
     * @param remove
     *     The remove
     */
    public void setRemove(KongPluginResponseTransformerModification remove) {
        this.remove = remove;
    }

    public KongPluginResponseTransformer withRemove(KongPluginResponseTransformerModification remove) {
        this.remove = remove;
        return this;
    }

    /**
     * 
     * @return
     *     The add
     */
    public KongPluginResponseTransformerModification getAdd() {
        return add;
    }

    /**
     * 
     * @param add
     *     The add
     */
    public void setAdd(KongPluginResponseTransformerModification add) {
        this.add = add;
    }

    public KongPluginResponseTransformer withAdd(KongPluginResponseTransformerModification add) {
        this.add = add;
        return this;
    }

    /**
     * 
     * @return
     *     The replace
     */
    public KongPluginResponseTransformerModification getReplace() {
        return replace;
    }

    /**
     * 
     * @param replace
     *     The replace
     */
    public void setReplace(KongPluginResponseTransformerModification replace) {
        this.replace = replace;
    }

    public KongPluginResponseTransformer withReplace(KongPluginResponseTransformerModification replace) {
        this.replace = replace;
        return this;
    }

    /**
     * 
     * @return
     *     The append
     */
    public KongPluginResponseTransformerModification getAppend() {
        return append;
    }

    /**
     * 
     * @param append
     *     The append
     */
    public void setAppend(KongPluginResponseTransformerModification append) {
        this.append = append;
    }

    public KongPluginResponseTransformer withAppend(KongPluginResponseTransformerModification append) {
        this.append = append;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(remove).append(add).append(replace).append(append).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongPluginResponseTransformer) == false) {
            return false;
        }
        KongPluginResponseTransformer rhs = ((KongPluginResponseTransformer) other);
        return new EqualsBuilder().append(remove, rhs.remove).append(add, rhs.add).append(replace, rhs.replace).append(append, rhs.append).isEquals();
    }

}
