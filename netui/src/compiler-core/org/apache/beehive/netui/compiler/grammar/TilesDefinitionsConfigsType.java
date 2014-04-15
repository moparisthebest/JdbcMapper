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
import org.apache.beehive.netui.compiler.FlowControllerInfo;
import org.apache.beehive.netui.compiler.FatalCompileTimeException;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationTypeElementDeclaration;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationValue;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;

import java.util.List;
import java.util.Iterator;

public class TilesDefinitionsConfigsType extends WebappPathType
{
    public TilesDefinitionsConfigsType( String requiredRuntimeVersion, AnnotationGrammar parentGrammar,
                                        FlowControllerInfo fcInfo )
    {
        super( false, requiredRuntimeVersion, parentGrammar, fcInfo );
    }

    
    public Object onCheck( AnnotationTypeElementDeclaration valueDecl, AnnotationValue value,
                           AnnotationInstance[] parentAnnotations, MemberDeclaration classMember,
                           int annotationArrayIndex )
            throws FatalCompileTimeException
    {
        List values = ( List ) value.getValue();

        if ( values != null )
        {
            for ( Iterator ii = values.iterator(); ii.hasNext(); )  
            {
                AnnotationValue pathValue = ( AnnotationValue ) ii.next();

                if ( pathValue != null )
                {
                    String filePath = ( String ) pathValue.getValue();
                    if ( filePath == null || filePath.length() == 0 || filePath.charAt( 0 ) != '/' )
                    {
                        addError( value, "error.absolute-path-required-for-tiles-def" );
                    }
                }

                super.onCheck( valueDecl, pathValue, parentAnnotations, classMember, annotationArrayIndex );
            }
        }

        return null;
    }

    protected boolean checkAnyExtension()
    {
        return true;
    }
}
