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
package org.apache.beehive.controls.runtime.generator.apt;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;

public class ControlAnnotationProcessorFactory implements AnnotationProcessorFactory
{
    private static final Collection<String> _supportedAnnotations =
            Collections.unmodifiableCollection(
                Arrays.asList(new String[] {
                    org.apache.beehive.controls.api.bean.ControlInterface.class.getName(),
                    org.apache.beehive.controls.api.bean.ControlExtension.class.getName(),
                    org.apache.beehive.controls.api.bean.ControlImplementation.class.getName(),
                    org.apache.beehive.controls.api.properties.PropertySet.class.getName()
                    }));

    private static final Collection<String> _supportedOptions =
            Collections.unmodifiableCollection(
                Arrays.asList(new String[] {
                    "-AcontrolGenerator",   // sets CodeGenerator class
                    }));

    public Collection<String> supportedOptions()
    {
        return _supportedOptions;
    }

    public Collection<String> supportedAnnotationTypes()
    {
        return _supportedAnnotations;
    }

    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds,
                                               AnnotationProcessorEnvironment env)
    {
        return new ControlAnnotationProcessor(atds, env);   
    }
}
