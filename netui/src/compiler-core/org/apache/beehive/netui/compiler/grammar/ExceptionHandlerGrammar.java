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
package org.apache.beehive.netui.compiler.grammar;

import org.apache.beehive.netui.compiler.AnnotationMemberType;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.FlowControllerInfo;
import org.apache.beehive.netui.compiler.RuntimeVersionChecker;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.Modifier;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;


public class ExceptionHandlerGrammar
        extends BaseFlowControllerGrammar
{
    public ExceptionHandlerGrammar( CoreAnnotationProcessorEnv env, Diagnostics diags,
                                    RuntimeVersionChecker runtimeVersionChecker, FlowControllerInfo fcInfo )
    {
        super( env, diags, null, runtimeVersionChecker, fcInfo );
        
        addMemberType( READONLY_ATTR, new AnnotationMemberType( VERSION_8_SP2_STRING, this ) );
        addMemberArrayGrammar( FORWARDS_ATTR, new ExceptionHandlerForwardGrammar( fcInfo ) );
    }

    protected boolean onBeginCheck( AnnotationInstance annotation, AnnotationInstance[] parentAnnotations,
                                    MemberDeclaration classMember )
            throws FatalCompileTimeException
    {
        if ( classMember.hasModifier( Modifier.ABSTRACT ) )
        {
            addWarning( annotation, "warning.annotated-abstract-method", null );
            return false;
        }
        
        TypeDeclaration outerType = CompilerUtils.getOuterClass( classMember );
        String thisMethodName = classMember.getSimpleName();
        MethodDeclaration[] classMethods = CompilerUtils.getClassMethods( outerType, EXCEPTION_HANDLER_TAG_NAME );
        
        for ( int i = 0; i < classMethods.length; i++ )
        {
            MethodDeclaration method = classMethods[i];
            
            if ( ! method.equals( classMember ) && method.getSimpleName().equals( thisMethodName ) )
            {
                addError( annotation, "error.duplicate-exception-handler" );
            }
        }

        return super.onBeginCheck( annotation, parentAnnotations, classMember );
    }
    
    private class ExceptionHandlerForwardGrammar
            extends ForwardGrammar
    {
        public ExceptionHandlerForwardGrammar( FlowControllerInfo fcInfo )
        {
            super( ExceptionHandlerGrammar.this.getEnv(), ExceptionHandlerGrammar.this.getDiagnostics(),
                   ExceptionHandlerGrammar.this.getRequiredRuntimeVersion(),
                   ExceptionHandlerGrammar.this.getRuntimeVersionChecker(), fcInfo );
            ExternalPathOrActionType baseType = new ExternalPathOrActionType( false, null, this, fcInfo );
            addMemberType( PATH_ATTR, new ForwardToExternalPathType( baseType, null, ExceptionHandlerGrammar.this ) );
        }
    
        protected AnnotationMemberType getNameType()
        {
            return new UniqueValueType( FORWARDS_ATTR, false, false, null, this );
        }
    }
    
}
