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

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.bean.ExternalPropertySets;

/**
 * The BaseMap class provide an abstract base PropertyMap class from which other
 * concrete PropertyMap implementation can derive.  It contains some common code
 * (such as property key validation and the implementation of the base delegation model)
 * that is generically useful.
 */
abstract public class BaseMap implements PropertyMap, java.io.Serializable
{
    /**
     * Sets the PropertySet or Control interface associated with this map.  Only properties
     * declared by the PropertySet or one of the PropertySets on the Control interface may
     * be used with this map.
     */
    protected void setMapClass(Class mapClass)
    {
        //
        // If the provided map class is a ControlBean type, then locate associated control
        // interface or extension that defines properties.
        //
        if (ControlBean.class.isAssignableFrom(mapClass))
        {
            Class [] intfs = mapClass.getInterfaces();
            for (int i = 0; i < intfs.length; i++)
            {
                if (intfs[i].isAnnotationPresent(ControlInterface.class) ||
                    intfs[i].isAnnotationPresent(ControlExtension.class))
                {
                    mapClass = intfs[i];
                    break;
                }
            }
        }
        else
        {
            if (!mapClass.isAnnotation() &&
                !mapClass.isAnnotationPresent(ControlInterface.class) &&
                !mapClass.isAnnotationPresent(ControlExtension.class))
                throw new IllegalArgumentException(mapClass+" must be Control or annotation type");
        }

        _mapClass = mapClass;
    }

    /**
     * Returns the PropertySet or Control interface class associated with the PropertyMap.
     */
    public Class getMapClass() { return _mapClass; }

    /**
     * Checks to see if the provided class is a control or property set interface that is
     * compatible with the local PropertyMap.
     */
    private boolean isCompatibleClass(Class checkClass)
    {
        //
        // If the check class is equal to or a super-interface of the map class, then
        // they are compatible.
        //
        if (_mapClass.isAssignableFrom(checkClass))
            return true;

        //
        // If the check class is a property set declared by the map class or a super interface
        // of the map class, then they are compatible.
        //
        if (checkClass.isAnnotationPresent(PropertySet.class))
        {
            Class declaringClass = checkClass.getDeclaringClass();

            // External property sets are always compatible.
            // TODO: Could do a more extensive check..
            if (declaringClass == null)
                return true;

            if (declaringClass.isAssignableFrom(_mapClass))
                return true;
        }

        //
        // If the map class is a property set declared by the check class or a super interface
        // of the check class, then they are compatible.  This is the inverse of the last check,
        // and happens e.g. when a programatically instantiated control w/ an initial property
        // map needs to delegate to the control interface's property map.
        //
        if (_mapClass.isAnnotationPresent(PropertySet.class))
        {
            Class declaringClass = _mapClass.getDeclaringClass();
            if (declaringClass != null &&
                declaringClass.isAssignableFrom(checkClass))
                return true;

            // External property sets have no declaring class
            if (declaringClass == null)
            {
                ExternalPropertySets eps = (ExternalPropertySets) checkClass.getAnnotation(ExternalPropertySets.class);
                if (eps != null)
                {
                    Class[] propSets = eps.value();
                    if (propSets != null)
                    {
                        for (Class ps : propSets)
                        {
                            if (_mapClass.equals(ps))
                                return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Checks to ensure that the provided key is a valid key for this PropertyMap
     */
    protected boolean isValidKey(PropertyKey key)
    {
        return isCompatibleClass(key.getPropertySet());
    }

    /**
     * Sets a delegate base property map from which values will be derived if not found within
     * the local property map.
     */
    public synchronized void setDelegateMap(PropertyMap delegateMap)
    {
        if (!isCompatibleClass(delegateMap.getMapClass()))
            throw new IllegalArgumentException("The delegate map type (" + delegateMap.getMapClass() + " is an incompatible type with " + _mapClass);

        _delegateMap = delegateMap;
    }

    /**
     * Returns a delegate base property map from which values will be derived if not found within
     * the local property map.
     */
    public PropertyMap getDelegateMap()
    {
        return _delegateMap;
    }

    /**
     * Returns the property value specified by 'key' within this map.
     */
    public Object getProperty(PropertyKey key)
    {
        //
        // Delegate up to any parent map
        //
        if (_delegateMap != null)
            return _delegateMap.getProperty(key);

        //
        // If neither found a value, return the default value
        //
        return key.getDefaultValue();
    }

    /**
     * Returns true if the PropertyMap contains one or more values for the specified
     * PropertySet, false otherwise.
     */
    public boolean containsPropertySet(Class<? extends Annotation> propertySet)
    {
        //
        // Defer to any delegate map
        //
        if (_delegateMap != null)
            return _delegateMap.containsPropertySet(propertySet);

        return false;
    }

    /**
     * Returns a PropertySet proxy instance that derives its data from the contents of
     * the property map.  Will return null if the PropertyMap does not contain any properties 
     * associated with the specified PropertySet.
     */
    public <T extends Annotation> T getPropertySet(Class<T> propertySet)
    {
        if (!containsPropertySet(propertySet))
            return null;

        return PropertySetProxy.getProxy(propertySet, this);
    }

    Class            _mapClass;                     // associated Control or PropertySet class
    PropertyMap      _delegateMap;                  // wrapped PropertyMap (or null)
}
