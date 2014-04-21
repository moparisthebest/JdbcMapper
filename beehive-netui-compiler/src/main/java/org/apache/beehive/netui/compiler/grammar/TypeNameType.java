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
package org.apache.beehive.netui.compiler.grammar;

import org.apache.beehive.netui.compiler.AnnotationMemberType;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.AnnotationGrammar;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.type.ArrayType;
import org.apache.beehive.netui.compiler.typesystem.type.ReferenceType;
import org.apache.beehive.netui.compiler.typesystem.type.PrimitiveType;
import org.apache.beehive.netui.compiler.typesystem.type.VoidType;

public class TypeNameType
        extends AnnotationMemberType
{
    private String _requiredSuperclassName;
    private boolean _allowArrayType;


    public TypeNameType( String requiredSuperclassName, boolean allowArrayType, String requiredRuntimeVersion,
                         AnnotationGrammar parentGrammar )
    {
        super( requiredRuntimeVersion, parentGrammar );
        _requiredSuperclassName = requiredSuperclassName;
        _allowArrayType = allowArrayType;
    }

    /**
     * @return the fully-qualified type (ClassDeclaration)
     */ 
    
    public Object onCheck( AnnotationTypeElementDeclaration valueDecl, AnnotationValue value,
                           AnnotationInstance[] parentAnnotations, MemberDeclaration classMember,
                           int annotationArrayIndex )
    {
        Object val = value.getValue();
        
        if ( CompilerUtils.isErrorString( val ) )
        {
            // This means that there is already an error related to the type itself.
            return null;
        }
        
        if ( val instanceof PrimitiveType )
        {
            addError( value, "error.primitive-type-not-allowed" );
            return null;
        }
        else if ( val instanceof VoidType )
        {
            addError( value, "error.void-type-not-allowed" );
            return null;
        }
        
        assert val instanceof ReferenceType : val.getClass().getName();
        ReferenceType type = ( ReferenceType ) val;
        
        if ( ! _allowArrayType && type instanceof ArrayType )
        {
            addError( value, "error.array-type-not-allowed" );
            return null;
        }
        
        if ( _requiredSuperclassName != null )
        {
            if ( ! CompilerUtils.isAssignableFrom( _requiredSuperclassName, type, getEnv() ) )
            {
                addError( value, "error.does-not-extend-base", new Object[]{ _requiredSuperclassName } );
                return null;
            }                
        }
        
        checkType( type, value );
        return type;
    }

    /**
     * Derived classes can plug in here to do further checks on the type.
     */
    protected void checkType( ReferenceType type, AnnotationValue member )
    {
    }
}
