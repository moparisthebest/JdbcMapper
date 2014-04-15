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
import org.apache.beehive.controls.test.controls.property.PropEvents;
import org.apache.beehive.controls.test.controls.property.PropEventsBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This test case validates bound and constrained property behaviors, w.r.t. to the delivery
 * of PropertyChange events for bound and constrained events
 */
public class PropEventsTest extends ControlTestCase {
    @Control
    private PropEventsBean _eventBean;

    /**
     * This base class hold a queue of property change events and makes them accessible
     * for event validation.
     */
    abstract static class QueueListener {
        QueueListener() {
            initEvents();
        }

        /**
         * Resets/initializes the internal event queue
         */
        public void initEvents() {
            eventQueue = new ArrayList<PropertyChangeEvent>();
        }

        /**
         * Returns the collection of events
         */
        public Collection<PropertyChangeEvent> getEvents() {
            return eventQueue;
        }

        protected ArrayList<PropertyChangeEvent> eventQueue;
    }

    static class ChangeTestListener extends org.apache.beehive.controls.test.junit.property.PropEventsTest.QueueListener
            implements PropEvents.ImplPropertyChange  // + java.beans.PropertyChangeListener
    {
        /**
         * Implementation of PropertyChangeListener.propertyChange().  Will enqueue
         * the received event.
         */
        public void propertyChange(PropertyChangeEvent pce) {
            eventQueue.add(pce);
        }
    }

    static class VetoableTestListener extends org.apache.beehive.controls.test.junit.property.PropEventsTest.QueueListener
            implements PropEvents.ImplVetoableChange  // + java.beans.VetoableChangeListener
    {
        VetoableTestListener(boolean doVeto) {
            _doVeto = doVeto;
        }

        /**
         * Implementation of PropertyChangeListener.propertyChange().  Will enqueue
         * the received event.
         */
        public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
            eventQueue.add(pce);

            // Veto attempts to set even values
            if (_doVeto && (((Integer) pce.getNewValue()).intValue() & 1) == 0)
                throw new PropertyVetoException("Sorry", pce);
        }

