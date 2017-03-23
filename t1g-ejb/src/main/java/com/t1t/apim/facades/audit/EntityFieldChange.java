package com.t1t.apim.facades.audit;

/**
 * Models a change in an entity field's value for auditing purposes.
 */
public class EntityFieldChange {
    
    private String before;
    private String after;
    
    /**
     * Constructor.
     */
    public EntityFieldChange() {
    }

    /**
     * Constructor.
     * @param before the before state
     * @param after the after state
     */
    public EntityFieldChange(String before, String after) {
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

}
