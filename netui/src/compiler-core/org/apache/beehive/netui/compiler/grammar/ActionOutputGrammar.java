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
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.RuntimeVersionChecker;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.TypeDeclaration;
import org.apache.beehive.netui.compiler.typesystem.type.TypeInstance;

public class ActionOutputGrammar
        extends AnnotationGrammar
{
    private static final String[][] REQUIRED_ATTRS = { { NAME_ATTR }, { TYPE_ATTR } };

    public ActionOutputGrammar( CoreAnnotationProcessorEnv env, Diagnostics diags, RuntimeVersionChecker rvc )
    {
        super( env, diags, null, rvc );

        addMemberType( NAME_ATTR, new UniqueValueType( ACTION_OUTPUTS_ATTR, false, false, null, this ) );
        addMemberType( TYPE_ATTR, new TypeNameType( null, true, null, this ) );
    }

    protected void onCheckMember(AnnotationTypeElementDeclaration memberDecl, AnnotationValue member,
                                 AnnotationInstance annotation, AnnotationInstance[] parentAnnotations,
                                 MemberDeclaration classMember)
    {
        if (memberDecl.getSimpleName().equals(TYPE_HINT_ATTR)) {
            // Strip off any template args, and compare the typeHint to the actual type.
            String typeHintStr = member.getValue().toString();
            int angleBracket = typeHintStr.indexOf('<');
            String strippedTypeHintStr = angleBracket != -1 ? typeHintStr.substring(0, angleBracket) : typeHintStr;
            TypeDeclaration typeHint = getEnv().getTypeDeclaration(strippedTypeHintStr);

            // Add a warning if the typeHint type can't be resolved.
            if (typeHint == null) {
                addWarning(member, "warning.type-hint-unresolvable", TYPE_HINT_ATTR, typeHintStr);
            } else {
                // Add a warning if the typeHint type can't be assigned to the actual type.
                TypeInstance actualType = CompilerUtils.getTypeInstance(annotation, TYPE_ATTR, true);
                if (! CompilerUtils.isAssignableFrom(actualType, typeHint)) {
                    addWarning(member, "warning.type-hint-mismatch",
                               new Object[]{ TYPE_HINT_ATTR, typeHintStr, TYPE_ATTR, actualType.toString() });
                }
            }
        }
    }

    public String[][] getRequiredAttrs()
    {
        return REQUIRED_ATTRS;
    }
}
