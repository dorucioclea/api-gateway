package com.t1t.apim.idp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Guillaume Vandecasteele
 * @since 2017
 */
@Entity
@Table(name = "idp_issuers")
public class IdpIssuerBean {

    @Id
    private String issuer;
    @Column(name = "public_key_field_name")
    private String publicKeyFieldName;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getPublicKeyFieldName() {
        return publicKeyFieldName;
    }

    public void setPublicKeyFieldName(String publicKeyFieldName) {
        this.publicKeyFieldName = publicKeyFieldName;
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
                ", publicKeyFieldName='" + publicKeyFieldName + '\'' +
                '}';
    }
}