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

import org.apache.beehive.netui.compiler.model.StrutsApp;
import org.apache.beehive.netui.compiler.model.MessageResourcesModel;
import org.apache.beehive.netui.compiler.CompilerUtils;
import org.apache.beehive.netui.compiler.JpfLanguageConstants;
import org.apache.beehive.netui.compiler.typesystem.declaration.AnnotationInstance;


public class GenMessageBundleModel
        extends MessageResourcesModel
        implements JpfLanguageConstants
{
    public GenMessageBundleModel( StrutsApp parent, AnnotationInstance annotation )
    {
        super( parent );
        
        setParameter( CompilerUtils.getString( annotation, BUNDLE_PATH_ATTR, true ) );
        setKey( CompilerUtils.getString( annotation, BUNDLE_NAME_ATTR, true ) );
        setReturnNull( true );
    }
}
