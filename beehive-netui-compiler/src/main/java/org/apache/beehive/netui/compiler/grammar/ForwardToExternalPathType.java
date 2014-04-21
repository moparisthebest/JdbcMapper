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

import org.apache.beehive.netui.compiler.AnnotationGrammar;
import org.apache.beehive.netui.compiler.AnnotationMemberType;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;

import java.util.Collection;

public class ForwardToExternalPathType
        extends DelegatingType
{
    public ForwardToExternalPathType( AnnotationMemberType baseType, String requiredRuntimeVersion,
                                      AnnotationGrammar parentGrammar )
    {
        super( baseType, requiredRuntimeVersion, parentGrammar );
    }

    
    public Object onCheck( AnnotationTypeElementDeclaration valueDecl, AnnotationValue value,
                           AnnotationInstance[] parentAnnotations, MemberDeclaration classMember,
                           int annotationArrayIndex )
        throws FatalCompileTimeException
    {
        String stringValue = ( String ) value.getValue();
        
        //
        // If we're forwarding to an external page, print a warning about error messages on the destination page not
        // having access to messages defined in the current page flow... *unless* the current page flow has no message
        // resources defined.
        //
        if ( stringValue.indexOf( '/' ) != -1 )
        {
            BaseFlowControllerGrammar fcGrammar = ( BaseFlowControllerGrammar ) getParentGrammar();
            Collection messageResources = 
                    fcGrammar.getFlowControllerInfo().getMergedControllerAnnotation().getMessageResources();
            if ( messageResources != null && messageResources.size() > 0 )
            {
                addWarning( value, "warning.exception-handler-forward-to-external-page", stringValue );
            }
        }
        
        return super.onCheck( valueDecl, value, parentAnnotations, classMember, annotationArrayIndex );
    }
}
