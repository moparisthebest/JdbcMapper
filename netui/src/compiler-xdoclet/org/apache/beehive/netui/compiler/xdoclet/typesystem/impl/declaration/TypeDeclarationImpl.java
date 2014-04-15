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

import org.apache.beehive.netui.compiler.typesystem.declaration.FieldDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.PackageDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.WrapperFactory;
import org.apache.beehive.netui.compiler.typesystem.type.InterfaceType;

import java.util.Collection;
import java.util.Iterator;

import xjavadoc.XClass;
import xjavadoc.XField;
import xjavadoc.XMethod;

public class TypeDeclarationImpl
        extends MemberDeclarationImpl
        implements TypeDeclaration
{
    private InterfaceType[] _superInterfaces;
    private MethodDeclaration[] _methods;
    private TypeDeclaration[] _nestedTypes;
    private FieldDeclaration[] _fields;
    
    public TypeDeclarationImpl( XClass delegate )
    {
        super( delegate );
    }
    
    public PackageDeclaration getPackage()
    {
        return WrapperFactory.get().getPackageDeclaration( getDelegateXClass().getContainingPackage() );
    }

    public String getQualifiedName()
    {
        return getDelegateXClass().getQualifiedName();
    }

    public InterfaceType[] getSuperinterfaces()
    {
        if ( _superInterfaces == null )
        {
            Collection delegateCollection = getDelegateXClass().getSuperInterfaceElements();
            InterfaceType[] array = new InterfaceType[delegateCollection.size()];
            int j = 0;
            for ( Iterator i = delegateCollection.iterator(); i.hasNext(); )
            {
                array[j++] = WrapperFactory.get().getInterfaceType( ( XClass ) i.next() );
            }
            _superInterfaces = array;
        }

        return _superInterfaces;
    }

    public FieldDeclaration[] getFields()
    {
        if ( _fields == null )
        {
            Collection delegateCollection = getDelegateXClass().getFields();
            FieldDeclaration[] array = new FieldDeclaration[delegateCollection.size()];
            int j = 0;
            for ( Iterator i = delegateCollection.iterator(); i.hasNext(); )
            {
                array[j++] = WrapperFactory.get().getFieldDeclaration( ( XField ) i.next() );
            }
            _fields = array;
        }

        return _fields;
    }

    public MethodDeclaration[] getMethods()
    {
        if ( _methods == null )
        {
            Collection delegateCollection = getDelegateXClass().getMethods();
            MethodDeclaration[] array = new MethodDeclaration[delegateCollection.size()];
            int j = array.length;
            
            // Doing this loop in reverse makes the ordering like what we see in apt/Mirror.
            for ( Iterator i = delegateCollection.iterator(); i.hasNext(); )
            {
                array[--j] = WrapperFactory.get().getMethodDeclaration( ( XMethod ) i.next() );
            }
            _methods = array;
        }

        return _methods;
    }

    public TypeDeclaration[] getNestedTypes()
    {
        if ( _nestedTypes == null )
        {
            Collection delegateCollection = getDelegateXClass().getInnerClasses();
            TypeDeclaration[] array = new TypeDeclaration[delegateCollection.size()];
            int j = 0;
            for ( Iterator i = delegateCollection.iterator(); i.hasNext(); )
            {
                array[j++] = WrapperFactory.get().getTypeDeclaration( ( XClass ) i.next() );
            }
            _nestedTypes = array;
        }

        return _nestedTypes;
    }

    public boolean equals( Object o )
    {
        return super.equals( o ) ||
               ( o instanceof TypeDeclaration && ( ( TypeDeclaration ) o ).getQualifiedName().equals( getQualifiedName() ));
    }

    protected XClass getDelegateXClass()
    {
        return ( XClass ) super.getDelegate();
    }
}
