package com.t1t.apim.beans.services;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class DefaultServiceTermsBean {

    public DefaultServiceTermsBean() {
    }

    public DefaultServiceTermsBean(String terms) {
        this.terms = terms;
    }

    private String terms;

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultServiceTermsBean that = (DefaultServiceTermsBean) o;

        return terms != null ? terms.equals(that.terms) : that.terms == null;

    }

    @Override
    public int hashCode() {
        return terms != null ? terms.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DefaultServiceTermsBean{" +
                "terms='" + terms + '\'' +
                '}';
    }
}