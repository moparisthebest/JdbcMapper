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

package org.apache.beehive.controls.test.junit;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.Controls;
import org.apache.beehive.controls.api.properties.BeanPropertyMap;
import org.apache.beehive.controls.test.controls.instantiate.BoundExtPropertySet;
import org.apache.beehive.controls.test.controls.instantiate.BoundPropertyControlBean;
import org.apache.beehive.controls.test.controls.instantiate.HelloControlBean;
import org.apache.beehive.controls.test.controls.instantiate.SingleProperty;
import org.apache.beehive.controls.test.controls.instantiate.SinglePropertyBean;

/**
 * A TestCase that tests instantiating controls in different ways
 * All tests on controls instantiated declaratively are deactivated until this feature is supported.
 */
public class InstantiationTest extends ControlTestCase {

    private static String EXPECTED_GREETING = "Good evening!";

    /**
     * A simple control with one method, no property declared
     */
    @Control
    private HelloControlBean _myHelloBean;


    /**
     * A simple control with one property declared
     * Resets the property value at declaration
     */
    @Control
    @SingleProperty.Greeting(GreetWord = "Good evening!")
    private SinglePropertyBean _myPropertyBean;

    /**
     * Tests instantiating a custom control programmatically
     */
    public void testProgrammaticInstantiation() throws Exception {
        HelloControlBean hc = (HelloControlBean) java.beans.Beans.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.instantiate.HelloControlBean");

        assertNotNull(hc);
        assertEquals("check", hc.hello("check"));
    }

    /**
     * Tests instantiating a custom control declaratively
     */
    public void testDeclarativeInstantiation() throws Exception {
        assertNotNull(_myHelloBean);
        assertEquals("check", _myHelloBean.hello("check"));
    }

    /**
     * Tests declaratively instantiating a custom control with propertySet
     */
    public void testDeclareWithProperty() throws Exception {
        assertEquals(EXPECTED_GREETING, _myPropertyBean.sayHello());
    }

    /**
     * Tests programmically instantiating a control with propertySet
     */
    public void testProgramWithProperty() throws Exception {
        BeanPropertyMap greetAttr = new BeanPropertyMap(SingleProperty.Greeting.class);
        greetAttr.setProperty(SinglePropertyBean.GreetWordKey, "Good afternoon!");

        SinglePropertyBean spbean = (SinglePropertyBean) Controls.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.instantiate.SinglePropertyBean",
                greetAttr);

        assertEquals("Good afternoon!", spbean.sayHello());
    }

    /**
     * Tests programmically instantiating a control with an external declared propertySet
     */
    public void testProgramWithExtProperty() throws Exception {
        BeanPropertyMap greetAttr = new BeanPropertyMap(BoundExtPropertySet.class);
        greetAttr.setProperty(BoundPropertyControlBean.AgeKey, Integer.valueOf(10));

        BoundPropertyControlBean bbean = (BoundPropertyControlBean) Controls.instantiate(
                Thread.currentThread().getContextClassLoader(),
                "org.apache.beehive.controls.test.controls.instantiate.BoundPropertyControlBean",
                greetAttr);
        assertEquals(10, bbean.getAge());
    }
}
