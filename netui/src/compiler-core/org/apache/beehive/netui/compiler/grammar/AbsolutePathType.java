/**
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
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;

public class AbsolutePathType extends AnnotationMemberType {

    public AbsolutePathType( String requiredRuntimeVersion, AnnotationGrammar parentGrammar )
    {
        super( requiredRuntimeVersion, parentGrammar);
    }

    
    public Object onCheck( AnnotationTypeElementDeclaration valueDecl, AnnotationValue member,
                           AnnotationInstance[] parentAnnotations, MemberDeclaration classMember,
                           int annotationArrayIndex )
    {
        AnnotationInstance parentAnnotation = parentAnnotations[ parentAnnotations.length - 1 ];
        String path = CompilerUtils.getString( parentAnnotation, PATH_ATTR, false );
        if ( ! path.startsWith("/") ) addError( member, "error.absolute-path-required-for-external-redirect", null );
        return null;
    }
}
