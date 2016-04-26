package com.t1t.digipolis.apim.events.qualifiers;

import com.t1t.digipolis.apim.beans.events.EventType;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Guillaume Vandecasteele
 * @since 2016
 */
@Qualifier
@Target({METHOD, PARAMETER, FIELD, TYPE})
@Retention(RUNTIME)
@Documented
public @interface MembershipRequest {
}
