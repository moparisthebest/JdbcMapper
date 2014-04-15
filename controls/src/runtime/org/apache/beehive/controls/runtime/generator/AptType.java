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

import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;

/**
 * The AptType abstract class defines a base set of methods that are generally available
 * for template usage on type declaration objects
 */
public class AptType
{
    /**
     * Sets the TypeDeclaration associated with this AptType.
     */
    protected void setDeclaration(TypeDeclaration typeDecl)
    {
        _typeDecl = typeDecl;
    }

    /**
     * Checks a MethodDeclaration for a 'private' modifier.
     *
     * @param md MethodDeclaration to check.
     * @return true if private modifier is present.
     */
    protected boolean isPrivateMethod(MethodDeclaration md)
    {
        Collection<Modifier> modifiers = md.getModifiers();
        for (Modifier m : modifiers)
        {
            if (m.compareTo(Modifier.PRIVATE) == 0)
                return true;
        }
        return false;
    }

    /**
     * Returns the fully qualified classname of this AptType
     */
    public String getClassName()
    {
        if ( _typeDecl == null)
            return "";

        return _typeDecl.getQualifiedName();
    }

    /**
     * Returns the base package name associated with the AptType
     */
    public String getPackage()
    {
        if ( _typeDecl == null)
            return "";

        return _typeDecl.getPackage().getQualifiedName();
    }

    /**
     * Returns the unqualified class name associated with the AptType
     */
    public String getShortName()
    {
        if ( _typeDecl == null )
            return "";
        
        return _typeDecl.getSimpleName();
    }

    /**
     * Helper method to return type parameter information
     */
    private String getFormalTypeParameters(boolean namesOnly)
    {
        Collection<TypeParameterDeclaration> ftColl = _typeDecl.getFormalTypeParameters();
        if (ftColl.size() == 0)
            return ""; 

        StringBuffer sb = new StringBuffer("<");
        boolean isFirst = true;
        for (TypeParameterDeclaration tpDecl : ftColl)
        {
            if (!isFirst)
                sb.append(",");
            else
                isFirst = false;
        
            if (namesOnly)
                sb.append(tpDecl.getSimpleName());
            else
                sb.append(tpDecl.toString());
        }
        sb.append(">");
        return sb.toString();
    }

    /**
     * Returns the full formal type parameter declaration associated with the type declaration
     */
    public String getFormalTypeParameters()
    {
        return getFormalTypeParameters(false);
    }

    /**
     * Returns the name of any formal type parameter names associated with the type declaration.
     */
    public String getFormalTypeParameterNames()
    {
        return getFormalTypeParameters(true);
    }

    /**
     * Returns the short name and the names of any formal type parameters associated with
     * the type.  The format is suitable for use in location (such as variable declarations
     * or extends clauses) where you want formal type parameters listed.
     */
    public String getFormalShortName()
    {
        StringBuffer sb = new StringBuffer(getShortName());
        sb.append(getFormalTypeParameterNames());
        return sb.toString();
    }

    /**
     * Returns the class name and the names of any formal type parameters associated with
     * the type.  The format is suitable for use in location (such as variable declarations
     * or extends clauses) where you want formal type parameters listed.
     */
    public String getFormalClassName()
    {
        StringBuffer sb = new StringBuffer(getClassName());
        sb.append(getFormalTypeParameterNames());
        return sb.toString();
    }

    /**
     * Returns the underlying type declaration name
     */
    public TypeDeclaration getTypeDeclaration()
    {
        return _typeDecl;
    }

    TypeDeclaration _typeDecl;
}
