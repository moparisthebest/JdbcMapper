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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.api.bean.ControlInterface;

/**
 * The AnnotatedElementMap represents a read-only PropertyMap where property values are
 * derived from Java 5.0 (JSR-175) annotations.
 */
public class AnnotatedElementMap
    extends BaseMap
    implements PropertyMap,java.io.Serializable
{
    /**
     * Creates a new PropertyMap that is initialized based upon the type and annotations
     * associated with an AnnotatedElement.
     */ 
    public AnnotatedElementMap(AnnotatedElement annotElem)
    {
        if (annotElem instanceof Class)
            setMapClass((Class)annotElem);
        else if (annotElem instanceof Field)
            setMapClass(((Field)annotElem).getType());
        else if (annotElem instanceof Method)
        {
            Class mapClass = getMethodMapClass((Method)annotElem);
            setMapClass(mapClass);
        }
        else
            throw new IllegalArgumentException("Unsupported element type: " + annotElem.getClass());

        _annotElem = annotElem;
    }

    // For methods, make sure we find a declaring class that is a valid
    // map class. For extended callback methods, we need to walk up a bit
    // further in the hierarchy.

    Class getMethodMapClass(Method method) {

        Class origMapClass = method.getDeclaringClass();
        Class mapClass = origMapClass;
        while (mapClass != null && !isValidMapClass(mapClass)) {
            mapClass = mapClass.getDeclaringClass();
        }
        if (mapClass == null) {
            mapClass = origMapClass;
        }
        return mapClass;
    }

    boolean isValidMapClass(Class mapClass) {
        if (ControlBean.class.isAssignableFrom(mapClass))
        {
            return true;
        }
        else
        {
            if (mapClass.isAnnotation() ||
                mapClass.isAnnotationPresent(ControlInterface.class) ||
                mapClass.isAnnotationPresent(ControlExtension.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the property specifed by 'key' within this map.
     */
    public void setProperty(PropertyKey key, Object value)
    {
        throw new IllegalStateException("AnnotatedElementMap is a read-only PropertyMap");
    }

    /**
     * Returns the property value specified by 'key' within this map.
     */
    public Object getProperty(PropertyKey key)
    {
        if (!isValidKey(key))
            throw new IllegalArgumentException("Key " + key + " is not valid for " + _mapClass);


        //
        // Look for the property value on the associated annotated element
        //
        Class propertySet = key.getPropertySet();
        Annotation annot = _annotElem.getAnnotation(propertySet);
        if (annot != null)
            return key.extractValue(annot);

        //
        // If the property supports inheritance and the annotated element is an interface,
        // then we'll search up the ControlInheritance/Extension hierachy to see if it is
        // provided higher up the chain.
        //
        if (propertySet.isAnnotationPresent(Inherited.class) && _annotElem instanceof Class)
        {
            Class controlIntf = (Class)_annotElem;
            do
            {
                Class [] superIntfs = controlIntf.getInterfaces();
                controlIntf = null;
                for (int i = 0; i < superIntfs.length; i++)
                {
                    if (superIntfs[i].isAnnotationPresent(ControlInterface.class) ||
                        superIntfs[i].isAnnotationPresent(ControlExtension.class))
                    {
                        controlIntf = superIntfs[i];
                        annot = controlIntf.getAnnotation(propertySet);
                        if (annot != null)
                            return key.extractValue(annot);
                    }
                }

            }
            while (controlIntf != null);
        }

        //
        // Call up to superclass for delegation / default value
        //
        return super.getProperty(key);
    }

    /**
     * Returns true if the PropertyMap contains one or more values for the specified
     * PropertySet, false otherwise
     */
    public boolean containsPropertySet(Class<? extends Annotation> propertySet)
    {
        if (_annotElem.isAnnotationPresent(propertySet))
            return true;

        //
        // Call up to superclass for delegation
        //
        return super.containsPropertySet(propertySet);
    }

    /**
     * Returns the AnnotatedElement used for PropertyMap values.
     */
    public AnnotatedElement getAnnotatedElement()
    {
        return _annotElem;
    }

    /**
     * Returns a String version of method argument lists based upon the method argument types
     */
    private String getMethodArgs(Method m)
    {
        StringBuffer sb = new StringBuffer();
        Class [] argTypes = m.getParameterTypes();
        for (int i = 0; i < argTypes.length; i++)
        {
            if (i != 0) sb.append(",");
            sb.append(argTypes[i].toString());
        }
        return sb.toString();
    }

    /**
     * Overrides the standard Serialization writeObject method to compute and store the element
     * information in a serializable form.
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        //
        // When serializing, compute sufficient information about the annotated element to
        // allow it to be reassociated later in readObject
        //
        if (_annotElem instanceof Class)
        {
            _elemClass = (Class)_annotElem;
            _elemDesc = null;   // non required
        }
        else if (_annotElem instanceof Field)
        {
            Field f = (Field)_annotElem;
            _elemClass = f.getDeclaringClass();
            _elemDesc = f.getName();
        }
        else if (_annotElem instanceof Method)
        {
            Method m = (Method)_annotElem;
            _elemClass = m.getDeclaringClass();
            _elemDesc = m.getName() + "(" + getMethodArgs(m) + ")";
        } 

        out.defaultWriteObject();
    } 

    /**
     * Overrides the standard Serialization readObject implementation to reassociated with the
     * target AnnotatedElement after deserialization.
     */
    private void readObject(java.io.ObjectInputStream in) 
                 throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();

        if (_elemDesc == null)  // element is a Class
            _annotElem = _elemClass;
        else
        {
            int argsIndex = _elemDesc.indexOf('(');
            if (argsIndex < 0)  // element is a Field
            {
                try
                {
                    _annotElem = _elemClass.getDeclaredField(_elemDesc);
                }
                catch (NoSuchFieldException nsfe)
                {
                    throw new IOException("Unable to locate field " + nsfe);
                }
            }
            else // element is a method
            {
                String methodName = _elemDesc.substring(0, argsIndex);
                if (_elemDesc.charAt(argsIndex+1) == ')')
                {
                    // At least handle the null args case quickly
                    try
                    {
                        _annotElem = _elemClass.getDeclaredMethod(methodName, new Class [] {});
                    }
                    catch (NoSuchMethodException nsme)
                    {
                        throw new IOException("Unable to locate method " +_elemDesc);
                    }
                }
                else
                {
                    // Linear search for the rest :(
                    String methodArgs = _elemDesc.substring(argsIndex+1, _elemDesc.length()-1);
                    Method [] methods = _elemClass.getDeclaredMethods();
                    for (int i = 0; i < methods.length; i++)
                    {
                        if (methods[i].getName().equals(methodName) &&
                            getMethodArgs(methods[i]).equals(methodArgs))
                        {
                            _annotElem = methods[i];
                            break;
                        }
                    }

                    if (_annotElem == null)
                    {
                        throw new IOException("Unable to locate method " +  _elemDesc);
                    }
                }
            }
        }
    }

    // The AnnotatedElement upon which this PropertyMap is based.  This is marked transient,
    // because many Reflection types are not Serializable. 
    transient private AnnotatedElement _annotElem;

    private Class  _elemClass;   // Class associated with the annotated element
    private String _elemDesc;    // Description of the element
}
