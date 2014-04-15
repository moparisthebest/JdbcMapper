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

import org.apache.beehive.netui.compiler.RuntimeVersionChecker;
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.FlowControllerInfo;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;
import org.apache.beehive.netui.compiler.typesystem.declaration.MemberDeclaration;

import java.util.Map;
import java.util.List;
import java.util.Iterator;


public class MessageBundleGrammar
        extends BaseFlowControllerGrammar
{
    private static final String[][] REQUIRED_ATTRS = { { BUNDLE_PATH_ATTR } };
    
    
    public MessageBundleGrammar( CoreAnnotationProcessorEnv env, Diagnostics diags,
                                    String requiredRuntimeVersion, RuntimeVersionChecker runtimeVersionChecker,
                                    FlowControllerInfo fcInfo )
    {
        super( env, diags, requiredRuntimeVersion, runtimeVersionChecker, fcInfo );
        
        addMemberType( BUNDLE_PATH_ATTR, new UniqueValueType( MESSAGE_BUNDLES_ATTR, false, false, null, this ) );
        addMemberType( BUNDLE_NAME_ATTR, new UniqueValueType( MESSAGE_BUNDLES_ATTR, true, true, null, this ) );
    }

    public String[][] getRequiredAttrs()
    {
        return REQUIRED_ATTRS;
    }

    protected Object onEndCheck( AnnotationInstance annotation, AnnotationInstance[] parentAnnotations,
                                 MemberDeclaration classMember, Map checkResults )
    {
        String bundlePath = CompilerUtils.getString( annotation, BUNDLE_PATH_ATTR, false );
        String bundleName = CompilerUtils.getString( annotation, BUNDLE_NAME_ATTR, false );
        
        if ( bundleName.length() == 0 )
        {
            AnnotationInstance immediateParent = parentAnnotations[parentAnnotations.length - 1];
            List peerAnnotations =
                    CompilerUtils.getAnnotationArray( immediateParent, MESSAGE_BUNDLES_ATTR, false );
            
            for ( Iterator ii = peerAnnotations.iterator(); ii.hasNext(); )  
            {
                AnnotationInstance peerAnnotation = ( AnnotationInstance ) ii.next();
                if ( ! CompilerUtils.annotationsAreEqual(  annotation, peerAnnotation, false, getEnv() )
                     && CompilerUtils.getString( peerAnnotation, BUNDLE_NAME_ATTR, false ).length() == 0 )
                {
                    addError( annotation, "error.multiple-default-message-resources", BUNDLE_NAME_ATTR );
                }
            }
        }
        
        getFlowControllerInfo().addMessageBundle( bundleName, bundlePath );
        
        return null;
    }
}
