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

import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;
import org.apache.beehive.netui.compiler.typesystem.type.PrimitiveType;
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.RuntimeVersionChecker;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;


public class ValidateRequiredGrammar
        extends BaseValidationRuleGrammar
{
    public ValidateRequiredGrammar( CoreAnnotationProcessorEnv env, Diagnostics diags, RuntimeVersionChecker rvc )
    {
        super( env, diags, rvc );
    }

    protected boolean onBeginCheck( AnnotationInstance annotation, AnnotationInstance[] parentAnnotations,
                                    MemberDeclaration classMember )
            throws FatalCompileTimeException
    {
        //
        // Add a warning when this annotation is on a getter method which returns a primitive type.
        // In that case, it will never be null.
        //
        if ( classMember instanceof MethodDeclaration )
        {
            TypeInstance returnType = ( ( MethodDeclaration ) classMember ).getReturnType();
            
            if ( returnType instanceof PrimitiveType )
            {
                addWarning( annotation, "warning.validate-required-on-primitive-type",
                            ANNOTATION_INTERFACE_PREFIX + VALIDATE_REQUIRED_TAG_NAME );
            }
        }
        
        return super.onBeginCheck( annotation, parentAnnotations, classMember );
    }
}
