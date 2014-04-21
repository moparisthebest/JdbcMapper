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
import java.lang.reflect.Method;

import org.apache.beehive.controls.api.ControlException;

/**
 * The PropertyKey class represents a key that can be used to set a JSR-175 attribute member
 * value within a <code>PropertyMap</code>.
 */
public class PropertyKey implements java.io.Serializable
{
    /**
     * This constructor takes the JSR-175 metadata interface that is associated with
     * the contained attributes.
     */
    public PropertyKey(Class<? extends Annotation> propertySet, String propertyName)
    {
        if (!propertySet.isAnnotation())
        {
            throw new IllegalArgumentException("Class " + propertySet + " is not a valid annotation type");
        } 

        try
        {
            _getMethod = propertySet.getMethod(propertyName, (Class [])null);
            _propertySet = propertySet;
            _propertyName = propertyName;
            _propertyType = _getMethod.getReturnType();

            //
            // Compute a hash code for the key instance that will be constant for all keys
            // that reference the same interface/member combo
            // 
            _hashCode = new String(propertySet.getName() + "." + propertyName).hashCode();
        }
        catch (NoSuchMethodException nsme)
        {
            throw new IllegalArgumentException(propertyName + 
                          "is not a valid member of the metadata interface " + propertySet);
        }
    }

    protected Method getMethod()
    {
        if (null == _getMethod)
        {
            try
            {
                _getMethod = _propertySet.getMethod(_propertyName, (Class [])null);
            }
            catch(NoSuchMethodException nsmEx)
            {
                // This can only happen if a PropertySet is incompatibly changed after
                // serialization of a PropertyKey (since it is initially validated in
                // the constructor).
                throw new ControlException("Unable to locate PropertyKey accessor method", nsmEx);
            }
        }
        return _getMethod;
    }

    /**
     * Computes the default value for the value of this property key, or null if there
     * is no defined default.
     */
    public Object getDefaultValue()
    {
        // Query the accessor method for the default value
        // This method will return 'null' if there is no defined default
        return getMethod().getDefaultValue();
    }

    /**
     * Extracts the value of the key from an Annotation instance
     */
    /* package */ Object extractValue(Annotation annot)
    {
        try
        {
            return getMethod().invoke(annot, new Object [] {});
        }
        // TODO -- cleanup exception handling, property defining a PropertyException
        catch (RuntimeException re) { throw re; }
        catch (Exception e)
        {
            throw new RuntimeException("Unable to extract value for " + _propertyName, e); 
        }
    }

    public boolean equals(Object obj)
    {
        // fast success for static key declaration cases
        if (this == obj)    
            return true;

        // fast fail on obvious differences
        if (obj == null || !(obj instanceof PropertyKey) || _hashCode != obj.hashCode())
            return false;

        // slower success on two equivalent keys constructed independently
        PropertyKey keyObj = (PropertyKey)obj;
        return _propertySet.equals(keyObj._propertySet) && 
               _propertyName.equals(keyObj._propertyName);
    }

    public int hashCode() {
        return _hashCode;
    }

    public String toString() 
    { 
        return "PropertyKey: " + _propertySet.getName() + "." + _propertyName; 
    }

    public Class<? extends Annotation> getPropertySet() {
        return  _propertySet;
    }

    public String getPropertyName() {
        return _propertyName;
    }

    public Class getPropertyType() {
        return _propertyType;
    }

    public Annotation[] getAnnotations() {
        return getMethod().getAnnotations();
    }

    Class<? extends Annotation>  _propertySet;
    String  _propertyName;
    Class   _propertyType;
    int     _hashCode;

    // WARNING: This field should never be accessed directly but instead via the getMethod()
    // API.  This ensures that the (transient) value is appropriately recomputed when necessary.
    private transient Method  _getMethod;
}