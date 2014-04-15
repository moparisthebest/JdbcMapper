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

package org.apache.beehive.controls.test.junit.composition;

import junit.framework.Assert;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.bean.Controls;
import org.apache.beehive.controls.test.controls.composition.InnerControlBean;
import org.apache.beehive.controls.test.controls.composition.OuterControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * A TeseCase that tests control composition.  The outer control is instantiated programmatically, and the outer
 * control instantiates the nested control programmatically
 */
public class ProgrammaticTest extends ControlTestCase {

    @Control
    private OuterControlBean _outer;

    /**
     * A control that contains a nested control
     */
    private OuterControlBean outerControl;
    private OuterControlBean outerControl2;


    /* Instantiate the outerControl once*/
    public void setUp() throws Exception {
        super.setUp();

        outerControl = (OuterControlBean) java.beans.Beans.instantiate(Thread.currentThread().getContextClassLoader(),
                                                                       "org.apache.beehive.controls.test.controls.composition.OuterControlBean");

        outerControl2 = (OuterControlBean) Controls.instantiate(Thread.currentThread().getContextClassLoader(),
                                                                "org.apache.beehive.controls.test.controls.composition.OuterControlBean", null);
    }

    /**
     * Tests outer control instantiats nested control by declaration
     */
    public void testInstantiate() {
        assertNotNull(outerControl);
        assertNotNull(outerControl.instantiateNestedControlProgrammatically());

        InnerControlBean innerBean = outerControl.instantiateNestedControlWithProperty();
        assertNotNull(innerBean);
        assertEquals(innerBean.getName(), "ken");
        assertEquals(innerBean.getJob(), "engineer");
        assertTrue(innerBean.getRank() == 2);
        assertNotNull(outerControl2);
    }

    /**
     * Tests outer control getting inner control property from control context
     */
    public void testGetProppertyByContext() {
        assertNotNull(outerControl);
        InnerControlBean innercontrol = outerControl.instantiateNestedControlProgrammatically();
        assertNotNull(innercontrol);
        assertEquals("Bob", innercontrol.getNameFromContext());
        assertNull(innercontrol.getJobFromContext());
    }

    /**
     * Tests outer control getting inner control property by getter
     */
    public void testGetProppertyByGetter() {
        assertNotNull(outerControl);
        InnerControlBean innercontrol = outerControl.instantiateNestedControlProgrammatically();
        assertNotNull(innercontrol);
        assertEquals("Bob", innercontrol.getName());
        assertNull(innercontrol.getJob());
    }

    /**
     * Tests outer control setting inner control property by setter
     */
    public void testSetProppertyBySetter() {
        assertNotNull(outerControl);
        InnerControlBean innercontrol = outerControl.instantiateNestedControlProgrammatically();
        assertNotNull(innercontrol);
        innercontrol.setName("new name");
        innercontrol.setJob("new job");
        assertEquals("new name", innercontrol.getNameFromContext());
        assertEquals("new job", innercontrol.getJobFromContext());
    }

    /**
     * Tests reconfigured property.
     * Outer control reconfigures the inner control's property when instantiating it
     * Deactivated by CR190302
     */
    public void testReconfiguredProperty() {
        assertNotNull(outerControl);
        InnerControlBean innercontrol = outerControl.instantiateNestedControlWithProperty();
        assertNotNull(innercontrol);
        assertEquals("ken", innercontrol.getNameFromContext());
        assertEquals("engineer", innercontrol.getJobFromContext());
    }

    /**
     * Tests outer control receiving events from nested control using
     * EventHandler
     * This is deactivated, as declarative instantiating control in java is not supported
     */
    public void testEventHandler() {
        assertNotNull(outerControl);
        assertEquals("0", outerControl.testActivityWakeup());
        assertEquals("0", outerControl.testActivityReadMessage());
        assertEquals("0", outerControl.testActivityReport());
        assertEquals("0", outerControl.testActionShopping());
        assertEquals("0", outerControl.testActionDostuff());
    }

    /**
     * Tests outer control receiving events from nested control using
     * inner class
     */
    public void testEventInnerClass() {
        assertNotNull(outerControl);
        assertEquals("0", outerControl.testInnerClassListener());
    }

    /**
     * Tests outer control receiving events from nested control using
     * event listener
     */
    public void testEventListener() {
        assertNotNull(outerControl);
        assertEquals("0", outerControl.testEventListener());
    }
}
