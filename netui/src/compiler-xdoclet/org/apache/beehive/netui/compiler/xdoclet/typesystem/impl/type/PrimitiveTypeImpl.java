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
package org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.type;

import org.apache.beehive.netui.compiler.typesystem.type.PrimitiveType;

import java.util.HashMap;

import xjavadoc.XClass;

public class PrimitiveTypeImpl
        extends TypeInstanceImpl
        implements PrimitiveType
{
    private static final HashMap PRIMITIVE_TYPES = new HashMap();
    
    static
    {
        PRIMITIVE_TYPES.put( "boolean", Kind.BOOLEAN );
        PRIMITIVE_TYPES.put( "byte", Kind.BYTE );
        PRIMITIVE_TYPES.put( "short", Kind.SHORT );
        PRIMITIVE_TYPES.put( "int", Kind.INT );
        PRIMITIVE_TYPES.put( "long", Kind.LONG );
        PRIMITIVE_TYPES.put( "char", Kind.CHAR );
        PRIMITIVE_TYPES.put( "float", Kind.FLOAT );
        PRIMITIVE_TYPES.put( "double", Kind.DOUBLE );
    }
    
    private Kind _kind;
    
    public PrimitiveTypeImpl( XClass delegate )
    {
        super( delegate );
        _kind = ( Kind ) PRIMITIVE_TYPES.get( delegate.getName() );
        assert _kind != null : "unexpected type " + delegate.getName();
    }
    
    public Kind getKind()
    {
        return _kind;
    }
}
