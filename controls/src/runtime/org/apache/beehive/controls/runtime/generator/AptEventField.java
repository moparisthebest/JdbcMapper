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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;

/**
 * The AptEventField class represents a field type that is also an event source
 */
abstract public class AptEventField extends AptField
{
    public AptEventField(FieldDeclaration fieldDecl)
    {
        super(fieldDecl);
    }

    /**
     * Inits the ControlInterface associated with this event field. The public interface
     * for controls and contextual services, and their associated events can be modeled in the
     * same way.  Subclasses will override this to assign an appropriate interface.
     */
    abstract protected AptControlInterface initControlInterface();

    /**
     * Computes the binding from any formal type parameters declared on the control interface
     * to bound types on the field declaration.
     */
    private void initTypeParameterBindings()
    {
        //
        // Get an iterator to both the declared type arguments and the original type
        // declaration on the associated control interface
        //
        DeclaredType fieldType = (DeclaredType)_fieldDecl.getType();
        Iterator<TypeMirror> paramBoundIter = fieldType.getActualTypeArguments().iterator();

        TypeDeclaration intfDecl = (TypeDeclaration)_controlIntf.getTypeDeclaration();
        Iterator<TypeParameterDeclaration> paramDeclIter = 
                                            intfDecl.getFormalTypeParameters().iterator();

        //
        // Iterate through them in parallel, creating a mapping from the original formal
        // type parameter name to the actual bound type.  In parallel, also build up a
        // representation of the bound type declaration.
        //
        // NOTE: If no type binding is done on the field declaration, then loop below
        // will not execute and no mappings/empty bound decl will be the result.
        //
        StringBuffer sb = new StringBuffer();
        boolean isFirst = true;
        while (paramBoundIter.hasNext())
        {
            TypeMirror paramBound = paramBoundIter.next();
            TypeParameterDeclaration paramDecl = paramDeclIter.next();

            //
            // Save a mapping from the formal type name to the bound mirror type
            //
            _typeBindingMap.put(paramDecl.getSimpleName(), paramBound);

            if (isFirst)
            {
                sb.append("<");
                isFirst = false;
            }
            else
                sb.append(", ");
            sb.append(paramBound);
        }
        if (!isFirst)
            sb.append(">");
        
        _boundParameterDecl = sb.toString();
    }

    /**
     * Returns the ControlInterface associated with this event field
     */
    public AptControlInterface getControlInterface()
    {
        if (_controlIntf == null)
        {
            _controlIntf = initControlInterface();
            if (_controlIntf != null)
                initTypeParameterBindings();
        }
        return _controlIntf;
    }

    /**
     * Gets the EventAdaptor for a particular EventSet
     */
    public EventAdaptor getEventAdaptor(AptEventSet eventSet)
    {
        return _eventAdaptors.get(eventSet);
    }

    /**
     * Adds a EventAdaptor for a particular EventSet
     */
    public void addEventAdaptor(AptEventSet eventSet, EventAdaptor eventAdaptor)
    {
        assert !_eventAdaptors.containsKey(eventSet);
        _eventAdaptors.put(eventSet, eventAdaptor);
    }

    /**
     *  Returns all EventAdaptors for this EventField
     */
    public Collection<EventAdaptor> getEventAdaptors()
    {
        return _eventAdaptors.values();
    }

    /**
     * Returns the bound parameter declaration for this event field
     */
    public String getBoundParameters()
    {
        return _boundParameterDecl;
    }

    /**
     * Returns the formal type binding map (from name to bound type) for the event field
     */
    public HashMap<String, TypeMirror> getTypeBindingMap()
    {
        return _typeBindingMap;
    }

    HashMap<AptEventSet, EventAdaptor> _eventAdaptors = 
        new HashMap<AptEventSet, EventAdaptor>();

    String _boundParameterDecl;
    HashMap<String,TypeMirror> _typeBindingMap = new HashMap<String,TypeMirror>();
    private AptControlInterface _controlIntf;
}
