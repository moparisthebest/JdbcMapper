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
import org.apache.beehive.controls.test.controls.property.Hello;
import org.apache.beehive.controls.test.controls.property.HelloBean;
import org.apache.beehive.controls.test.controls.property.PropertyControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.beans.Beans;

/**
 * A TestCase that tests accessing control's propertySet by both impl and client
 */

public class ClientImplTest extends ControlTestCase {
    /**
     * A control that declares some propertySets in its control interface
     */
    @Control
    private PropertyControlBean myControl;

    @Control
    private HelloBean myHelloControl;

    /**
     * Resets control's property value using setters on the bean class and
     * retrieves the new value using control context on control's impl.
     * This method instantiates HelloBean by declaration
     */
    public void testResetPropertyByDeclare() throws Exception {
        myControl.setAttribute1("New value for attribute1");
        myControl.setPropertyTwoAttribute3("New value for attribute3");

        assertEquals("New value for attribute1", myControl.getAttribute1ByContext());
        assertEquals("New value for attribute3", myControl.getAttribute3ByContext());
    }

    /**
     * Resets control's property value using setters on the bean class and
     * retrieves the new value using control context on control's impl.
     * This method instantiates HelloBean by program
     */
    public void testResetPropertyByProgram() throws Exception {
        PropertyControlBean sbean = (PropertyControlBean) Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.property.PropertyControlBean");

        sbean.setAttribute1("New value for attribute1");
        sbean.setPropertyTwoAttribute3("New value for attribute3");

        assertEquals("New value for attribute1", sbean.getAttribute1ByContext());
        assertEquals("New value for attribute3", sbean.getAttribute3ByContext());
    }

    /**
     * Verifies the fix of JIRA-149
     */
    public void testHelloControl() throws Exception {
        Hello.Gender theGender = myHelloControl.getGender();
        assertEquals(Hello.GenderType.NEUTRAL, theGender.value());
    }
}
