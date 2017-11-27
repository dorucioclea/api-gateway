
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongPluginRequestTransformer implements KongConfigValue
{

    @SerializedName("remove")
    @Expose
    private KongPluginRequestTransformerModification remove;
    @SerializedName("add")
    @Expose
    private KongPluginRequestTransformerModification add;
    @SerializedName("replace")
    @Expose
    private KongPluginRequestTransformerModification replace;
    @SerializedName("append")
    @Expose
    private KongPluginRequestTransformerModification append;

    /**
     * 
     * @return
     *     The remove
     */
    public KongPluginRequestTransformerModification getRemove() {
        return remove;
    }

    /**
     * 
     * @param remove
     *     The remove
     */
    public void setRemove(KongPluginRequestTransformerModification remove) {
        this.remove = remove;
    }

    public KongPluginRequestTransformer withRemove(KongPluginRequestTransformerModification remove) {
        this.remove = remove;
        return this;
    }

    /**
     * 
     * @return
     *     The add
     */
    public KongPluginRequestTransformerModification getAdd() {
        return add;
    }

    /**
     * 
     * @param add
     *     The add
     */
    public void setAdd(KongPluginRequestTransformerModification add) {
        this.add = add;
    }

    public KongPluginRequestTransformer withAdd(KongPluginRequestTransformerModification add) {
        this.add = add;
        return this;
    }

    /**
     * 
     * @return
     *     The replace
     */
    public KongPluginRequestTransformerModification getReplace() {
        return replace;
    }

    /**
     * 
     * @param replace
     *     The replace
     */
    public void setReplace(KongPluginRequestTransformerModification replace) {
        this.replace = replace;
    }

    public KongPluginRequestTransformer withReplace(KongPluginRequestTransformerModification replace) {
        this.replace = replace;
        return this;
    }

    /**
     * 
     * @return
     *     The append
     */
    public KongPluginRequestTransformerModification getAppend() {
        return append;
    }

    /**
     * 
     * @param append
     *     The append
     */
    public void setAppend(KongPluginRequestTransformerModification append) {
        this.append = append;
    }

    public KongPluginRequestTransformer withAppend(KongPluginRequestTransformerModification append) {
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
        if ((other instanceof KongPluginRequestTransformer) == false) {
            return false;
        }
        KongPluginRequestTransformer rhs = ((KongPluginRequestTransformer) other);
        return new EqualsBuilder().append(remove, rhs.remove).append(add, rhs.add).append(replace, rhs.replace).append(append, rhs.append).isEquals();
    }

}
