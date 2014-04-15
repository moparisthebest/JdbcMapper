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
import org.apache.beehive.controls.test.controls.property.SingleProperty;
import org.apache.beehive.controls.test.controls.property.SinglePropertyBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * Test objective:
 * JSR175 provides a mechanism for assigning a default value as part of a member definition.
 * The default value must be type consistent with the return type of the methods and it cannot be null.
 * If there is no default value defined, then the declaration of the annotation must include the member
 * (or a compile error will be generated).
 */
public class DefaultValueTest extends ControlTestCase {
    /**
     * If the property has a default value,property annoatation doesn't have the member
     * and should not cause a compile error
     */
    @Control
    @SingleProperty.Greeting
    private SinglePropertyBean myControl;

    /**
     * If the property doesn't have a default value,property annoatation has the member
     * no compile error
     */
    @Control
    @SingleProperty.Identity(name = "a control")
    private SinglePropertyBean myControl2;


    /**
     * Accesses property value by getter of the control bean instance.
     * The ccontrol bean is instantiated declaratively
     */
    public void testPropertyValue() throws Exception {
        assertNotNull(myControl.getGreetWord());
        assertEquals("a control", myControl2.getName());
    }

    /**
     * Accesses property of primitve type by getter
     */
    public void testPremitiveType() throws Exception {
        assertEquals(20, myControl.getAge());
    }
}
