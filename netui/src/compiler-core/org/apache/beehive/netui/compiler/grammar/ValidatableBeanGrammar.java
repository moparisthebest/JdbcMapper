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
import org.apache.beehive.netui.compiler.RuntimeVersionChecker;
import org.apache.beehive.netui.compiler.Diagnostics;
import org.apache.beehive.netui.compiler.typesystem.env.CoreAnnotationProcessorEnv;

public class ValidatableBeanGrammar
        extends AnnotationGrammar
{
    private static final String[][] REQUIRED_ATTRS = { { VALIDATABLE_PROPERTIES_ATTR }, { TYPE_ATTR } };
    
    public ValidatableBeanGrammar( CoreAnnotationProcessorEnv env, Diagnostics diags, RuntimeVersionChecker rvc )
    {
        super( env, diags, VERSION_9_0_STRING, rvc );
        
        addMemberArrayGrammar( VALIDATABLE_PROPERTIES_ATTR, new ValidatablePropertyGrammar( env, diags, rvc ) );
        // TYPE_ATTR does not need a custom type
    }
    
    public String[][] getRequiredAttrs()
    {
        return REQUIRED_ATTRS;
    }
}

