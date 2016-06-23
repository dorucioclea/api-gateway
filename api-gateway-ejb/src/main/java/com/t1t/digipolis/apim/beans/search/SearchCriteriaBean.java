package com.t1t.digipolis.apim.beans.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic search criteria used when searching for beans.
 *
 */
public class SearchCriteriaBean implements Serializable {

    private static final long serialVersionUID = 5103776179000907112L;

    private List<SearchCriteriaFilterBean> filters = new ArrayList<>();
    private OrderByBean orderBy;
    private PagingBean paging;

    /**
     * Constructor.
     */
    public SearchCriteriaBean() {
    }

    /**
     * Adds a single filter to the criteria.
     * @param name the filter name
     * @param value the filter value
     * @param operator the operator type
     */
    public void addFilter(String name, String value, SearchCriteriaFilterOperator operator) {
        SearchCriteriaFilterBean filter = new SearchCriteriaFilterBean();
        filter.setName(name);
        filter.setValue(value);
        filter.setOperator(operator);
        filters.add(filter);
    }

    /**
     * @param page the page
     */
    public void setPage(int page) {
        if (this.paging == null)
            this.paging = new PagingBean();
        getPaging().setPage(page);
    }

    /**
     * @param pageSize size of page
     */
    public void setPageSize(int pageSize) {
        if (this.paging == null)
            this.paging = new PagingBean();
        getPaging().setPageSize(pageSize);
    }

    /**
     * @param name the name
     * @param ascending whether is ascending
     */
    public void setOrder(String name, boolean ascending) {
        if (this.orderBy == null)
            this.orderBy = new OrderByBean();
        orderBy.setName(name);
        orderBy.setAscending(ascending);
    }

    /**
     * @return the filters
     */
    public List<SearchCriteriaFilterBean> getFilters() {
        return filters;
    }

    /**
     * @param filters the filters to set
     */
    public void setFilters(List<SearchCriteriaFilterBean> filters) {
        this.filters = filters;
    }

    /**
     * @return the paging
     */
    public PagingBean getPaging() {
        return paging;
    }

    /**
     * @param paging the paging to set
     */
    public void setPaging(PagingBean paging) {
        this.paging = paging;
    }

    /**
     * @return the orderBy
     */
    public OrderByBean getOrderBy() {
        return orderBy;
    }

    /**
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(OrderByBean orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((filters == null) ? 0 : filters.hashCode());
        result = prime * result + ((orderBy == null) ? 0 : orderBy.hashCode());
        result = prime * result + ((paging == null) ? 0 : paging.hashCode());
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
        SearchCriteriaBean other = (SearchCriteriaBean) obj;
        if (filters == null) {
            if (other.filters != null)
                return false;
        } else if (!filters.equals(other.filters))
            return false;
        if (orderBy == null) {
            if (other.orderBy != null)
                return false;
        } else if (!orderBy.equals(other.orderBy))
            return false;
        if (paging == null) {
            if (other.paging != null)
                return false;
        } else if (!paging.equals(other.paging))
            return false;
        return true;
    }

}
