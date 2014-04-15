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
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * The BeanPropertyMap class represents a collection of property values where properties are
 * stored in a local HashMap.
 */
public class BeanPropertyMap extends BaseMap implements PropertyMap,java.io.Serializable
{
    private static final HashMap _primToObject = new HashMap();

    static
    {
        _primToObject.put(Integer.TYPE, Integer.class);
        _primToObject.put(Long.TYPE, Long.class);
        _primToObject.put(Short.TYPE, Short.class);
        _primToObject.put(Byte.TYPE, Byte.class);
        _primToObject.put(Float.TYPE, Float.class);
        _primToObject.put(Double.TYPE, Double.class);
        _primToObject.put(Character.TYPE, Character.class);
        _primToObject.put(Boolean.TYPE, Boolean.class);
    }

    /**
     * Creates an empty BeanPropertyMap associated with the specific Control public
     * interface, PropertySet, or annotation type.
     */
    public BeanPropertyMap(Class mapClass)
    {
        setMapClass(mapClass);
    }

    /**
     * Creates a BeanPropertyMap that wraps another PropertyMap.  Any changes via setProperty
     * will be maintained locally on the constructed map, but getProperty will delegate to the 
     * base PropertyMap for properties not set locally.
     */
    public BeanPropertyMap(PropertyMap map)
    {
        setMapClass(map.getMapClass());
        setDelegateMap(map); 
    }

    /**
     * Creates a BeanPropertyMap where default values are derived from a single annotation
     * type instance.  This can be used to create a map from a property getter return value,
     * to modify element values.
     */
    public <T extends Annotation> BeanPropertyMap(T annot)
    {
        // If the annotation value is actually a PropertySetProxy, then unwrap it and use
        // the standard delegation model
        try
        {
            Object handler = Proxy.getInvocationHandler(annot);
            if (handler instanceof PropertySetProxy)
            {
                PropertySetProxy psp = (PropertySetProxy)handler;
                setMapClass(psp.getPropertySet());
                setDelegateMap(psp.getPropertyMap());
                return;
            }
        }
        catch (IllegalArgumentException iae) {}     // regular annotation

        _annot = annot;
        setMapClass(annot.getClass());
    }

    /**
     * Sets the property specifed by 'key' within this map.
     */
    public synchronized void setProperty(PropertyKey key, Object value)
    {
        if (!isValidKey(key))
            throw new IllegalArgumentException("Key " + key + " is not valid for " + getMapClass());

        //
        // Validate the value argument, based upon the property type reference by the key
        //
        Class propType = key.getPropertyType();
        if (value == null)
        {
            if (propType.isPrimitive() || propType.isAnnotation())
                throw new IllegalArgumentException("Invalid null value for key " + key);
        }
        else
        {
            if (propType.isPrimitive())
                propType = (Class)_primToObject.get(propType);

            if (!propType.isAssignableFrom(value.getClass()))
            {
                throw new IllegalArgumentException("Value class (" + value.getClass() + 
                                                   ") not of expected type: " + propType);
            }
        }
        _properties.put(key, value);
        _propertySets.add(key.getPropertySet());
    }

    /**
     * Returns the property value specified by 'key' within this map.
     */
    public Object getProperty(PropertyKey key)
    {
        if (!isValidKey(key))
            throw new IllegalArgumentException("Key " + key + " is not valid for " + getMapClass());

        //
        // Check local properties first
        //
        if (_properties.containsKey(key))
            return _properties.get(key);

        //
        // Return the value of the annotation type instance (if any)
        //
        if (_annot != null)
            return key.extractValue(_annot);

        //
        // Call up to superclass, for delegation model / default value
        //
        return super.getProperty(key);
    }

    /**
     * Returns true if the PropertyMap contains one or more values for the specified
     * PropertySet, false otherwise
     */
    public boolean containsPropertySet(Class<? extends Annotation> propertySet)
    {
        // If we have an annotation type instance and it matches up with the requested
        // type, then return true
        if (_annot != null && _annot.getClass().equals(propertySet))
            return true;

        if (_propertySets.contains(propertySet))
            return true;

        //
        // Call up to superclass, for delegation model
        //
        return super.containsPropertySet(propertySet);
    }

    /**
     * Returns the set of PropertyKeys that are locally set in this property map. Note:
     * this <b>does not</b> include any properties that might be set as a result of
     * property lookup delegation.
     */
    public Set<PropertyKey> getPropertyKeys() { return _properties.keySet(); }

    // local default annotation value, only set if annot constructor form is used
    Annotation _annot;

    // locally maintained property values
    HashMap<PropertyKey,Object> _properties = new HashMap<PropertyKey,Object>();   

    // locally maintained PropertySets
    HashSet<Class>   _propertySets = new HashSet<Class>();
}
