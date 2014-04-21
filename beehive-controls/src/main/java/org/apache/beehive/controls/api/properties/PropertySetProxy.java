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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * The PropertySetProxy class is a dynamic proxy {@link InvocationHandler} class that exposes the
 * values held within a PropertyMap as an Object implementing an annotation type interface.
 * <p>
 * This enables properties resolved using the {@link PropertyMap}'s hiearchical resolution mechanism to
 * be exposed to the client of the proxy in the same way that Java 5 annotations are 
 * exposed using raw Java reflection APIs.   A proxy of this type should behave identically
 * to the one returned from a call to <code>AnnotatedElement.getAnnotation()</code>, but backed
 * by a richer, more dynamic resolution mechanism.
 *
 * @see java.lang.reflect.Proxy
 * @see java.lang.reflect.InvocationHandler
 * @see java.lang.reflect.AnnotatedElement#getAnnotation
 * @see org.apache.beehive.controls.api.properties.PropertySet
 * @see org.apache.beehive.controls.api.properties.PropertyMap
 */
public class PropertySetProxy <T extends Annotation> implements InvocationHandler
{
    /**
     * Creates a new proxy instance implementing the PropertySet interface and backed
     * by the data from the property map.
     *
     * @param propertySet an annotation type that has the PropertySet meta-annotation
     * @param propertyMap the PropertyMap containing property values backing the proxy
     * @return proxy that implements the PropertySet interface
     */
    public static <T extends Annotation> T getProxy(Class<T> propertySet, PropertyMap propertyMap)
    {
        assert propertySet != null && propertyMap != null;

        if (!propertySet.isAnnotation())
            throw new IllegalArgumentException(propertySet + " is not an annotation type");

        return (T)Proxy.newProxyInstance(propertySet.getClassLoader(),
                                         new Class [] {propertySet },
                                         new PropertySetProxy(propertySet, propertyMap));
    }

    /**
     * Private constructor, called only from the getProxy factory method
     */
    private PropertySetProxy(Class<T> propertySet, PropertyMap propertyMap)
    {
        _propertySet = propertySet;
        _propertyMap = propertyMap;
    }

    //
    // InvocationHandler.invoke
    //
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        // Handle cases where Object/Annotation methods are called on this
        // proxy. We were getting null back from Annotation.annotationType.
        Object value = null;
        if (method.getDeclaringClass() == Object.class) 
        {
            try {
                if (method.getName().equals("getClass")) 
                {
                    value = _propertySet;
                }
                else 
                {
                    value = method.invoke(_propertyMap, args);
                }
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        } 
        else if (method.getDeclaringClass() == Annotation.class && 
                 method.getName().equals("annotationType")) 
        {
            value = _propertySet;
        }
        else 
        {

            // Query the nested value in the property map
            PropertyKey key = new PropertyKey(_propertySet, method.getName());
            value = _propertyMap.getProperty(key);

            // If the returned value is itself a PropertyMap (i.e. a nested annotation type),
            // then wrap it in a PropertySetProxy instance before returning.
            if (value instanceof PropertyMap)
            {
                PropertyMap propertyMap = (PropertyMap)value;
                value = getProxy(propertyMap.getMapClass(), propertyMap);
            }
        }

        return value;
    }

    /**
     * Returns the PropertySet annotation type associated with the proxy
     */
    public Class<T> getPropertySet() {
        return _propertySet;
    }

    /**
     * Returns the underlying PropertyMap containing the property values exposed by the
     * proxy.
     */
    public PropertyMap getPropertyMap() {
        return _propertyMap; 
    }

    private Class<T> _propertySet;
    private PropertyMap _propertyMap;
}
