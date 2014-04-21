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
package org.apache.beehive.netui.pageflow.interceptor;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * <p>
 * Class used to hold configuration information for an {@link Interceptor}.  Each
 * interceptor instance will have it's own configuration object which holds the
 * interceptor implementation class and a set of custom properties that can be provided
 * for each interceptor instance.
 * </p>
 */ 
public class InterceptorConfig
    implements Serializable
{
    private String _interceptorClass;
    private Map/*< String, String >*/ _customProperties = new HashMap/*< String, String >*/();

    /**
     * Default constructor.
     */
    protected InterceptorConfig()
    {
    }

    /**
     * Create this config object for an intereceptor with the given class name.
     * @param interceptorClass the {@link Interceptor}'s class name
     */
    protected InterceptorConfig( String interceptorClass )
    {
        _interceptorClass = interceptorClass;
    }

    /**
     * Get the interceptor class.
     * @return the String for the interceptor's class
     */
    public String getInterceptorClass()
    {
        return _interceptorClass;
    }

    /**
     * Set the interceptor class.
     * @param interceptorClass the String for the interceptor's class
     */
    public void setInterceptorClass( String interceptorClass )
    {
        _interceptorClass = interceptorClass;
    }

    /**
     * Get the interceptor's custom properties.  This returned {@link Map} contains pairs
     * of &lt;String, String&gt;.
     * @return the map of custom properties
     */
    public Map/*< String, String >*/ getCustomProperties()
    {
        return _customProperties;
    }

    /**
     * Get the custom property value corresponding to the given property name.
     * @param name the name of the property
     * @return the value of the property; if the property name isn't found, the value will be null
     */
    public String getCustomProperty( String name )
    {
        return ( String ) _customProperties.get( name );
    }

    /**
     * Internal method used by the framework to add custom interceptor config properties.
     * @param name the custom property's name
     * @param value the custom property's value
     */
    void addCustomProperty( String name, String value )
    {
        _customProperties.put( name, value );
    }
}