        boolean _doVeto;
    }

    private void validatePropertyEvents(Collection<PropertyChangeEvent> events) {
        // Now validate the received properties
        int i = 0;
        for (PropertyChangeEvent pce : events) {
            assertEquals(_eventBean, pce.getSource());
            assertEquals("boundInt", pce.getPropertyName());
            assertEquals(i, pce.getOldValue());
            assertEquals(i+1, pce.getNewValue());
            i++;
        }
        assertEquals(99, i);
    }

    /**
     * Basic test: setting/reading property values by a control client
     */
    public void testPropertyChange() throws Exception {
        // Set the test property to a well-defined initial value
        _eventBean.setBoundInt(0);

        // Create a new test listener and register it on the test bean
        ChangeTestListener extChange = new ChangeTestListener();
        _eventBean.addPropertyChangeListener(extChange);

        // Create an test listener and register it to indirectly from impl callbacks
        ChangeTestListener implChange = new ChangeTestListener();
        _eventBean.addImplPropertyChangeListener(implChange);

        // Call the bound property setter on the bean 100 times
        for (int i = 1; i < 100; i++) {
            _eventBean.setBoundInt(i);
        }

        // Validate the events received by the external listener
        validatePropertyEvents(extChange.getEvents());

        // Validate the events received by the implementation via the context
        validatePropertyEvents(implChange.getEvents());

        // Reset the event queues
        extChange.initEvents();
        implChange.initEvents();

        // Change an unbound property and verify that no property change event was delivered
        _eventBean.setBasicInt(0);
        assertEquals(0, extChange.getEvents().size());
        assertEquals(0, implChange.getEvents().size());

        // Remove the external event listener, change a bound property, and verify no event is
        // delivered on it, but is delivered to the implentation
        _eventBean.removePropertyChangeListener(extChange);
        _eventBean.setBoundInt(0);

        assertEquals(0, extChange.getEvents().size());
        assertTrue(implChange.getEvents().size() > 0);
    }

    /**
     * Validate the expected set of change and veto events received during the testVetoChange
     * test.  This is factored out so it can be used to validate events delivered to both
     * the implementation and an external listener (which should match)
     */
    private void validateVetoEvents(Iterator<PropertyChangeEvent> changeIter,
                                    Iterator<PropertyChangeEvent> vetoIter) {
        // Now validate the received properties, there should be one per vetoed property,
        // two per allowed change
        int i = 1;
        int expected = 0;
        boolean expectVeto;
        while (vetoIter.hasNext()) {
            PropertyChangeEvent vce = vetoIter.next();

            expectVeto = (i & 1) == 0;

            assertEquals(vce.getSource(), _eventBean);
            assertEquals("constrainedInt", vce.getPropertyName());
            assertEquals(expected, vce.getOldValue());
            assertEquals(i, vce.getNewValue());

            if (expectVeto) {
                // If a veto occurred, then there should be a 2nd vetoable change event that
                // goes from the vetoed value back to the last valid value
                assertTrue(vetoIter.hasNext());

                //
                // Pull the next event, which should revert from the attempted change back
                // to the last accepted value
                //
                vce = vetoIter.next();
                assertEquals(_eventBean, vce.getSource());
                assertEquals("constrainedInt", vce.getPropertyName());
                assertEquals(i, vce.getOldValue());
                assertEquals(expected, vce.getNewValue());
            }
            else {
                // Expected to succeed so look for the corresponding PropertyChange
                assertTrue(changeIter.hasNext());

                PropertyChangeEvent pce = changeIter.next();
                assertEquals(_eventBean, pce.getSource());
                assertEquals("constrainedInt", pce.getPropertyName());
                assertEquals(expected, pce.getOldValue());
                assertEquals(i, pce.getNewValue());
                expected = i;
            }

            i++;
        }
        assertEquals(99, expected);
    }

    /**
     * Basic test: setting/reading property values by a control client
     */
    public void testVetoChange() throws Exception {
        // Set the test property to a well-defined initial value
        _eventBean.setConstrainedInt(0);

        // Create a test listener and register it to indirectly receive impl callbacks
        // but not to veto anything
        VetoableTestListener implVeto = new VetoableTestListener(false);
        _eventBean.addImplVetoableChangeListener(implVeto);

        // Create a test listener and register it as an external listener that will veto
        VetoableTestListener extVeto = new VetoableTestListener(true);
        _eventBean.addVetoableChangeListener(extVeto);

        // Create an test listener and register it to indirectly from impl callbacks
        ChangeTestListener implChange = new ChangeTestListener();
        _eventBean.addImplPropertyChangeListener(implChange);

        // Create an external change listener and register it... this will be used to validate the
        // property changes that were not vetoed
        ChangeTestListener extChange = new ChangeTestListener();
        _eventBean.addPropertyChangeListener(extChange);

        //
        // Change the property multiple times, validating that veto exceptions propogate as
        // expected and that the retrieved property value matches the expected value (whether
        // accepted or vetoed)
        //
        int expected = 0;
        for (int i = 1; i < 100; i++) {
            boolean vetoed = false;
            boolean expectVeto = (i & 1) == 0;
            try {
                _eventBean.setConstrainedInt(i);
            }
            catch (PropertyVetoException pve) {
                vetoed = true;
            }

            if (vetoed) {
                assertTrue(expectVeto);
            }
            else {
                assertFalse(expectVeto);
                expected = i;
            }

            //
            // Read back the property and see if it was successfully changed or vetoed
            assertEquals(expected, _eventBean.getConstrainedInt());
        }

        // Validate the events generated on the implementation
        validateVetoEvents(implChange.getEvents().iterator(), implVeto.getEvents().iterator());

        // Validate the events generated on the external listener
        validateVetoEvents(extChange.getEvents().iterator(), extVeto.getEvents().iterator());

        // Reset the event queues
        extVeto.initEvents();
        extChange.initEvents();
        implVeto.initEvents();
        implChange.initEvents();

        //
        // Change an unbound property and verify that no property change events were delivered
        //
        _eventBean.setBasicInt(0);

        if (implVeto.getEvents().size() != 0 || implChange.getEvents().size() != 0)
            fail("Unexpected impl event delivered on unbound property change");

        if (extVeto.getEvents().size() != 0 || extChange.getEvents().size() != 0)
            fail("Unexpected external event delivered on unbound property change");

        //
        // Remove the external veto event listener but not the impl veto listener change listener,
        // change a constrained property, and verify no external veto event is delivered but an
        // impl veto and external change event is delivered
        //
        _eventBean.removeVetoableChangeListener(extVeto);
        _eventBean.removeImplPropertyChangeListener(implChange);
        _eventBean.setConstrainedInt(1);

        if (extVeto.getEvents().size() != 0)
            fail("Unexpected external event delivered after listener removed");

        if (implVeto.getEvents().size() == 0)
            fail("No impl event delivered after external listener removed");

        if (extChange.getEvents().size() != 1)
            fail("External change event not delivered after listener removed");

        if (implChange.getEvents().size() != 0)
            fail("Unexpected Impl change event delivered after listener removed");
    }
}
