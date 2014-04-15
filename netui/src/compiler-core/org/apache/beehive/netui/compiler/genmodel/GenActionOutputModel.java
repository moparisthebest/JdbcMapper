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
package org.apache.beehive.netui.compiler.genmodel;

import org.apache.beehive.netui.compiler.model.ActionOutputModel;
import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.type.ArrayType;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;
import org.apache.beehive.netui.compiler.typesystem.type.DeclaredType;
import org.apache.beehive.netui.compiler.typesystem.type.PrimitiveType;


public class GenActionOutputModel
        extends ActionOutputModel
        implements JpfLanguageConstants
{
    public GenActionOutputModel( AnnotationInstance annotation, ClassDeclaration jclass )
    {
        setName( CompilerUtils.getString( annotation, NAME_ATTR, true ) );
        
        Boolean required = CompilerUtils.getBoolean( annotation, REQUIRED_ATTR, false );
        boolean nullable = ! required.booleanValue();
        setNullable( nullable );
        
        //
        // Get the base type, and add "[]" to it for arrays.
        //
        TypeInstance baseType = CompilerUtils.getReferenceType( annotation, TYPE_ATTR, true );
        StringBuffer arrayDimensions = new StringBuffer();
        while ( baseType instanceof ArrayType )
        {
            arrayDimensions.append( ARRAY_TYPE_SUFFIX );
            baseType = ( ( ArrayType ) baseType ).getComponentType();
        }
        
        String baseTypeName;
        if ( baseType instanceof PrimitiveType )
        {
            baseTypeName = ( ( PrimitiveType ) baseType ).getKind().toString().toLowerCase();
        }
        else
        {
            assert baseType instanceof DeclaredType : baseType.getClass().getName();   // checker should enforce this
            baseTypeName = CompilerUtils.getLoadableName( ( DeclaredType ) baseType );
        }
        
        setType( baseTypeName + arrayDimensions.toString() );
    }
}
