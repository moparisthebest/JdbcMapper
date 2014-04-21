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
package org.apache.beehive.controls.api.properties;

import java.lang.annotation.Annotation;

/**
 * The PropertyMap interface represents a collection of ControlBean properties.  Concrete
 * implementations of this interface might derive property values from a local Map, Java 5.0
 * annotations, external configuration, or other property sources.
 */
public interface PropertyMap 
{
    /**
     * Returns the PropertySet or Control interface class associated with the PropertyMap.
     */
    public Class getMapClass();

    /**
     * Sets a delegate base property map from which values will be derived if not found within
     * the local property map.
     */
    public void setDelegateMap(PropertyMap delegateMap);

    /**
     * Returns a delegate base property map from which values will be derived if not found within
     * the local property map.
     */
    public PropertyMap getDelegateMap();

    /**
     * Sets the property specifed by 'key' within this map.
     */
    public void setProperty(PropertyKey key, Object value);

    /**
     * Returns the property value specified by 'key' within this map.
     */
    public Object getProperty(PropertyKey key);

    /**
     * Returns true if the PropertyMap contains one or more values for the specified
     * PropertySet, false otherwise
     */
    public boolean containsPropertySet(Class<? extends Annotation> propertySet);

    /**
     * Returns a PropertySet proxy instance that derives its data from the contents of
     * the property map.  Will return null if the PropertyMap does not contain any properties 
     * associated with the specified PropertySet.
     */
    public <T extends Annotation> T getPropertySet(Class<T> propertySet);
}
