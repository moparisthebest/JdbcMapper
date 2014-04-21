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
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.ClassDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;


public class RaiseActionGrammar
        extends BaseFlowControllerGrammar
{
    private static final String[][] REQUIRED_ATTRS = { { ACTION_ATTR } };
    
    public RaiseActionGrammar( CoreAnnotationProcessorEnv env, Diagnostics diags, String requiredRuntimeVersion,
                               RuntimeVersionChecker runtimeVersionChecker, ClassDeclaration jpfClass,
                               FlowControllerInfo fcInfo )
    {
        super( env, diags, requiredRuntimeVersion, runtimeVersionChecker, fcInfo );
        addMemberType( ACTION_ATTR, new PageFlowActionType( jpfClass ) );
        addMemberType( OUTPUT_FORM_BEAN_ATTR, new MemberFieldType( null , null, this ) );
    }
    
    private class PageFlowActionType
            extends AnnotationMemberType
    {
        private ClassDeclaration _jpfClass;
        
        public PageFlowActionType( ClassDeclaration jpfClass )
        {
            super( RaiseActionGrammar.this.getRequiredRuntimeVersion(), RaiseActionGrammar.this );
            _jpfClass = jpfClass;
        }

        
        public Object onCheck( AnnotationTypeElementDeclaration valueDecl, AnnotationValue member,
                               AnnotationInstance[] parentAnnotations, MemberDeclaration classMember,
                               int annotationArrayIndex )
        {
            String action = ( String ) member.getValue();
            
            if ( _jpfClass != null
                 && ! WebappPathOrActionType.actionExists( action, _jpfClass, null, getEnv(), getFlowControllerInfo(), true ) )
            {
                getDiagnostics().addWarning( member, "warning.no-such-action", action, 
                                             CompilerUtils.getSourceFile( _jpfClass, true ) );
            }
            
            return null;
        }
    }
    
    public String[][] getRequiredAttrs()
    {
        return REQUIRED_ATTRS;
    }
}
