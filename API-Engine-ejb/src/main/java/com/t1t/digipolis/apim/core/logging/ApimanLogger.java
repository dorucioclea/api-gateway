package com.t1t.digipolis.apim.core.logging;

import org.apache.commons.lang.ObjectUtils.Null;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * Allows injection of an {@link IApimanLogger} instance, with a class passed as the requester
 * <tt><pre>@Inject @ApimanLogger(SomeClass.clazz) IApimanLogger logger;</pre></tt>.
 *
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD, FIELD, PARAMETER})
//TODO change to digiapim
public @interface ApimanLogger {
    /**
     * @return the requesting class
     */
    @Nonbinding Class<?> value() default Null.class;
}
