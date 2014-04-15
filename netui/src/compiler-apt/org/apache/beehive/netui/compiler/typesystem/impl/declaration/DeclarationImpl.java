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
package org.apache.beehive.netui.compiler.typesystem.impl.declaration;

import org.apache.beehive.netui.compiler.typesystem.declaration.Declaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.Modifier;
import org.apache.beehive.netui.compiler.typesystem.util.SourcePosition;
import org.apache.beehive.netui.compiler.typesystem.impl.env.SourcePositionImpl;
import org.apache.beehive.netui.compiler.typesystem.impl.DelegatingImpl;
import org.apache.beehive.netui.compiler.typesystem.impl.WrapperFactory;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

public class DeclarationImpl
        extends DelegatingImpl
        implements Declaration
{
    private AnnotationInstance[] _annotations;
    private Set _modifiers;
    
    public DeclarationImpl( com.sun.mirror.declaration.Declaration delegate )
    {
        super( delegate );
    }

    public String getDocComment()
    {
        return getDelegate().getDocComment();
    }

    public AnnotationInstance[] getAnnotationInstances()
    {
        if ( _annotations == null )
        {
            Collection<com.sun.mirror.declaration.AnnotationMirror> delegateCollection = getDelegate().getAnnotationMirrors();
            AnnotationInstance[] array = new AnnotationInstance[delegateCollection.size()];
            int j = 0;
            for ( com.sun.mirror.declaration.AnnotationMirror i : delegateCollection )
            {
                array[j++] = WrapperFactory.get().getAnnotationInstance( i, this );
            }
            _annotations = array;
        }

        return _annotations;
    }

    public Set getModifiers()
    {
        if ( _modifiers == null )
        {
            Collection<com.sun.mirror.declaration.Modifier> delegateCollection = getDelegate().getModifiers();
            Set modifiers = new HashSet();
            for ( com.sun.mirror.declaration.Modifier i : delegateCollection )
            {
                modifiers.add( ModifierImpl.get( i ) );
            }
            _modifiers = modifiers;
        }

        return _modifiers;
    }

    public String getSimpleName()
    {
        return getDelegate().getSimpleName();
    }

    public SourcePosition getPosition()
    {
        return SourcePositionImpl.get( getDelegate().getPosition() );
    }

    public boolean hasModifier( Modifier modifier )
    {
        return getModifiers().contains( modifier );
    }

    protected com.sun.mirror.declaration.Declaration getDelegate()
    {
        return ( com.sun.mirror.declaration.Declaration ) super.getDelegate();
    }
}
