
package com.t1t.kong.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class SCIMUser {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("profileUrl")
    @Expose
    private String profileUrl;
    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("emails")
    @Expose
    private List<String> emails = new ArrayList<String>();
    @SerializedName("phoneNumbers")
    @Expose
    private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
    @SerializedName("meta")
    @Expose
    private Meta meta;

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    public SCIMUser withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * 
     * @return
     *     The displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 
     * @param displayName
     *     The displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public SCIMUser withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public SCIMUser withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The profileUrl
     */
    public String getProfileUrl() {
        return profileUrl;
    }

    /**
     * 
     * @param profileUrl
     *     The profileUrl
     */
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public SCIMUser withProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
        return this;
    }

    /**
     * 
     * @return
     *     The name
     */
    public Name getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(Name name) {
        this.name = name;
    }

    public SCIMUser withName(Name name) {
        this.name = name;
        return this;
    }

    /**
     * 
     * @return
     *     The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     *     The userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SCIMUser withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * 
     * @return
     *     The emails
     */
    public List<String> getEmails() {
        return emails;
    }

    /**
     * 
     * @param emails
     *     The emails
     */
    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public SCIMUser withEmails(List<String> emails) {
        this.emails = emails;
        return this;
    }

    /**
     * 
     * @return
     *     The phoneNumbers
     */
    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    /**
     * 
     * @param phoneNumbers
     *     The phoneNumbers
     */
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public SCIMUser withPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }

    /**
     * 
     * @return
     *     The meta
     */
    public Meta getMeta() {
        return meta;
    }

    /**
     * 
     * @param meta
     *     The meta
     */
    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public SCIMUser withMeta(Meta meta) {
        this.meta = meta;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(displayName).append(title).append(profileUrl).append(name).append(userName).append(emails).append(phoneNumbers).append(meta).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SCIMUser) == false) {
            return false;
        }
        SCIMUser rhs = ((SCIMUser) other);
        return new EqualsBuilder().append(id, rhs.id).append(displayName, rhs.displayName).append(title, rhs.title).append(profileUrl, rhs.profileUrl).append(name, rhs.name).append(userName, rhs.userName).append(emails, rhs.emails).append(phoneNumbers, rhs.phoneNumbers).append(meta, rhs.meta).isEquals();
    }

}
