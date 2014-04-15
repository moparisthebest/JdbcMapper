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

import org.apache.beehive.netui.compiler.grammar.CommandHandlerGrammar;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;

import java.util.HashMap;
import java.util.Map;


public class FacesBackingChecker
        extends BaseChecker
        implements JpfLanguageConstants
{
    public FacesBackingChecker( CoreAnnotationProcessorEnv env, FacesBackingInfo fbInfo, Diagnostics diags )
    {
        super( env, fbInfo, diags );
    }
    
    public Map onCheck( ClassDeclaration jclass )
            throws FatalCompileTimeException
    {
        if ( ! CompilerUtils.isAssignableFrom( FACES_BACKING_BEAN_CLASS, jclass, getEnv() ))
        {
            getDiagnostics().addError( jclass, "error.does-not-extend-base", FACES_BACKING_BEAN_CLASS );
            return null;
        }
        
        ClassDeclaration[] packageClasses = jclass.getPackage().getClasses();
        ClassDeclaration jpfClass = null;
        
        for ( int i = 0; i < packageClasses.length; i++ )
        {
            ClassDeclaration classDecl = packageClasses[i];
            if ( CompilerUtils.isPageFlowClass( classDecl, getEnv() ) ) jpfClass = classDecl;
        }
        
        // Don't run these checks if there's no associated page flow controller (or if it's in error).
        if (jpfClass != null) {
            FlowControllerInfo fcInfo = new FlowControllerInfo( jpfClass );
            fcInfo.startBuild( getEnv(), jpfClass );
            
            CommandHandlerGrammar chg =
                    new CommandHandlerGrammar( getEnv(), getDiagnostics(), getRuntimeVersionChecker(), jpfClass, fcInfo );
            MethodDeclaration[] methods = CompilerUtils.getClassMethods( jclass, COMMAND_HANDLER_TAG_NAME );
    
            for ( int i = 0; i < methods.length; i++ )
            {
                MethodDeclaration method = methods[i];
                getFBSourceFileInfo().addCommandHandler( method.getSimpleName() );
                chg.check( CompilerUtils.getAnnotation( method, COMMAND_HANDLER_TAG_NAME ), null, method );
            }
            
            Map checkResultMap = new HashMap();
            checkResultMap.put( JpfLanguageConstants.ExtraInfoKeys.facesBackingInfo, getSourceFileInfo() );
            return checkResultMap;
        }
        
        return null;
    }

    protected FacesBackingInfo getFBSourceFileInfo()
    {
        return ( FacesBackingInfo ) super.getSourceFileInfo();
    }
}
