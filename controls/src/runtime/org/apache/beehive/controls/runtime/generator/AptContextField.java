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

import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptContextField class contains information about a field referring to a contextual
 * service with an AptControlImplementation class.
 */
public class AptContextField extends AptEventField
{
    /**
     * Base constructor, protected so only a custom subclass can invoke
     * @param controlImpl the declaring ControlImplementation
     */
    public AptContextField(AptControlImplementation controlImpl, FieldDeclaration fieldDecl, 
                           TwoPhaseAnnotationProcessor ap)
    {
        super(fieldDecl);
        _controlImpl = controlImpl;
        _ap = ap;
    };

    /**
     * Initializes a ControlInterface associated with this context field.  Because
     * contextual services can expose both APIs and events, they are similar to controls.
     */
    protected AptControlInterface initControlInterface()
    {
        TypeMirror fieldType = _fieldDecl.getType();        
        if (! (fieldType instanceof InterfaceType))
        {
            _ap.printError( _fieldDecl, "context.field.badinterface" );
            return null;
        }

        //
        // For contextual services, the declared type of the field is always the public
        // interface for the contextual service.
        //
        return new AptControlInterface(((InterfaceType)_fieldDecl.getType()).getDeclaration(), 
                                       _ap);
    }

    private AptControlImplementation _controlImpl;
    private TwoPhaseAnnotationProcessor _ap;
}
