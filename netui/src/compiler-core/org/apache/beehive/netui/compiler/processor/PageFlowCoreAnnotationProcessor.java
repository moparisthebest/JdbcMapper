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

import org.apache.beehive.netui.compiler.*;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Declaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;


public class PageFlowCoreAnnotationProcessor
        extends BaseCoreAnnotationProcessor
        implements JpfLanguageConstants
{
    public PageFlowCoreAnnotationProcessor( AnnotationTypeDeclaration[] annotationTypeDecls,
                                            CoreAnnotationProcessorEnv env )
    {
        super( annotationTypeDecls, env );
    }

    /*
     * Run the checkers on classes including form beans with declarative
     * validation annotations. This override will find class declarations
     * for classes that may only have method level declarative validation
     * annotations.
     */
    protected void check(Collection decls)
        throws FatalCompileTimeException
    {
        ArrayList classDecls = new ArrayList();
        HashSet classNames = new HashSet();
        for (Iterator i = decls.iterator(); i.hasNext();) {
            Declaration decl = (Declaration) i.next();
            if (decl instanceof ClassDeclaration) {
                ClassDeclaration jclass = (ClassDeclaration) decl;
                if (!classNames.contains(jclass.getQualifiedName())) {
                    classNames.add(jclass.getQualifiedName());
                    classDecls.add(jclass);
                }
            }
            else if (decl instanceof MethodDeclaration) {
                //
                // Declarative validation annotations on a method. Include as a
                // possible form bean that does not have the @FormBean annotation.
                //
                MethodDeclaration methodDecl = (MethodDeclaration) decl;
                TypeDeclaration type = methodDecl.getDeclaringType();
                if (type instanceof ClassDeclaration) {
                    ClassDeclaration jclass = (ClassDeclaration) type;
                    if (!classNames.contains(jclass.getQualifiedName())) {
                        classNames.add(jclass.getQualifiedName());
                        classDecls.add(jclass);
                    }
                }
            }
        }

        super.check(classDecls);
    }

    public BaseChecker getChecker( ClassDeclaration classDecl, Diagnostics diagnostics )
    {
        CoreAnnotationProcessorEnv env = getAnnotationProcessorEnvironment();

        if ( CompilerUtils.isAssignableFrom( JPF_BASE_CLASS, classDecl, env ) )
        {
            if ( expectAnnotation( classDecl, CONTROLLER_TAG_NAME, JPF_FILE_EXTENSION_DOT, JPF_BASE_CLASS, diagnostics ) )
            {
                FlowControllerInfo fcInfo = new FlowControllerInfo( classDecl );
                setSourceFileInfo( classDecl, fcInfo );

                return new PageFlowChecker( env, diagnostics, fcInfo );
            }
        }
        else if ( CompilerUtils.isAssignableFrom( SHARED_FLOW_BASE_CLASS, classDecl, env ) )
        {
            if ( expectAnnotation( classDecl, CONTROLLER_TAG_NAME, SHARED_FLOW_FILE_EXTENSION_DOT,
                                   SHARED_FLOW_BASE_CLASS, diagnostics ) )
            {
                FlowControllerInfo fcInfo = new FlowControllerInfo( classDecl );
                setSourceFileInfo( classDecl, fcInfo );

                return new SharedFlowChecker( env, fcInfo, diagnostics );
            }
        }
        else if ( CompilerUtils.isAssignableFrom( GLOBALAPP_BASE_CLASS, classDecl, env ) )
        {
            if ( expectAnnotation( classDecl, CONTROLLER_TAG_NAME, GLOBALAPP_FILE_EXTENSION_DOT, GLOBALAPP_BASE_CLASS,
                                   diagnostics ) )
            {
                FlowControllerInfo fcInfo = new FlowControllerInfo( classDecl );
                setSourceFileInfo( classDecl, fcInfo );

                return new SharedFlowChecker( env, fcInfo, diagnostics );
            }
        }
        else if ( CompilerUtils.isAssignableFrom( FACES_BACKING_BEAN_CLASS, classDecl, env ) )
        {
            if ( expectAnnotation( classDecl, FACES_BACKING_TAG_NAME, JPF_FILE_EXTENSION_DOT, JPF_BASE_CLASS, diagnostics ) )
            {
                File originalFile = CompilerUtils.getSourceFile( classDecl, true );
                FacesBackingInfo fbInfo = new FacesBackingInfo( originalFile, classDecl.getQualifiedName() );
                setSourceFileInfo( classDecl, fbInfo );
                return new FacesBackingChecker( env, fbInfo, diagnostics );
            }
        }
        else
        {
            AnnotationInstance ann = CompilerUtils.getAnnotation( classDecl, CONTROLLER_TAG_NAME );

            if ( ann != null )
            {
                diagnostics.addError( ann, "error.annotation-invalid-base-class2",
                                      CONTROLLER_TAG_NAME, JPF_BASE_CLASS, SHARED_FLOW_BASE_CLASS );
            }

            ann = CompilerUtils.getAnnotation( classDecl, FACES_BACKING_TAG_NAME );

            if ( ann != null )
            {
                diagnostics.addError( ann, "error.annotation-invalid-base-class",
                                      FACES_BACKING_TAG_NAME, FACES_BACKING_BEAN_CLASS );
            }
        }

        // check the class as a form bean for declarative validation annotations
        return new FormBeanChecker(getAnnotationProcessorEnvironment(), diagnostics);
    }

    public BaseGenerator getGenerator( ClassDeclaration classDecl, Diagnostics diags )
    {
        CoreAnnotationProcessorEnv env = getAnnotationProcessorEnvironment();
        SourceFileInfo sourceFileInfo = getSourceFileInfo( classDecl );

        if ( CompilerUtils.isAssignableFrom( JPF_BASE_CLASS, classDecl, env ) )
        {
            assert sourceFileInfo != null : classDecl.getQualifiedName();
            assert sourceFileInfo instanceof FlowControllerInfo : sourceFileInfo.getClass().getName();
            return new PageFlowGenerator( env, ( FlowControllerInfo ) sourceFileInfo, diags );
        }
        else if ( CompilerUtils.isAssignableFrom( SHARED_FLOW_BASE_CLASS, classDecl, env ) )
        {
            assert sourceFileInfo != null : classDecl.getQualifiedName();
            assert sourceFileInfo instanceof FlowControllerInfo : sourceFileInfo.getClass().getName();
            return new SharedFlowGenerator( env, ( FlowControllerInfo ) sourceFileInfo, diags );
        }
        else if ( CompilerUtils.isAssignableFrom( FACES_BACKING_BEAN_CLASS, classDecl, env ) )
        {
            assert sourceFileInfo != null : classDecl.getQualifiedName();
            assert sourceFileInfo instanceof FacesBackingInfo : sourceFileInfo.getClass().getName();
            return new FacesBackingGenerator( env, sourceFileInfo, diags );
        }

        return null;
    }
}
