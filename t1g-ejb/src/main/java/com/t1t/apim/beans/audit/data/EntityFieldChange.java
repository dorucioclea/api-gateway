package com.t1t.apim.beans.audit.data;


/**
 * Models a change in an entity field's value for auditing purposes.
 */
public class EntityFieldChange {

    private String name;
    private String before;
    private String after;

    /**
     * Constructor.
     *
     * @param name   the name
     * @param before the before state
     * @param after  the after state
     */
    public EntityFieldChange(String name, String before, String after) {
        this.name = name;
        this.setBefore(before);
        this.setAfter(after);
    }

    /**
     * @return the before
     */
    public String getBefore() {
        return before;
    }

    /**
     * @param before the before to set
     */
    public void setBefore(String before) {
        this.before = before;
    }

    /**
     * @return the after
     */
    public String getAfter() {
        return after;
    }

    /**
     * @param after the after to set
     */
    public void setAfter(String after) {
        this.after = after;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    @SuppressWarnings("nls")
    public String toString() {
        return "EntityFieldChange [name=" + name + ", before=" + before + ", after=" + after + "]";
    }

}
