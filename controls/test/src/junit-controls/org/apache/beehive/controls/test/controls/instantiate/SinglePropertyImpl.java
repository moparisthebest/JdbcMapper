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

package org.apache.beehive.controls.test.controls.instantiate;

import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;

/**
 * A control impl that accesses the property declared by its control interface via control context
 */

@ControlImplementation(isTransient=true)
public class SinglePropertyImpl implements SingleProperty
{
    @Context ControlBeanContext context;

    /*Accesses the propertySet value and returns the value*/
    public String sayHello()
    {
        /**BUG: could not refer to Greeting directly*/
        Greeting greeting= context.getControlPropertySet(Greeting.class);

        return greeting.GreetWord();
    }

}
