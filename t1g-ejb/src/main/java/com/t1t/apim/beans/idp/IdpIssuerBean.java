package com.t1t.apim.beans.idp;

import javax.persistence.*;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "idp_issuers")
public class IdpIssuerBean {

    @Id
    private String issuer;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private IdpType type;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public IdpType getType() {
        return type;
    }

    public void setType(IdpType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdpIssuerBean)) return false;

        IdpIssuerBean that = (IdpIssuerBean) o;

        if (!issuer.equals(that.issuer)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return issuer.hashCode();
    }

    @Override
    public String toString() {
        return "IdpIssuerBean{" +
                "issuer='" + issuer + '\'' +
                ", type=" + type +
                '}';
    }
}
