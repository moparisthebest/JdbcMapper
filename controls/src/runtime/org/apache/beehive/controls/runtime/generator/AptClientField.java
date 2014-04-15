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
package org.apache.beehive.controls.runtime.generator;

import java.util.ArrayList;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.FieldDeclaration;

/**
 * The AptClientField class describes a reference to a client callback notifier within an
 * AptControlImplementation class.
 */
public class AptClientField extends AptField
{
    /**
     * Base constructor, protected so only a custom subclass can invoke
     * @param controlImpl the declaring AptControlImplementation
     */
    public AptClientField(AptControlImplementation controlImpl, FieldDeclaration fieldDecl)
    {
        super(fieldDecl);
        _controlImpl = controlImpl;
    };

    private AptControlImplementation _controlImpl;
}
