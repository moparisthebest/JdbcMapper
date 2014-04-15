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

import com.sun.mirror.declaration.*;
import org.apache.beehive.controls.runtime.generator.apt.TwoPhaseAnnotationProcessor;

/**
 * The AptOperation class represents a control operation where the operation attributes
 * are derived using APT metadata.
 */
public class AptOperation extends AptMethod
{
    /**
     * Constructs a new ControlOperation instance where interface information is derived
     * from APT metadata
     * @param controlIntf the declaring ControlInterface
     * @param methodDecl the method associated with the operation
     */
    public AptOperation(AptControlInterface controlIntf, MethodDeclaration methodDecl, TwoPhaseAnnotationProcessor ap)
    {
        super(methodDecl, ap);
        _controlIntf = controlIntf;
        _operDecl = methodDecl;
    }


    /**
     * Returns the name of the static field that holds the name of this method.
     */
    public String getMethodField()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("_");
        sb.append(getName());
        int methodIndex = getIndex();
        if (methodIndex != -1)
            sb.append(methodIndex);
        sb.append("Method");
        return sb.toString();
    }

    /**
     * Returns the AptControlInterface associated with this ControlOperation
     */
    public AptControlInterface getControlInterface() { return _controlIntf; }

    MethodDeclaration _operDecl;
    AptControlInterface _controlIntf;
}
