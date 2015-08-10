package com.t1t.digipolis.apim.beans.search;

import java.io.Serializable;

/**
 * Models order-by for a search.
 *
 */
public class OrderByBean implements Serializable {

    private static final long serialVersionUID = -7569401325900866820L;

    private boolean ascending;
    private String name;

    /**
     * Constructor.
     */
    public OrderByBean() {
    }

    /**
     * @return the ascending
     */
    public boolean isAscending() {
        return ascending;
    }

    /**
     * @param ascending the ascending to set
     */
    public void setAscending(boolean ascending) {
        this.ascending = ascending;
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

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (ascending ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OrderByBean other = (OrderByBean) obj;
        if (ascending != other.ascending)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
