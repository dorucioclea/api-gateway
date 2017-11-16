package com.t1t.apim.beans.actions;

import java.io.Serializable;

/**
 * The bean used to peform an action on an entity.
 */
public class ActionBean implements Serializable {

    private static final long serialVersionUID = -540487411892625007L;

    private ActionType type;
    private String organizationId;
    private String entityId;
    private String entityVersion;

    /**
     * @return the type
     */
    public ActionType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(ActionType type) {
        this.type = type;
    }

    /**
     * @return the organizationId
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * @param organizationId the organizationId to set
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * @return the entityId
     */
    public String getEntityId() {
        return entityId;
    }

    /**
     * @param entityId the entityId to set
     */
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    /**
     * @return the entityVersion
     */
    public String getEntityVersion() {
        return entityVersion;
    }

    /**
     * @param entityVersion the entityVersion to set
     */
    public void setEntityVersion(String entityVersion) {
        this.entityVersion = entityVersion;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
        result = prime * result + ((entityVersion == null) ? 0 : entityVersion.hashCode());
        result = prime * result + ((organizationId == null) ? 0 : organizationId.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActionBean)) return false;

        ActionBean that = (ActionBean) o;

        if (type != that.type) return false;
        if (organizationId != null ? !organizationId.equals(that.organizationId) : that.organizationId != null)
            return false;
        if (entityId != null ? !entityId.equals(that.entityId) : that.entityId != null) return false;
        return entityVersion != null ? entityVersion.equals(that.entityVersion) : that.entityVersion == null;
    }
}
