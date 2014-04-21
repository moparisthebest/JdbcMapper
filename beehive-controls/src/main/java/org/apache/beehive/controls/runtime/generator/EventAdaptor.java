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
package org.apache.beehive.controls.runtime.generator;

import java.util.HashMap;

import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.declaration.TypeParameterDeclaration;

/**
 * The EventAdaptor class represents the generated class that is necessary to route
 * events for a EventSet onto implemented EventHandlers on an implementation class.
 */
public class EventAdaptor
{
    /**
     * Constructs a new EventAdaptor for events declared on an EventSet
     */
    public EventAdaptor(AptEventField eventField, AptEventSet eventSet)
    {
        _eventField = eventField;
        _eventSet = eventSet;
        _className = initClassName();
    }

    /**
     * Computes a unique adaptor class name
     */
    private String initClassName()
    {
        StringBuffer sb = new StringBuffer();
        String fieldName = _eventField.getName();
        String setName = _eventSet.getClassName();
        sb.append(Character.toUpperCase(fieldName.charAt(0)));
        if (fieldName.length() > 1)
            sb.append(fieldName.substring(1));
        sb.append(setName.substring(setName.lastIndexOf('.') + 1));
        sb.append("EventAdaptor");
        return sb.toString();
    }

    /**
     * Returns the name of the generated class for this adaptor.
     */
    public String getClassName()
    {
        return _className;
    }

    /**
     * Returns the name of the generated class for this adaptor, including any formal type
     * declarations from the associate event set.
     */
    public String getFormalClassName()
    {
        StringBuffer sb = new StringBuffer(_className);
        sb.append(_eventSet.getFormalTypeParameters());
        return sb.toString();
    }

    /**
     * Returns the event field associated with this event adaptor
     */
    public AptEventField getEventField() { return _eventField; }

    /**
     * Returns the EventSet associated with this Adaptor
     */
    public AptEventSet getEventSet() { return _eventSet; }

    /**
     * Adds a new EventHandler for a Event to the EventAdaptor
     */
    public void addHandler(AptEvent event, AptMethod eventHandler)
    {
        assert event.getEventSet() == _eventSet;
        _handlerMap.put(event, eventHandler);
    }

    /**
     * Returns true if there is an EventHandler for ControlEvent on this EventAdaptor
     */
    public boolean hasHandler(AptEvent event)
    {
        return _handlerMap.containsKey(event);
    }

    /**
     * Returns the EventHandler for a ControlEvent on this EventAdaptor
     */
    public AptMethod getHandler(AptEvent event)
    {
        return _handlerMap.get(event);
    }

    /**
     * Returns any formal type parameter declaration for EventSet interface associated with 
     * the adaptor class.  This will bind the formal types of the interface based on any type 
     * binding from the event field declaration
     */
    public String getEventSetBinding()
    {
        // Get the type bindings for the associated event field.
        HashMap<String,TypeMirror> typeBinding = _eventField.getTypeBindingMap();

        StringBuffer sb = new StringBuffer();
        boolean isFirst = true;
        for (TypeParameterDeclaration tpd : 
             _eventSet.getDeclaration().getFormalTypeParameters())
        {
            if (isFirst)
            {
                sb.append("<");
                isFirst = false;
            }
            else
                sb.append(", ");

            // Map from the declared formal type name to the bound type name
            // If no map entry exists (i.e. not bindings were specified on the field
            // declaration, then the implied binding is to Object.class
            String typeName = tpd.getSimpleName();
            if (typeBinding.containsKey(typeName))
                sb.append(typeBinding.get(tpd.getSimpleName()));
            else
                sb.append("java.lang.Object");
        }
        if (!isFirst)
            sb.append(">");

        return sb.toString();
    }

    private String _className;
    private AptEventField _eventField;
    private AptEventSet _eventSet;
    private HashMap<AptEvent, AptMethod> _handlerMap = new HashMap<AptEvent,AptMethod>();
}
