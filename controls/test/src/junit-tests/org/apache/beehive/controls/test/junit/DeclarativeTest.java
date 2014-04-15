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

import java.io.ByteArrayOutputStream;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.test.controls.composition.OuterControl;
import org.apache.beehive.controls.test.controls.composition.OuterControlBean;
import org.apache.beehive.controls.test.controls.composition.InnerControlBean;
import org.apache.beehive.controls.test.junit.utils.ControlTestUtils;

/**
 * A TestCase that tests control composition.
 * The outer control is instantiated declaratively, and the outer
 * control instantiates the nested control declaratively
 *
 * Instantiating controls declaratively is not supported currently.
 * All tests are deactivated until this is supported
 */
public class DeclarativeTest
    extends ControlTestCase
    implements java.io.Serializable {

    @Control
    private OuterControlBean _outerControl;

    private String _onReportMessage;

    @EventHandler(field="_outerControl", eventSet=OuterControl.OuterEvents.class, eventName="report")
    public int onReport( String msg ) {
        _onReportMessage = msg;
        return 0;
    }

    /**
     * Tests outer control instantiats nested control by declaration
     */
    public void testInstantiation()
        throws Exception {
        assertNotNull(_outerControl);
        assertNotNull(_outerControl.getDeclaredNestedControl());
        assertNotNull(_outerControl.getDeclaredNestedControl2());
    }

    /**
     * Tests outer control getting inner control property from control context
     */
    public void testGetPropertyByContext() {
        assertNotNull(_outerControl);

        InnerControlBean innercontrol=_outerControl.getDeclaredNestedControl();
        assertNotNull(innercontrol);
        assertEquals("Bob",innercontrol.getNameFromContext());
        assertNull(innercontrol.getJobFromContext());
    }

    /**
     * Tests reconfigured property.
     * Outer control reconfigures the inner control's property when instantiating it
     */
    public void testReconfiguredProperty() {
        assertNotNull(_outerControl);
        InnerControlBean innercontrol=_outerControl.getDeclaredNestedControl2();
        assertNotNull(innercontrol);
        assertEquals("Bob",innercontrol.getNameFromContext());
        assertEquals("farmer",innercontrol.getJobFromContext());
    }

    /**
     * Tests outer control receiving events from nested control using
     * EventHandler
     */
    public void testEventHandler() {
        assertNotNull(_outerControl);
        assertEquals("0",_outerControl.testActivityWakeup());
        assertEquals("0",_outerControl.testActivityReadMessage());
        assertEquals("0",_outerControl.testActivityReport());
        assertEquals("0",_outerControl.testActionShopping());
        assertEquals("0",_outerControl.testActionDostuff());
    }

    /**
     * Tests outer control firing events to us
     */
    public void testOuterEvents() {
        //System.out.println( "_onReportMessage=" + _onReportMessage );

        assertNotNull(_outerControl);

        _outerControl.fireOuterEvents("this is the reported msg");
        assertEquals("this is the reported msg", _onReportMessage );
        //System.out.println( "_onReportMessage=" + _onReportMessage );
    }

    /**
     * Test to ensure that control identifiers and relationships are maintained correctly after
     * serializing and deserializing a control tree.
     */
    public void testControlIDs()
        throws Exception {

        assertEquals("_outerControl", _outerControl.getControlID());
        assertEquals("_outerControl/innerControl", _outerControl.getDeclaredNestedControl().getControlID());

        ByteArrayOutputStream baos = ControlTestUtils.serialize(_outerControl);

        Object object = ControlTestUtils.deserialize(baos);
        assertTrue(object instanceof OuterControlBean);
        OuterControlBean outerControlCopy = (OuterControlBean)object;

        assertFalse(outerControlCopy == _outerControl);
        assertEquals("_outerControl", outerControlCopy.getControlID());
        assertEquals("_outerControl/innerControl", outerControlCopy.getDeclaredNestedControl().getControlID());
    }
}
