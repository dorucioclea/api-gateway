package com.t1t.digipolis.qualifier;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by michallispashidis on 7/08/15.
 * Qualifier to annotate specific APIEngine context. Sometimes injections are published in imported dependencies, to be sure using
 * our proper published annotated object, we qualify the @Inject Object with our custom qualifier.
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface APIEngineContext {
}
