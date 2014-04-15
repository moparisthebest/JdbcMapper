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
package org.apache.beehive.netui.compiler.processor;

import org.apache.beehive.netui.compiler.BaseChecker;
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.FormBeanChecker;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.BaseGenerator;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;


public class FormBeanCoreAnnotationProcessor
        extends BaseCoreAnnotationProcessor
{
    public FormBeanCoreAnnotationProcessor( AnnotationTypeDeclaration[] annotationTypeDecls, CoreAnnotationProcessorEnv env )
    {
        super( annotationTypeDecls, env );
    }
    
    protected BaseChecker getChecker( ClassDeclaration decl, Diagnostics diagnostics )
    {
        if ( CompilerUtils.getAnnotation( decl, FORM_BEAN_TAG_NAME, true ) != null )
        {
            return new FormBeanChecker( getAnnotationProcessorEnvironment(), diagnostics );
        }
        
        return null;
    }
    
    protected BaseGenerator getGenerator( ClassDeclaration decl, Diagnostics diagnostics )
    {
        return null;
    }
}
