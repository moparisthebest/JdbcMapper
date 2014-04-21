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
package org.apache.beehive.netui.compiler;

import org.apache.beehive.netui.compiler.genmodel.GenSharedFlowStrutsApp;
import org.apache.beehive.netui.compiler.genmodel.GenStrutsApp;
import org.apache.beehive.netui.compiler.grammar.ControllerGrammar;
import org.apache.beehive.netui.compiler.grammar.InvalidAttributeType;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;

import java.io.File;
import java.io.IOException;


public class SharedFlowChecker
        extends FlowControllerChecker
{
    public SharedFlowChecker( CoreAnnotationProcessorEnv env, FlowControllerInfo fcInfo, Diagnostics diagnostics )
    {
        super( env, fcInfo, diagnostics );
    }

    protected void doAdditionalClassChecks( ClassDeclaration jclass )
    {
        // Make sure there are no other shared flows in this package/directory.
        checkForOverlappingClasses( jclass, SHARED_FLOW_BASE_CLASS, SHARED_FLOW_FILE_EXTENSION_DOT,
                                    "error.overlapping-sharedflows" );
        
        String pkg = jclass.getPackage().getQualifiedName();              
        
        //
        // If it's Global.app, make sure the package is "global".
        //
        if ( CompilerUtils.isAssignableFrom( GLOBALAPP_BASE_CLASS, jclass, getEnv() ) )
        {
            if ( ! pkg.equals( GLOBALAPP_PACKAGE ) )
            {
                getDiagnostics().addError( jclass, "error.wrong-package", GLOBALAPP_PACKAGE );
            }
            
            if ( ! jclass.getPosition().file().getParentFile().getName().endsWith( GLOBALAPP_PACKAGE ) )
            {
                getDiagnostics().addError( jclass, "error.global-app-wrong-dir",
                                           GLOBALAPP_SOURCE_NAME, GLOBALAPP_PARENT_PATH );
            }
        }
    }
    
    protected String getDesiredBaseClass( ClassDeclaration jclass )
    {
        File sourceFile = CompilerUtils.getSourceFile( jclass, true );
        if ( sourceFile.getName().endsWith( GLOBALAPP_FILE_EXTENSION_DOT ) ) return GLOBALAPP_BASE_CLASS;
        if ( sourceFile.getName().endsWith( SHARED_FLOW_FILE_EXTENSION_DOT ) ) return SHARED_FLOW_BASE_CLASS;
        return null;
    }

    protected GenStrutsApp createStrutsApp( ClassDeclaration jclass )
        throws IOException, FatalCompileTimeException
    {
        File sourceFile = CompilerUtils.getSourceFile( jclass, true );
        return new GenSharedFlowStrutsApp( sourceFile, jclass, getEnv(), getFCSourceFileInfo(), true, getDiagnostics() );
    }

    protected AnnotationGrammar getControllerGrammar()
    {
        return new SharedFlowControllerGrammar();
    }    
       
    private class SharedFlowControllerGrammar
        extends ControllerGrammar
    {
        public SharedFlowControllerGrammar()
        {
            super( SharedFlowChecker.this.getEnv(), SharedFlowChecker.this.getDiagnostics(),
                   SharedFlowChecker.this.getRuntimeVersionChecker(), SharedFlowChecker.this.getFCSourceFileInfo() );
            InvalidAttributeType type = new InvalidAttributeType( null, this, "error.only-valid-on-pageflow",
                                                                  new Object[]{ NESTED_ATTR, JPF_BASE_CLASS } );
            addMemberType( NESTED_ATTR, type );
            type = new InvalidAttributeType( null, this, "error.only-valid-on-pageflow",
                                             new Object[]{ LONGLIVED_ATTR, JPF_BASE_CLASS } );
            addMemberType( LONGLIVED_ATTR, type );
        }
    } 
}
