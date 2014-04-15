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
package org.apache.beehive.netui.compiler.typesystem.env;

import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.PackageDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Declaration;
import org.apache.beehive.netui.compiler.Diagnostics;

import java.util.Map;
import java.util.Collection;

/**
 * Annotation processor environment.  "Core" to distinguish from Sun's AnnotationProcessorEnvironment.
 */
public interface CoreAnnotationProcessorEnv
{
    /** Map of String -> String */
    Map getOptions();

    Messager getMessager();

    Filer getFiler();

    TypeDeclaration[] getSpecifiedTypeDeclarations();

    TypeDeclaration getTypeDeclaration( String s );

    Declaration[] getDeclarationsAnnotatedWith( AnnotationTypeDeclaration annotationTypeDeclaration );
    
    void setAttribute( String propertyName, Object value );
    
    Object getAttribute( String propertyName );
}
