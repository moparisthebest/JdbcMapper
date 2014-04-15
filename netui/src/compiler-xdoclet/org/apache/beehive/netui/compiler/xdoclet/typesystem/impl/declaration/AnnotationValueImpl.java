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

import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.util.SourcePosition;
import org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.DelegatingImpl;
import org.apache.beehive.netui.xdoclet.XDocletCompilerUtils;

import java.util.Iterator;

public class AnnotationValueImpl
        extends DelegatingImpl
        implements AnnotationValue
{
    private SourcePosition _position;
    
    public AnnotationValueImpl( Object value, SourcePosition position, AnnotationTypeElementDeclaration decl )
    {
        super( value );
        _position = position;
        
        if ( decl != null )
        {
            assert decl instanceof AnnotationTypeElementDeclarationImpl : decl.getClass().getName();
            AnnotationTypeElementDeclarationImpl declImpl = ( AnnotationTypeElementDeclarationImpl ) decl;
            
            if ( ! declImpl.isValidValue( value ) )
            {
                StringBuffer values = new StringBuffer();
                for ( Iterator i = declImpl.getValidValues().iterator(); i.hasNext(); )
                {
                    String validValue = ( String ) i.next();
                    values.append( ' ' ).append( validValue );
                }
                
                XDocletCompilerUtils.addError( position, "error.invalid-enum-value",
                                               new String[]{ value.toString(), decl.getSimpleName(), values.toString() } );
            }
        }
    }

    public Object getValue()
    {
        return getDelegate();
    }

    public SourcePosition getPosition()
    {
        return _position;
    }

    public boolean equals( Object o )
    {
        return this == o;
    }
}
