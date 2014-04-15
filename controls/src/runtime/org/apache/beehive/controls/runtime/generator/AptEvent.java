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

import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.VoidType;

import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptEvent class represents a control Property where the event attributes
 * are derived using APT metadata
 */
public class AptEvent extends AptMethod
{
    /**
     * Constructs a new AptEvent instance from APT metadata
     * @param eventSet the declaring EventSet
     * @param eventDecl the event annotation type element declaration
     */
    public AptEvent(AptEventSet eventSet, MethodDeclaration eventDecl, TwoPhaseAnnotationProcessor ap)
    {
        super(eventDecl, ap);
        _eventSet = eventSet;
        _eventDecl = eventDecl;

        //
        // If the event is in multicast event set but does not return 'void', then generate
        // an error.  Only unicast events can have a return value, to avoid ambiguity over
        // which listener gets to provide the value.
        //
        if (!eventSet.isUnicast() && !(eventDecl.getReturnType() instanceof VoidType))
        {
            ap.printError( eventDecl, "eventset.illegal.multicast" );
        }
    }

    /**
     * Returns the name of the static field that holds the name of this method.
     */
    public String getMethodField()
    {
        //
        // Both the event set and event name must be used for the generated field to avoid
        // conflicts between same-named events in different event sets.
        //
        StringBuffer sb = new StringBuffer();
        sb.append("_");
        sb.append(_eventSet.getShortName());
        sb.append("_");
        sb.append(getName());
        int methodIndex = getIndex();
        if (methodIndex != -1)
            sb.append(methodIndex);
        sb.append("Event");
        return sb.toString();
    }

    /**
     * Returns the EventSet associated with the event
     */
    public AptEventSet getEventSet() { return _eventSet; }

    MethodDeclaration _eventDecl;
    private AptEventSet _eventSet;
}
