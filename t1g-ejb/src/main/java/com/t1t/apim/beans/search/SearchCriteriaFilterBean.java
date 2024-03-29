package com.t1t.apim.beans.search;

import java.io.Serializable;

/**
 * Represents a single filter or search criteria.  This is used when searching
 * for beans.
 */
public class SearchCriteriaFilterBean implements Serializable {

    private static final long serialVersionUID = -1199180207971619165L;

    private String name;
    private String value;
    private SearchCriteriaFilterOperator operator;

    /**
     * Constructor.
     */
    public SearchCriteriaFilterBean() {
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
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the operator
     */
    public SearchCriteriaFilterOperator getOperator() {
        return operator;
    }

    /**
     * @param operator the operator to set
     */
    public void setOperator(SearchCriteriaFilterOperator operator) {
        this.operator = operator;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((operator == null) ? 0 : operator.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        SearchCriteriaFilterBean other = (SearchCriteriaFilterBean) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (operator == null) {
            if (other.operator != null)
                return false;
        } else if (!operator.equals(other.operator))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
