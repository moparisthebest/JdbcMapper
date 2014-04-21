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
package org.apache.beehive.controls.runtime.packaging;

import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;

/**
 * The ControlEventSetDescriptor is a result of an infortunate evoluntary flaw in the 
 * java.beans.EventSetDescriptor class.  The getListeners functionality for event sets was
 * added after the initial implementation, and unfortunately, there is no constructor that
 * let you specify <b>both</b> the MethodDescriptors for events <b>and</b> the getListener
 * method.  To compensate for this, we must subclass and provide our own getGetListenerMethod
 * implementation.
 */
public class ControlEventSetDescriptor extends EventSetDescriptor
{
    /**
     * This constructor adds the getListenerMethod argument that is missing from the JDK!
     */
    public ControlEventSetDescriptor(String eventSetName,
                                     Class<?> listenerType,
                                     MethodDescriptor[] listenerMethodDescriptors,
                                     Method addListenerMethod,
                                     Method removeListenerMethod,
                                     Method getListenerMethod)
           throws IntrospectionException
    {
        super(eventSetName, listenerType, listenerMethodDescriptors, addListenerMethod, removeListenerMethod);

        // Follow the same pattern as the JDK and store the method as a soft reference, so
        // the introspector (alone) won't prevent Class garbage collection.
        _getMethodRef = new SoftReference<Method>(getListenerMethod);
    }

    /**
     * Override the default implementation of getGetListenerMethod to return the method
     * provided in the constructor.
     */
    public Method getGetListenerMethod()
    {
        if (_getMethodRef == null)
            return null;

        return _getMethodRef.get();
    }

    Reference<Method> _getMethodRef;
}
