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

package org.apache.beehive.controls.test.junit.context;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.context.ServiceGetterBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * When a control is instantiated the web tier or a JWS, the Control runtime automatically associates
 * it with the current active context.
 * When a control is instantiated in the java test case, there is no active context.
 * One approach is to use the MiltonControlContext that gets used in other places...
 * A MiltonControlContext needs to be called to startContext before instantiating the control,
 * then any newly instantiated control in the Java test will be associated with it.
 */
public class GetServiceTest extends ControlTestCase {
    /**
     * A control that exposes a function to access available services
     * via the control context service
     */
    @Control
    private ServiceGetterBean _serviceGetter;

    /**
     * Tests accessing ControlBeanContext via controls' context service.
     * In java environment, this should be an instanceof ControlBeanContext
     * The control is instantiated by declaration
     */
    public void testGetServiceFromContextByDeclaration() throws Exception {
        assertNotNull(_serviceGetter);
        assertNotNull(_serviceGetter.getBeanContext());
        assertNotNull(_serviceGetter.getControlBeanContext());
    }

    /**
     * Tests accessing servlet service via controls' context service
     * In java environment, this should be "NULL"
     * The control is instantiated programmatically
     */
    public void testGetServiceFromContextByProgram() throws Exception {
        ServiceGetterBean getterbean = (ServiceGetterBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.context.ServiceGetterBean");
        assertNotNull(getterbean);
        assertNotNull(getterbean.getBeanContext());
        assertNotNull(getterbean.getControlBeanContext());
    }
}
