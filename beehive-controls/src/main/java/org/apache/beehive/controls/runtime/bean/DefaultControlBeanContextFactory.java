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
package org.apache.beehive.controls.runtime.bean;

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.spi.context.ControlBeanContextFactory;

/**
 * Default implementation of the {@link ControlBeanContextFactory} that simply creates an instance of a
 * {@link ControlBeanContext} given the current {@link ControlBean}.
 */
/* package */ class DefaultControlBeanContextFactory
    implements ControlBeanContextFactory {

    /**
     * Create the {@link ControlBeanContext} for the {@link ControlBean}.
     * @param controlBean
     * @return the {@link ControlBeanContext}
     */
    public ControlBeanContext instantiate(ControlBean controlBean) {
        if(!(controlBean instanceof org.apache.beehive.controls.runtime.bean.ControlBean))
            throw new IllegalArgumentException("The ControlBean of type \"" +
                controlBean.getClass().getName() +
                "\" is unsupported.  The ControlBean must extend " +
                org.apache.beehive.controls.runtime.bean.ControlBean.class.getName());

        /*
        The provided ControlBean is a "api.bean.ControlBean"; this factory implementation only creates
        ControlBeanContext implementations for "runtime.bean.ControlBean" types.  Ensuare that this is
        of that type.
         */
        org.apache.beehive.controls.runtime.bean.ControlBean runtimeControlBean =
            (org.apache.beehive.controls.runtime.bean.ControlBean)controlBean;

        /*
        Create a simple new ControlBeanContext.
        */
        return new org.apache.beehive.controls.runtime.bean.ControlBeanContext(runtimeControlBean);
    }
}
