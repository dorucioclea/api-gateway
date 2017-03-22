package com.t1t.digipolis.apim.beans.categories;

import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
public class TagBean implements Serializable {

    private String tag;

    public TagBean() {
    }

    public TagBean(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagBean tagBean = (TagBean) o;

        return tag != null ? tag.equals(tagBean.tag) : tagBean.tag == null;

    }

    @Override
    public int hashCode() {
        return tag != null ? tag.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TagBean{" +
                "tag='" + tag + '\'' +
                '}';
    }
}