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
 * Test accessing control's propertySet by control bean's getter/setter
 */

public class ClientAccessTest extends ControlTestCase {
    /**
     * A control that declares some propertySet in its control interface
     */
    @Control
    private PropertyControlBean _myControl;

    /**
     * Accesses property value by getter of the control bean instance.
     * The ccontrol bean is instantiated declaratively
     */
    public void testGetterByDeclare() throws Exception {
        assertEquals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE1, _myControl.getAttribute1());
        assertEquals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE3, _myControl.getPropertyTwoAttribute3());
    }

    /**
     * Accesses property value by getter og control bean instance.
     * The control bean is instantiated programmatically
     */
    public void testGetterByProgram() throws Exception {
        PropertyControlBean sbean = (PropertyControlBean) Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.property.PropertyControlBean");

        assertEquals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE1, sbean.getAttribute1());
        assertEquals(PropertyControl.DEFAULT_ATTRIBUTE_VALUE3, sbean.getPropertyTwoAttribute3());
    }
}
