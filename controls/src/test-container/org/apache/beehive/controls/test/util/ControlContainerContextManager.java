/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

   $Header:$
*/
package org.apache.beehive.controls.test.util;

import org.apache.beehive.controls.api.bean.Controls;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.test.ControlTestException;

/**
 *
 */
public class ControlContainerContextManager {

    private ControlContainerContext _controlContainerContext;

    protected ControlContainerContextManager(ControlContainerContext controlContainerContext) {
        super();
        _controlContainerContext = controlContainerContext;
    }

    public ControlContainerContext getControlContainerContext() {
        return ControlThreadContext.getContext();
    }

    public void instantiateControls(Object object) {
        Class testClass = object.getClass();
        try {
            Controls.initializeClient(Thread.currentThread().getContextClassLoader(), object, ControlThreadContext.getContext());
        }
        catch(Exception e) {
            if(e.getCause() instanceof ClassNotFoundException)
                System.err.println("Could not locate initializer for '" + getClass().getName() + "'.  Assuming no controls to initialize");
            else throw new ControlTestException("Exception initializing controls for type '" + testClass.getName() + "'", e);
        }
    }

    public void beginContext() {
        _controlContainerContext.beginContext();
    }

    public void endContext() {
        _controlContainerContext.endContext();
    }
}