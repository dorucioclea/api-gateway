package com.t1t.digipolis.apim.beans.operation;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Entity
@Table(name = "operating_modes")
public class OperatingBean implements Serializable {

    @Id
    @Enumerated(EnumType.STRING)
    private OperatingModes id;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "message")
    private String message;

    public OperatingModes getId() {
        return id;
    }

    public void setId(OperatingModes id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OperatingBean that = (OperatingBean) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OperatingBean{" +
                "id=" + id +
                ", enabled=" + enabled +
                ", message='" + message + '\'' +
                '}';
    }
}