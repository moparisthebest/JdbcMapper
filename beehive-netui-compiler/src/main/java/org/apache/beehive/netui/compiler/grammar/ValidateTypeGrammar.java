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
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.RuntimeVersionChecker;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.MethodDeclaration;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.type.PrimitiveType;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;


public class ValidateTypeGrammar
        extends BaseValidationRuleGrammar
{
    private static final String[][] REQUIRED_ATTRS = { { TYPE_ATTR } };
    
    public ValidateTypeGrammar( CoreAnnotationProcessorEnv env, Diagnostics diags,
                                RuntimeVersionChecker runtimeVersionChecker )
    {
        super( env, diags, runtimeVersionChecker );
        
        addMemberType( TYPE_ATTR, new PrimitiveTypeType() );
    }

    public String[][] getRequiredAttrs()
    {
        return REQUIRED_ATTRS;
    }

    private class PrimitiveTypeType
            extends AnnotationMemberType
    {
        public PrimitiveTypeType()
        {
            super( ValidateTypeGrammar.this.getRequiredRuntimeVersion(), ValidateTypeGrammar.this );
        }

        
        public Object onCheck( AnnotationTypeElementDeclaration valueDecl, AnnotationValue value,
                               AnnotationInstance[] parentAnnotations, MemberDeclaration classMember,
                               int annotationArrayIndex )
        {
            Object val = value.getValue();
            
            //
            // Make sure the type is in the list of valid types.
            //
            if ( ! ( val instanceof PrimitiveType ) )
            {
                addError( value, "error.must-be-primitive-type" );
            }
            else
            {
                PrimitiveType.Kind kind = ( ( PrimitiveType ) val ).getKind();
                
                if ( kind.equals( PrimitiveType.Kind.BOOLEAN ) )
                {
                    addError( value, "error.invalid-type" );
                }
                else if ( classMember instanceof MethodDeclaration )
                {
                    //
                    // Add a warning if this annotation is on a property getter of the same type, in which case the
                    // validation rule will never fail.
                    //
                    TypeInstance returnType = ( ( MethodDeclaration ) classMember ).getReturnType();
                    
                    if ( returnType instanceof PrimitiveType )
                    {
                        if ( ( ( PrimitiveType ) returnType ).getKind().equals( kind ) )
                        {
                            addWarning( value, "warning.validate-type-on-same-type", 
                                        ANNOTATION_INTERFACE_PREFIX + VALIDATE_TYPE_TAG_NAME,
                                        kind.toString().toLowerCase() );
                        }
                    }
                }
            }
            
            return null;
        }
    }
}
