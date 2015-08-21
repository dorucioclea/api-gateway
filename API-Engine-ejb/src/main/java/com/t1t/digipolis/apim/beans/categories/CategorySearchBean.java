package com.t1t.digipolis.apim.beans.categories;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.List;

/**
 * Created by michallispashidis on 21/08/15.
 */
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class CategorySearchBean {
    public CategorySearchBean() {
    }

    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "categories=" + categories +
                '}';
    }
}
