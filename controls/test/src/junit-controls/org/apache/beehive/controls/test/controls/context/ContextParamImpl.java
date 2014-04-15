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

package org.apache.beehive.controls.test.controls.context;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;

import java.lang.reflect.Method;

@ControlImplementation(isTransient = true)
public class ContextParamImpl implements ContextParam, Extensible {
    @Context
    ControlBeanContext context;

    public String [] paramNameTest(Method m) {
        return context.getParameterNames(m);
    }

    public Param [] paramAnnotTest(Method m) {
        Param [] annots = new Param [m.getParameterTypes().length];
        for (int i = 0; i < annots.length; i++)
            annots[i] = context.getParameterPropertySet(m, i, Param.class);

        return annots;
    }

    //
    // The implementation of Extensible.invoke() for this class will echo back one of the
    // named parameters of the invoked method.  
    //
    // JCX-defined methods *must* follow two simple rules:
    //
    //  1. the first method is expected to contain the name of one of the method parameters
    //  2. the method return type must be assignment-compatible with the type of the request 
    //     parameter
    //
    public Object invoke(Method m, Object [] params) {
        return context.getParameterValue(m, (String) params[0], params);
    }
}
