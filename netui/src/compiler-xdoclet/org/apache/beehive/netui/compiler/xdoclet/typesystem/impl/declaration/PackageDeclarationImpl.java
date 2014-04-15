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

import org.apache.beehive.netui.compiler.typesystem.declaration.PackageDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.WrapperFactory;
import org.apache.beehive.netui.compiler.xdoclet.typesystem.impl.DelegatingImpl;

import java.util.Collection;
import java.util.Iterator;

import xjavadoc.XPackage;
import xjavadoc.XClass;

public class PackageDeclarationImpl
        extends DelegatingImpl
        implements PackageDeclaration
{
    private ClassDeclaration[] _classes;
    
    public PackageDeclarationImpl( XPackage delegate )
    {
        super( delegate );
    }

    public String getQualifiedName()
    {
        return getDelegateXPackage().getName();
    }

    public ClassDeclaration[] getClasses()
    {
        if ( _classes == null )
        {
            Collection delegateCollection = getDelegateXPackage().getClasses();
            ClassDeclaration[] array = new ClassDeclaration[delegateCollection.size()];
            int j = 0;
            for ( Iterator i = delegateCollection.iterator(); i.hasNext(); )
            {
                array[j++] = WrapperFactory.get().getClassDeclaration( ( XClass ) i.next() );
            }
            _classes = array;
        }

        return _classes;
    }
    
    public XPackage getDelegateXPackage()
    {
        return ( XPackage ) super.getDelegate();
    }
}
