package com.t1t.apim.beans.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a set of beans returned as a result of a search.
 *
 * @param <T> the bean type
 */
public class SearchResultsBean<T> implements Serializable {

    private static final long serialVersionUID = -1672829715471947181L;

    private List<T> beans = new ArrayList<>();
    private int totalSize;

    /**
     * Constructor.
     */
    public SearchResultsBean() {
    }

    /**
     * @return the beans
     */
    public List<T> getBeans() {
        return beans;
    }

    /**
     * @param beans the beans to set
     */
    public void setBeans(List<T> beans) {
        this.beans = beans;
    }

    /**
     * @return the totalSize
     */
    public int getTotalSize() {
        return totalSize;
    }

    /**
     * @param totalSize the totalSize to set
     */
    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

}
