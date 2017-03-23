package com.t1t.apim.beans.iprestriction;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by michallispashidis on 12/02/16.
 */
@Entity
@Table(name = "white_ip_restriction")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WhitelistBean extends IPRestrictionBean implements Serializable{
    public WhitelistBean() {}
    @Id
    @Column(name = "netw_value", nullable=false)
    private String netwValue;

    public String getNetwValue() {
        return netwValue;
    }

    public void setNetwValue(String netwValue) {
        this.netwValue = netwValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WhitelistBean)) return false;
        WhitelistBean that = (WhitelistBean) o;
        return netwValue.equals(that.netwValue);

    }

    @Override
    public int hashCode() {
        return netwValue.hashCode();
    }

    @Override
    public String toString() {
        return "WhitelistBean{" +
                "netwValue='" + netwValue + '\'' +
                '}';
    }
}
