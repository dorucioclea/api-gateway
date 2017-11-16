package com.t1t.apim.beans.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractPaginationBean<T> implements Serializable {

    private Set<T> data;
    private Long total;
    private String nextPage;
    private String currentPage;

    public AbstractPaginationBean() {
    }

    public Set<T> getData() {
        return data;
    }

    public void setData(Set<T> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPaginationBean<?> that = (AbstractPaginationBean<?>) o;

        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (total != null ? !total.equals(that.total) : that.total != null) return false;
        if (nextPage != null ? !nextPage.equals(that.nextPage) : that.nextPage != null) return false;
        return currentPage != null ? currentPage.equals(that.currentPage) : that.currentPage == null;

    }

    @Override
    public int hashCode() {
        int result = data != null ? data.hashCode() : 0;
        result = 31 * result + (total != null ? total.hashCode() : 0);
        result = 31 * result + (nextPage != null ? nextPage.hashCode() : 0);
        result = 31 * result + (currentPage != null ? currentPage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AbstractPaginationBean{" +
                "data=" + data +
                ", total=" + total +
                ", nextPage=" + nextPage +
                ", currentPage=" + currentPage +
                '}';
    }
}