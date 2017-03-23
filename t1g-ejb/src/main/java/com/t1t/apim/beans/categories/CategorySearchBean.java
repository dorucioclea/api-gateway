package com.t1t.apim.beans.categories;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by michallispashidis on 21/08/15.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
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
