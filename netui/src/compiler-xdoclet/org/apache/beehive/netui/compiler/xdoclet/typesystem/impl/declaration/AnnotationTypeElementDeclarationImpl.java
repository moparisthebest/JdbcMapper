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
package org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.declaration;

import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.Modifier;
import org.apache.beehive.netui.compiler.typesystem.declaration.ParameterDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;
import org.apache.beehive.netui.compiler.typesystem.util.SourcePosition;
import org.apache.beehive.netui.xdoclet.XDocletCompilerUtils;

import java.util.Set;
import java.util.HashSet;

public class AnnotationTypeElementDeclarationImpl
        implements AnnotationTypeElementDeclaration
{
    private String _name;
    private TypeInstance _type;
    private String _typeName;
    private AnnotationValue _defaultVal;
    private HashSet _validValues;
    
    public AnnotationTypeElementDeclarationImpl( String name, String typeName, Object defaultVal, HashSet validValues )
    {
        _name = name;
        _typeName = typeName;
        if ( defaultVal != null ) _defaultVal = new AnnotationValueImpl( defaultVal, null, this );
        _validValues = validValues;
    }

    public boolean isValidValue( Object value )
    {
        return _validValues == null || _validValues.contains( value );
    }
    
    public Set getValidValues()
    {
        return _validValues;
    }
    
    public AnnotationValue getDefaultValue()
    {
        return _defaultVal;
    }

    public TypeInstance getReturnType()
    {
        if ( _typeName != null )
        {
            _type = XDocletCompilerUtils.resolveType( _typeName, false, null );
            assert _type != null : "unresolvable type " + _typeName;
            _typeName = null;
        }
        
        return _type;
    }

    public ParameterDeclaration[] getParameters()
    {
        assert false : "NYI";
        throw new UnsupportedOperationException( "NYI" );
    }

    public TypeDeclaration getDeclaringType()
    {
        assert false : "NYI";
        throw new UnsupportedOperationException( "NYI" );
    }

    public AnnotationInstance[] getAnnotationInstances()
    {
        assert false : "NYI";
        throw new UnsupportedOperationException( "NYI" );
    }

    public Set getModifiers()
    {
        assert false : "NYI";
        throw new UnsupportedOperationException( "NYI" );
    }

    public String getSimpleName()
    {
        return _name;
    }

    public SourcePosition getPosition()
    {
        assert false : "NYI";
        throw new UnsupportedOperationException( "NYI" );
    }

    public boolean hasModifier( Modifier modifier )
    {
        assert false : "NYI";
        throw new UnsupportedOperationException( "NYI" );
    }
}
