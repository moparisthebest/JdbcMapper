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

import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.Modifier;

/**
 * The AptField class is a helper class that knows how to generate useful information
 * about a Field using APT metadata
 */
public class AptField
{
    AptField(FieldDeclaration fieldDecl)
    {
        _fieldDecl = fieldDecl;
    }

    /**
     * Returns the name of the method
     */
    public String getName()
    {
        if ( _fieldDecl == null )
            return "";
        return _fieldDecl.getSimpleName();
    }

    /**
     * Returns a local variable used when setting the field value
     */
    public String getLocalName() { return "_" + getName(); }

    /**
     * Returns the type of the field
     */
    public String getType()
    {
        if ( _fieldDecl == null || _fieldDecl.getType() == null )
            return "";
        
        return _fieldDecl.getType().toString();
    }

    /**
     * Returns the class name of the field (does not include any formal type parameters
     */
    public String getClassName()
    {
        if ( _fieldDecl == null || _fieldDecl.getType() == null )
            return "";

        //
        // This is lazily... but much easier than navigating the APT type system and just
        // as effective ;)
        String typeName = _fieldDecl.getType().toString();
        int formalIndex = typeName.indexOf('<');
        if (formalIndex > 0)
            return typeName.substring(0, formalIndex);
        return typeName;
    }

    /**
     * Returns the access modifier associated with the field
     */
    public String getAccessModifier()
    {
        if ( _fieldDecl == null )
            return "";
        
        Collection<Modifier> modifiers = _fieldDecl.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE))
            return "private";
        if (modifiers.contains(Modifier.PROTECTED))
            return "protected";
        if (modifiers.contains(Modifier.PUBLIC))
            return "public";

        return "";
    }

    /**
     * Returns the name of a static local field using to refer to this Field
     */
    public String getReflectField()
    {
        return "_" + getName() + "Field";
    }

    protected FieldDeclaration _fieldDecl;
}
