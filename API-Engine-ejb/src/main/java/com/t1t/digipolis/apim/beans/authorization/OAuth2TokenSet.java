package com.t1t.digipolis.apim.beans.authorization;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OAuth2TokenSet {

    private Long currentPage;
    private Long nextPage;
    private Set<OAuth2TokenBean> data;
    private Long total;

    public OAuth2TokenSet() {
    }

    public Set<OAuth2TokenBean> getData() {
        return data;
    }

    public void setData(Set<OAuth2TokenBean> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getNextPage() {
        return nextPage;
    }

    public void setNextPage(Long nextPage) {
        this.nextPage = nextPage;
    }

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OAuth2TokenSet that = (OAuth2TokenSet) o;

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
        return "OAuth2TokenSet{" +
                "data=" + data +
                ", total=" + total +
                ", nextPage=" + nextPage +
                ", currentPage=" + currentPage +
                '}';
    }
}