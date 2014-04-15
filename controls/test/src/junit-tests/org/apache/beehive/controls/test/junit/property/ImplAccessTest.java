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

package org.apache.beehive.controls.test.junit.property;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.property.PropertyControl;
import org.apache.beehive.controls.test.controls.property.PropertyControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.beans.Beans;

/**
 * A TestCase that tests accessing property values via control context in control impl
 * All tests on controls instantiated declaratively are deactivated until this feature is supported.
 */
public class ImplAccessTest extends ControlTestCase {

    /**
     * A control that declares some propertySets in its control interface.
     * It has a method that allow accessing these property values via control context.
     */
    @Control
    private PropertyControlBean myControl;

    /**
     * Tests accessing property values via control context.
     * By invoking sayHello method, the property value is retrieved via control context.
     * The control bean is instantiated by declaration.
     */
    public void testContextAccessByDeclare() throws Exception {
        assertEquals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE1, myControl.getAttribute1ByContext());
        assertEquals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE3, myControl.getAttribute3ByContext());
    }

    /**
     * Tests accessing property values via control context.
     * By invoking sayHello method, the property value is retrieved via control context.
     * The control bean is instantiated programmatically.
     */
    public void testContextAccessByProgram() throws Exception {
        PropertyControlBean sbean = (PropertyControlBean) Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.property.PropertyControlBean");

        assertEquals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE1, sbean.getAttribute1ByContext());
        assertEquals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE3, sbean.getAttribute3ByContext());
    }
}
