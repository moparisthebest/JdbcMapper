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

import org.apache.beehive.controls.test.controls.beancontextchild.BeanChildPeer;
import org.apache.beehive.controls.test.controls.beancontextchild.DummyBeanContext;
import org.apache.beehive.controls.test.controls.beancontextchild.ChangeListener;
import org.apache.beehive.controls.test.controls.beancontextchild.AlwaysVetoListener;
import org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport;
import junit.framework.TestCase;

import java.beans.beancontext.BeanContext;
import java.beans.PropertyVetoException;
import java.beans.PropertyChangeEvent;

/**
 * Junit tests for the ControlBeanContextChildSupport class.
 */
public class ControlBeanContextChildSupportTest extends TestCase {

    /**
     * Create a new ControlBeanContextChildSupport.
     */
    public void testCreate() {
        ControlBeanContextChildSupport _cbcChildSupport = new ControlBeanContextChildSupport();
        assertNotNull(_cbcChildSupport);
    }

    /**
     * Create a new ControlBeanContextChildSupport instance with a bean context child peer.
     */
    public void testCreateWithPeer() {
        ControlBeanContextChildSupport cbcChildSupport = new ControlBeanContextChildSupport(new BeanChildPeer());
        assertNotNull(cbcChildSupport);
    }

    /**
     * Create a new ControlBeanContextChildSupport instance and set
     * the bean context with no listeners registered.
     */
    public void testSetBeanContext_NoListeners() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport cbcChildSupport = getContext();
        assertNotNull(cbcChildSupport);

        BeanContext bc = new DummyBeanContext();

        try {
        cbcChildSupport.setBeanContext(bc);
        } catch (PropertyVetoException pve) {
            fail("Unexpected PropertyVetoException:" + pve.toString());
        }

