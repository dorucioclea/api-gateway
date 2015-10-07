/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.t1t.digipolis.util;
import com.t1t.digipolis.qualifier.APIEngineContext;
import com.t1t.digipolis.apim.kong.KongClient;
import com.t1t.digipolis.apim.kong.RestServiceBuilder;
import net.sf.ehcache.Ehcache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 * 
 * <p>
 * Example injection on a managed bean field:
 * </p>
 */
public class Resources {
    /**
     * REST Service Builder (Retrofit)
     */
    @Inject
    private RestServiceBuilder serviceBuilder;
    @Inject
    private CacheUtil cacheUtil;

    @Produces @APIEngineContext
    @PersistenceContext
    private EntityManager em;

    @Produces
    @APIEngineContext
    public Logger produceLog(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @APIEngineContext
    public Ehcache produceClientAppCache(){
        return cacheUtil.getClientAppCache();
    }

}
