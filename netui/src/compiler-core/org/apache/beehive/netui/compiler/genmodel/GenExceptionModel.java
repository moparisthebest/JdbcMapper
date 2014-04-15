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
package org.apache.beehive.netui.compiler.genmodel;

import org.apache.beehive.netui.compiler.model.ExceptionModel;
import org.apache.beehive.netui.compiler.model.ForwardContainer;
import org.apache.beehive.netui.compiler.model.ExceptionContainer;
import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;

import java.util.Collection;
import java.util.Iterator;


public class GenExceptionModel
        extends ExceptionModel
        implements JpfLanguageConstants
{
    public GenExceptionModel( GenStrutsApp parentApp, AnnotationInstance annotation, ClassDeclaration jclass,
                              ForwardContainer forwardContainer )
    {
        super( parentApp );
        
        setType( CompilerUtils.getLoadableName( CompilerUtils.getDeclaredType( annotation, TYPE_ATTR, true ) ) );
        setMessage( CompilerUtils.getString( annotation, MESSAGE_ATTR, true ) );
        setMessageKey( CompilerUtils.getString( annotation, MESSAGE_KEY_ATTR, true ) );
        String path = CompilerUtils.getString( annotation, PATH_ATTR, true );
        String methodName = CompilerUtils.getString( annotation, METHOD_ATTR, true );
        
        //
        // Now get the forwards (@Jpf.Forward) from the handler method, and add them as global or local
        // forwards, as appropriate.
        //
        if ( methodName != null )
        {
            setHandlerMethod( methodName );
            MethodDeclaration method = CompilerUtils.getClassMethod( jclass, methodName, EXCEPTION_HANDLER_TAG_NAME );
            AnnotationInstance exHandlerAnnotation = CompilerUtils.getAnnotation( method, EXCEPTION_HANDLER_TAG_NAME );
            GenForwardModel.addForwards( exHandlerAnnotation, forwardContainer, jclass, parentApp,
                                         " from exception-handler " + methodName );  // @TODO I18N the comment
                    
            //
            // Also, if the exception-handler was marked "read-only", note this on the  tag.
            //
            Boolean readOnly = CompilerUtils.getBoolean( exHandlerAnnotation, READONLY_ATTR, true );
            if ( readOnly == null )
            {
                readOnly = parentApp.getFlowControllerInfo().getMergedControllerAnnotation().isReadOnly();
            }
            setReadonly( readOnly != null && readOnly.booleanValue() );
        }
        else
        {
            assert path != null;
            setPath( path );
        }
    }
    
    static void addCatches( Collection catches, ExceptionContainer container, ClassDeclaration jclass,
                            GenStrutsApp strutsApp, ForwardContainer forwardContainer )
    {
        if ( catches != null )
        {
            for ( Iterator i = catches.iterator(); i.hasNext(); )  
            {
                AnnotationInstance ann = ( AnnotationInstance ) i.next();
                TypeDeclaration containingType = ann.getContainingType();
                
                // If this is an inherited exception-catch, add a delegating exception config.
                if (CompilerUtils.typesAreEqual(containingType, jclass)) {
                    container.addException(new GenExceptionModel(strutsApp, ann, jclass, forwardContainer));
                } else {
                    container.addException(new DelegatingExceptionModel(container, strutsApp, ann, containingType));
                }
            }
        }
    }    
}