        assertEquals(bc, cbcChildSupport.getBeanContext());
    }

    /**
     * Test setting a bean context on a child which has a peer defined.
     * The main difference here is that the peer should be set as the
     * event source for any PropertyChangeEvents/VetoableChangeEvents thrown.
     */
    public void testSetBeanContext_PropertyChangeListener_Peer() {
        BeanChildPeer bcp = new BeanChildPeer();
        ControlBeanContextChildSupport cbcChildSupport = new org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport(bcp);
        ChangeListener cl1 = new ChangeListener();

        cbcChildSupport.addPropertyChangeListener("beanContext", cl1);
        BeanContext bc = new DummyBeanContext();

        // change beancontext value from null to bc
        try {
            cbcChildSupport.setBeanContext(bc);
        } catch (PropertyVetoException pve) {
            fail("Unexpected PropertyVetoException:" + pve.toString());
        }

        PropertyChangeEvent[] evts1 = cl1.getEvents();
        assertEquals(1, evts1.length);
        assertNull(evts1[0].getOldValue());
        assertEquals(bc, evts1[0].getNewValue());
        assertEquals("beanContext", evts1[0].getPropertyName());
        assertEquals(bcp, evts1[0].getSource());
    }

    /**
     * Register 2 Propertychange listeners, set the bean context and verify
     * that the proper PropertyChangeEvents were generated.  Attempt to
     * set the BeanContext to the same value, verify that no events were
     * generated (noop).
     */
    public void testSetBeanContext_PropertyChangeListeners() {
        ControlBeanContextChildSupport cbcChildSupport = getContext();
        assertNotNull(cbcChildSupport);

        ChangeListener cl1 = new ChangeListener();
        ChangeListener cl2 = new ChangeListener();

        cbcChildSupport.addPropertyChangeListener("beanContext", cl1);
        cbcChildSupport.addPropertyChangeListener("beanContext", cl2);
        BeanContext bc = new DummyBeanContext();

        // change beancontext value from null to bc
        try {
            cbcChildSupport.setBeanContext(bc);
        } catch (PropertyVetoException pve) {
            fail("Unexpected PropertyVetoException:" + pve.toString());
        }

        PropertyChangeEvent[] evts1 = cl1.getEvents();
        assertEquals(1, evts1.length);
        assertNull(evts1[0].getOldValue());
        assertEquals(bc, evts1[0].getNewValue());
        assertEquals("beanContext", evts1[0].getPropertyName());
        assertEquals(cbcChildSupport, evts1[0].getSource());

        PropertyChangeEvent[] evts2 = cl2.getEvents();
        assertEquals(1, evts2.length);
        assertNull(evts2[0].getOldValue());
        assertEquals(bc, evts2[0].getNewValue());
        assertEquals("beanContext", evts2[0].getPropertyName());
        assertEquals(cbcChildSupport, evts2[0].getSource());


        // this should be a noop event-wise, setBeanContext just returns
        // if beancontext is being set to same value.
        cl1.reset();
        cl2.reset();
        try {
            cbcChildSupport.setBeanContext(bc);
        } catch (PropertyVetoException pve) {
            fail("Unexpected PropertyVetoException:" + pve.toString());
        }

        evts1 = cl1.getEvents();
        assertEquals(0, evts1.length);
        evts2 = cl2.getEvents();
        assertEquals(0, evts2.length);

        cbcChildSupport.removePropertyChangeListener("beanContext", cl1);
        cbcChildSupport.removePropertyChangeListener("beanContext", cl2);
    }

    /**
     * Test a veto of setting the bean context.
     */
    public void testSetBeanContext_VetoListeners() {
        ControlBeanContextChildSupport cbcChildSupport = getContext();
        assertNotNull(cbcChildSupport);

        ChangeListener cl = new ChangeListener();
        AlwaysVetoListener avl = new AlwaysVetoListener();
        cbcChildSupport.addPropertyChangeListener("beanContext", cl);
        cbcChildSupport.addVetoableChangeListener("beanContext", avl);
        BeanContext bc = new DummyBeanContext();

        // change beancontext value from null to bc
        try {
            cbcChildSupport.setBeanContext(bc);
        } catch (PropertyVetoException pve) {
            assertEquals("beanContext", pve.getPropertyChangeEvent().getPropertyName());
            assertEquals(bc, pve.getPropertyChangeEvent().getNewValue());
            assertNull(pve.getPropertyChangeEvent().getOldValue());

            // should never have fired a property change event
            PropertyChangeEvent[] evts1 = cl.getEvents();
            assertEquals(0, evts1.length);
            return;
        }
        fail("Expected PropertyVetoException!!");
    }


    /**
     * A veto listener is only allowed to veto a beanContext change once,
     * this is enforced by the setBeanContext() method of the CBCCS.
     */
    public void testSetBeanContext_VetoOverride() {
        ControlBeanContextChildSupport cbcChildSupport = getContext();
        boolean caughtVeto = false;
        assertNotNull(cbcChildSupport);

        AlwaysVetoListener avl = new AlwaysVetoListener();
        cbcChildSupport.addVetoableChangeListener("beanContext", avl);
        BeanContext bc = new DummyBeanContext();

        // change beancontext value from null to bc
        try {
            cbcChildSupport.setBeanContext(bc);
        } catch (PropertyVetoException pve) {
            assertEquals("beanContext", pve.getPropertyChangeEvent().getPropertyName());
            assertEquals(bc, pve.getPropertyChangeEvent().getNewValue());
            assertNull(pve.getPropertyChangeEvent().getOldValue());
            caughtVeto = true;
        }

        assertTrue(caughtVeto);

        // try again this should force override of veto
        try {
            cbcChildSupport.setBeanContext(bc);
        } catch (PropertyVetoException pve) {
            fail("Unexpected PropertyVetoException:" + pve.toString());
        }

        assertEquals(bc, cbcChildSupport.getBeanContext());
    }

    /**
     * Abstracts the type of BeanContextSupport object being created.
     */
    protected ControlBeanContextChildSupport getContext() {
        return new ControlBeanContextChildSupport();
    }
}
