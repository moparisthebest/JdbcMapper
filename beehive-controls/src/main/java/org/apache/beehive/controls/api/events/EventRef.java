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
package org.apache.beehive.controls.api.events;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * The EventRef class represents a reference to a specific Control event.   EventRefs can
 * be used to fire external events into a Control, in contexts where the event source may
 * not share the associated EventSet class instance with the event target, or even have
 * access to the EventSet class itself.
 * <p> 
 * It is roughly equivalent to the java.lang.reflect.Method object that refers to a method 
 * on an EventSet interface, but has several additional properties:
 * <ul>
 * <li>It is serializable, so can be persisted/restored or passed across the wire</li>
 * <li>It supports materializing the EventRef back to a Method reference in a way that allows
 * EventRefs to be passed across class loaders</li>
 * <li>It can be constructed in contexts where a reference to the actual EventSet class might
 * not be available (using a String event descriptor format to describe events)</li>
 * </ul> 
 */
public class EventRef implements java.io.Serializable
{
    //
    // Static helper map to go from a primitive type to the type descriptor string
    //
    static private HashMap<Class,String> _primToType = new HashMap<Class,String>();
    static
    {
        _primToType.put(Integer.TYPE, "I");
        _primToType.put(Boolean.TYPE, "Z");
        _primToType.put(Byte.TYPE, "B");
        _primToType.put(Character.TYPE, "C");
        _primToType.put(Short.TYPE, "S");
        _primToType.put(Long.TYPE, "J");
        _primToType.put(Float.TYPE, "F");
        _primToType.put(Double.TYPE, "D");
        _primToType.put(Void.TYPE, "V");
    }

    /**
     * Constructs a new EventRef based upon a Method reference.  The input method must be one
     * that is declared within a Control EventSet interface.
     * @param eventMethod the Method associated with the event
     */
    public EventRef(Method eventMethod)
    {
        _method = eventMethod;
        _descriptor = computeEventDescriptor(eventMethod);
    }

    /**
     * Constructs a new EventRef using an event descriptor string.  The format of this string
     * is: 
     * <pre> 
     *      <eventSet>.<eventName><eventDescriptor>
     * </pre> 
     * where <i>eventSet</i> refers to the fully qualified name of the EventSet class,
     * <i>eventName</i> refers to the name of the event Method, and <i>eventDescriptor</i>
     * describes the event argument and return types using the method descriptor format
     * defined in the Java Language Specification.
     * <p>
     * For example, given the following EventSet interface:
     * <pre>
     * <sp>@ControlInterface
     * public interface MyControl
     * {
     *     <sp>@EventSet
     *     public interface MyEvents
     *     {
     *          public String myEvent(int arg0, Object arg2);
     *     }
     * }
     * </pre> 
     * the eventDescriptor for myEvent would be:
     * <pre> 
     *     MyControl.MyEvents.myEvent(ILjava/lang/Object;)Ljava/lang/String;
     * </pre> 
     * @param eventDescriptor the event descriptor string associated with the event 
     */ 
    public EventRef(String eventDescriptor)
    {
        _descriptor = eventDescriptor;
    }

    /**
     * Returns the event descriptor string associated with the EventRef. 
     * @param controlInterface the ControlInterface 
     */
    public String getEventDescriptor(Class controlInterface)
    {
        //
        // NOTE: The input controlInterface is currently unused, but included to
        // enable downstream optimization of serialization representation.  See the
        // OPTIMIZE comment below for more details.  If implemented, the interface
        // is needed to reverse the transformation from a hash back to a method or
        // descriptor.
        //
        if (_descriptor == null)
            _descriptor = computeEventDescriptor(_method);

        return _descriptor;
    }

    /**
     * Helper method that computes the event descriptor sting for a method
     */
    private String computeEventDescriptor(Method method)
    {
        StringBuilder sb = new StringBuilder();

        // Add event class and method name
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());

        // Add event arguments
        Class [] parms = method.getParameterTypes();
        sb.append("(");
        for (int i = 0; i < parms.length; i++)
            appendTypeDescriptor(sb, parms[i]);
        sb.append(")");

        // Add event return type
        appendTypeDescriptor(sb, method.getReturnType());

        return sb.toString();
    }

    /**
     * Helper method that appends a type descriptor to a StringBuilder.  Used
     * while accumulating an event descriptor string.
     */ 
    private void appendTypeDescriptor(StringBuilder sb, Class clazz)
    {
        if (clazz.isPrimitive())
            sb.append(_primToType.get(clazz));
        else if (clazz.isArray())
            sb.append(clazz.getName().replace('.','/'));
        else
        {
            sb.append("L");
            sb.append(clazz.getName().replace('.','/'));
            sb.append(";");
        }
    }
    
    /**
     *  Returns the event Method associated with this EventRef.
     */
    public Method getEventMethod(Class controlInterface)
    {
        //
        // If we already hold a method reference and its loader matches up with the input
        // interface, then just return it.
        //
        if (_method != null &&
            _method.getDeclaringClass().getClassLoader().equals(controlInterface.getClassLoader()))
            return _method;

        //
        // Otherwise, obtain the mapping from descriptors to methods, and use it to
        // convert back to a method.
        //
        String eventDescriptor = getEventDescriptor(controlInterface); 
        HashMap<String,Method> descriptorMap = getDescriptorMap(controlInterface);
        if (!descriptorMap.containsKey(eventDescriptor))
        {
            throw new IllegalArgumentException("Control interface " + controlInterface + 
                                               " does not contain an event method that " +
                                               " corresponds to " + eventDescriptor);
        }
        return descriptorMap.get(eventDescriptor);
    }

    /**
     * A WeakHashMap used to cache the event descriptor-to-Method mapping for control
     * interfaces.
     */
    static private WeakHashMap<Class, HashMap<String,Method>> _descriptorMaps = 
                                    new WeakHashMap<Class, HashMap<String,Method>>();

    private HashMap<String,Method> getDescriptorMap(Class controlInterface)
    {
        //
        // If the local cache has the mapping, then return it.
        //
        HashMap<String,Method> descMap = _descriptorMaps.get(controlInterface);
        if (descMap == null)
        {
            //
            // Compute the mapping from event descriptors to event methods, using reflection
            //
            descMap = new HashMap<String, Method>();
            Class [] innerClasses = controlInterface.getClasses();
            for (int i = 0; i < innerClasses.length; i++)
            {
                if (!innerClasses[i].isInterface() || 
                    !innerClasses[i].isAnnotationPresent(EventSet.class))
                    continue;

                Method [] eventMethods = innerClasses[i].getMethods();
                for (int j = 0; j < eventMethods.length; j++)
                    descMap.put(computeEventDescriptor(eventMethods[j]), eventMethods[j]);
            }
            _descriptorMaps.put(controlInterface, descMap);
        }
        return descMap;
    }


    /**
     * Two EventRefs are equal if the method descriptor string associated with them is equal
     */
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof EventRef))
            return false;

        return _descriptor.equals(((EventRef)obj)._descriptor);
    }

    public String toString()
    {
        return "EventRef: " + _descriptor;
    }

    //
    // OPTIMIZE: A more efficient internal representation for serialization/wire purposes
    // would be to compute a hash of the descriptor string (ala RMI opnums), that could be
    // reconstituted on the other end, given a candidate ControlInterface.  The public APIs
    // are structured to support this downstream optimization.
    //
    private String  _descriptor;
    transient private Method _method;
}
