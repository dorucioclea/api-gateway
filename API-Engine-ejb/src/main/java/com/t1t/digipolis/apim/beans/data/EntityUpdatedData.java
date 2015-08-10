package com.t1t.digipolis.apim.beans.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The data saved along with the audit entry when an entity is
 * updated.
 *
 */
public class EntityUpdatedData implements Serializable {

    private static final long serialVersionUID = 3009506122267996076L;

    private List<EntityFieldChange> changes = new ArrayList<>();

    /**
     * Constructor.
     */
    public EntityUpdatedData() {
    }

    /**
     * Adds a single change.
     * @param name the name
     * @param before the before state
     * @param after the after state
     */
    public void addChange(String name, String before, String after) {
        addChange(new EntityFieldChange(name, before, after));
    }

    /**
     * Adds a single change.
     * @param name the name
     * @param before the before state
     * @param after the after state
     */
    public void addChange(String name, Enum<?> before, Enum<?> after) {
        String beforeStr = before != null ? before.name() : null;
        String afterStr = after != null ? after.name() : null;
        addChange(name, beforeStr, afterStr);
    }

    /**
     * Adds a single change.
     * @param change change to add
     */
    public void addChange(EntityFieldChange change) {
        changes.add(change);
    }

    /**
     * @return the changes
     */
    public List<EntityFieldChange> getChanges() {
        return changes;
    }

    /**
     * @param changes the changes to set
     */
    public void setChanges(List<EntityFieldChange> changes) {
        this.changes = changes;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "EntityUpdatedData [changes=" + changes + "]";
    }

}
