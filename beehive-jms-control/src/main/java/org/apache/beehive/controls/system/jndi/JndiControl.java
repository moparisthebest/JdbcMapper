/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */

package org.apache.beehive.controls.system.jndi;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.naming.InitialContext;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.AnnotationMemberTypes;
import org.apache.beehive.controls.api.packaging.FeatureInfo;
import org.apache.beehive.controls.api.properties.PropertySet;

/**
 * The Jndi Control encapsulates access to the JNDI context. It
 * provides annotation for setting the factory and provider URL.
 */
@ControlInterface (defaultBinding="org.apache.beehive.controls.system.jndi.impl.JndiControlImpl")
public interface JndiControl
{
    /**
     * Get a JNDI based resource.
     * @param resource the resource name.
     * @param resourceClass the resource class.
     * @return the resource object.
     * @throws ControlException
     */
    public Object getResource(String resource,Class resourceClass) throws ControlException;

    /**
     * Get the JNDI initial context.
     * @return the initial context.
     *
     * @throws ControlException
     */
    public InitialContext getInitialContext() throws ControlException;

    @PropertySet
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE,ElementType.FIELD})
    public @interface Properties {
        /**
         * The JNDI context factory class name.
         */
        @FeatureInfo(shortDescription="JNDI context factory")
        @AnnotationMemberTypes.Optional
        String factory();

        /**
         * The JNDI provider URL.
         */
        @FeatureInfo(shortDescription="JNDI provider URL")      
        @AnnotationMemberTypes.Optional
        @AnnotationMemberTypes.URI
        String url();

        /**
         * The JNDI security principal.
         */
        @FeatureInfo(shortDescription="JNDI security principal")      
        @AnnotationMemberTypes.Optional
    	public String jndiSecurityPrincipal() default "";

        /**
         * The JNDI security credentials.
         */
        @FeatureInfo(shortDescription="JNDI security credentials")      
        @AnnotationMemberTypes.Optional
    	public String jndiSecurityCredentials() default "";
    }
}
