
package com.t1t.kong.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class KongStatus {

    @SerializedName("server")
    @Expose
    private Server server;
    @SerializedName("database")
    @Expose
    private Database database;

    /**
     * 
     * @return
     *     The server
     */
    public Server getServer() {
        return server;
    }

    /**
     * 
     * @param server
     *     The server
     */
    public void setServer(Server server) {
        this.server = server;
    }

    public KongStatus withServer(Server server) {
        this.server = server;
        return this;
    }

    /**
     * 
     * @return
     *     The database
     */
    public Database getDatabase() {
        return database;
    }

    /**
     * 
     * @param database
     *     The database
     */
    public void setDatabase(Database database) {
        this.database = database;
    }

    public KongStatus withDatabase(Database database) {
        this.database = database;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(server).append(database).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof KongStatus) == false) {
            return false;
        }
        KongStatus rhs = ((KongStatus) other);
        return new EqualsBuilder().append(server, rhs.server).append(database, rhs.database).isEquals();
    }

}
