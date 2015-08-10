package com.t1t.digipolis.apim.jpa;

import org.hibernate.dialect.H2Dialect;

import java.sql.Types;

/**
 * A custom h2 dialect to work around https://hibernate.atlassian.net/browse/HHH-9693
 *
 */
public class ApimanH2Dialect extends H2Dialect {

    /**
     * Constructor.
     */
    public ApimanH2Dialect() {
        registerColumnType(Types.LONGVARCHAR, String.format("varchar(%d)", Integer.MAX_VALUE)); //$NON-NLS-1$
    }

}
