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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.beancontext.BeanContextMembershipEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport;
import org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport;
import org.apache.beehive.controls.test.controls.beancontext.BeanContextProxyImpl;
import org.apache.beehive.controls.test.controls.beancontext.MembershipListener;
import org.apache.beehive.controls.test.controls.beancontext.VisibilityImpl;
import org.apache.beehive.controls.test.controls.beancontext.BeanContextPeer;
import org.apache.beehive.controls.test.controls.beancontextchild.AlwaysVetoListener;
import org.apache.beehive.controls.test.controls.beancontextchild.ChangeListener;

/**
 */
public class ControlBeanContextSupportTest
    extends ControlBeanContextChildSupportTest {

    /**
     * Create a new ControlBeanContextSupport.
     */
    public void testCreateCBCS() {
        ControlBeanContextSupport _cbcSupport = new ControlBeanContextSupport();
        assertNotNull(_cbcSupport);
    }

    /**
     * Create a new ControlBeanContextChildSupport instance with a bean delegate.
     */
    public void testCreateWithDelegate() {
        ControlBeanContextSupport _cbcSupport = new ControlBeanContextSupport(new BeanContextPeer());
        assertNotNull(_cbcSupport);
    }

    //
    // Collection Api tests
    //

    /**
     * Test addAll() collection api.
     */
    public void testAddAll() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();

        try {
            cbcs.addAll(Collections.EMPTY_LIST);
        }
        catch (UnsupportedOperationException uoe) {
            return;
        }
        fail("Expected UnsupportedOperationException!!");
    }

    /**
     * Test clear() collection api.
     */
    public void testClear() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();

        try {
            cbcs.clear();
        }
        catch (UnsupportedOperationException uoe) {
            return;
        }
        fail("Expected UnsupportedOperationException!!");
    }

    /**
     * Test removeAll() collection api.
     */
    public void testRemoveAll() {
        ControlBeanContextSupport cbcs = getContext();

        try {
            cbcs.removeAll(Collections.EMPTY_LIST);
        }
        catch (UnsupportedOperationException uoe) {
            return;
        }
        fail("Expected UnsupportedOperationException!!");
    }


    /**
     * Test retainAll() collection api.
     */
    public void testRetainAll() {
        ControlBeanContextSupport cbcs = getContext();

        try {
            cbcs.retainAll(Collections.EMPTY_LIST);
        }
        catch (UnsupportedOperationException uoe) {
            return;
        }
        fail("Expected UnsupportedOperationException!!");
    }

    /**
     * Test contains() collection api.
     */
    public void testContains() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport child1 = new ControlBeanContextChildSupport();
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport child2 = new ControlBeanContextChildSupport();

        assertTrue(cbcs.add(child1));
        assertTrue(cbcs.contains(child1));
        assertFalse(cbcs.contains(child2));
    }

    /**
     * Test containsAll() collection api.
     */
    public void testContainsAll() {
        ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child1 = new ControlBeanContextChildSupport();
        ControlBeanContextChildSupport child2 = new ControlBeanContextChildSupport();
        ControlBeanContextChildSupport child3 = new org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport();

        assertTrue(cbcs.add(child1));
        assertTrue(cbcs.add(child2));

        ArrayList children = new ArrayList();
        children.add(child1);
        children.add(child2);
        assertTrue(cbcs.containsAll(children));

        children.add(child3);
        assertFalse(cbcs.containsAll(children));
    }

    /**
     * Test isEmpty() collection api.
     */
    public void testIsEmpty() {
        ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport();

        assertTrue(cbcs.isEmpty());
        assertTrue(cbcs.add(child));
        assertFalse(cbcs.isEmpty());
    }

    /**
     * Test interator() collection api. Iterator returned should not
     * allow for removal of items. NOTE: ControlBeanContextSupport
     * uses a Map to store its children so the iteration order
     * is not predictable.
     */
    public void testIterator() {

        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child1 = new ControlBeanContextChildSupport();
        assertTrue(cbcs.add(child1));

        Iterator i = cbcs.iterator();
        assertTrue(i.hasNext());
        assertEquals(child1, i.next());

        try {
            i.remove();
        }
        catch (UnsupportedOperationException uoe) {
            return;
        }
        fail("Expected UnsupportedOperationException when trying to remove item via Iterator!!");
    }

    /**
     * Test size() collection api.
     */
    public void testSize() {
        ControlBeanContextSupport cbcs = getContext();
        assertEquals(0, cbcs.size());

        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        assertTrue(cbcs.add(child));
        assertEquals(1, cbcs.size());
    }

    /**
     * Test toArray() collection api.
     */
    public void testToObjectArray() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        assertTrue(cbcs.add(child));
        Object[] array = cbcs.toArray();
        assertEquals(1, array.length);
        assertEquals(child, array[0]);
    }

    /**
     * Test toArray(Object[]) collection api.
     */
    public void testToObjectArray2() {
        ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        assertTrue(cbcs.add(child));
        Object[] array = cbcs.toArray(new Object[1]);
        assertEquals(1, array.length);
        assertEquals(child, array[0]);
    }

    //////////////////// add() API ////////////////////////////////////////////////////////////////////////////

    /**
     * Add a single child to the context, verify that all the proper events are generated.
     */
    public void testAddSingleChild() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);

        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        ChangeListener cl = new ChangeListener();
        child.addPropertyChangeListener("beanContext", cl);

        assertTrue(cbcs.add(child));

        // verify that....

        // a BeanContextMembershipEvent is fired...
        BeanContextMembershipEvent[] bcme = ml.getEvents();
        assertEquals(1, bcme.length);
        assertTrue(bcme[0].contains(child));

        // the child recieved a PropertyEvent that is parent was changed
        PropertyChangeEvent[] pce = cl.getEvents();
        assertEquals(1, pce.length);
        assertEquals("beanContext", pce[0].getPropertyName());
        assertNull(pce[0].getOldValue());
        assertEquals(cbcs, pce[0].getNewValue());

        // that the child was added to the collection
        Object[] children = cbcs.toArray();
        assertEquals(1, children.length);
        assertEquals(child, children[0]);
    }

    /**
     * Add a single child to a BeanContext which has a peer.
     */
    public void testAddSingleChild_Peer() {
        BeanContextPeer bcp = new BeanContextPeer();
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = new org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport(bcp);

        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);

        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        assertTrue(cbcs.add(child));

        // verify that....

        // a BeanContextMembershipEvent is fired and the source is the peer ...
        BeanContextMembershipEvent[] bcme = ml.getEvents();
        assertEquals(1, bcme.length);
        assertTrue(bcme[0].contains(child));
        assertEquals(bcp, bcme[0].getSource());

        // the nested context of the child is the peer
        assertEquals(bcp, child.getBeanContext());

        // that the child was added to the collection
        Object[] children = cbcs.toArray();
        assertEquals(1, children.length);
        assertEquals(child, children[0]);
    }

    /**
     * Add a child which implements the BeanContextProxy interface.
     */
    public void testAddProxyChild() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);

        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        BeanContextProxyImpl proxy = new BeanContextProxyImpl(child);
        ChangeListener cl = new ChangeListener();
        child.addPropertyChangeListener("beanContext", cl);

        assertTrue(cbcs.add(proxy));

        // verify that....

        // a BeanContextMembershipEvent is fired...
        BeanContextMembershipEvent[] bcme = ml.getEvents();
        assertEquals(1, bcme.length);
        assertTrue(bcme[0].contains(proxy));

        // the child recieved a PropertyEvent that is parent was changed
        PropertyChangeEvent[] pce = cl.getEvents();
        assertEquals(1, pce.length);
        assertEquals("beanContext", pce[0].getPropertyName());
        assertNull(pce[0].getOldValue());
        assertEquals(cbcs, pce[0].getNewValue());

        // test that the proxy as well as the bcc it references were added to the bean context
        // as children -- may seem a bit odd but the spec says it should work this way!
        Object[] children = cbcs.toArray();
        assertEquals(2, children.length);
    }

    /**
     * Add a child which implements the Visibility interface.
     */
    public void testAddVisibilityChild() {
        ControlBeanContextSupport cbcs = getContext();
        cbcs.okToUseGui();

        VisibilityImpl child = new VisibilityImpl();
        assertTrue(cbcs.add(child));
        assertTrue(child.getCanUseGui());

        cbcs = new org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport();
        cbcs.dontUseGui();
        assertTrue(cbcs.add(child));
        assertFalse(child.getCanUseGui());
    }

    /**
     * Attempt to re-add an existing child.
     */
    public void testAddExistingChild() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        assertTrue(cbcs.add(child));
        assertFalse(cbcs.add(child));
    }

    /**
     * Add a child who veto's its add to the bean context.
     */
    public void testAddChildWhoVetos() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        AlwaysVetoListener avl = new AlwaysVetoListener();
        child.addVetoableChangeListener("beanContext", avl);

        try {
            cbcs.add(child);
        }
        catch (IllegalStateException ise) {
            return;
        }
        fail("Excpected IllegalStateException!!!");
    }

    ////////////////////// test remove() API /////////////////////////////////////////////////////////////////////

    /**
     * Remove a child from the bean context, verify the proper events are generated.
     */
    public void testRemoveSingleChild() {

        ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        // add the child
        assertTrue(cbcs.add(child));

        // setup listeners
        ChangeListener cl = new ChangeListener();
        child.addPropertyChangeListener("beanContext", cl);

        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);

        // remove the child
        assertTrue(cbcs.remove(child));

        // verify that....

        // a BeanContextMembershipEvent is fired...
        BeanContextMembershipEvent[] bcme = ml.getEvents();
        assertEquals(1, bcme.length);
        assertTrue(bcme[0].contains(child));

        // the child recieved a PropertyEvent that is parent was changed
        PropertyChangeEvent[] pce = cl.getEvents();
        assertEquals(1, pce.length);
        assertEquals("beanContext", pce[0].getPropertyName());
        assertNull(pce[0].getNewValue());
        assertEquals(cbcs, pce[0].getOldValue());

        // that the child was added to the collection
        Object[] children = cbcs.toArray();
        assertEquals(0, children.length);
    }

    /**
     * Remove a child which implements BeanContextProxy.
     */
    public void testRemoveProxyChild() {
        ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        BeanContextProxyImpl proxy = new BeanContextProxyImpl(child);

        // add the child
        assertTrue(cbcs.add(proxy));

        // setup listeners
        ChangeListener cl = new ChangeListener();
        child.addPropertyChangeListener("beanContext", cl);
        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);

        // remove the child
        assertTrue(cbcs.remove(proxy));

        // verify that....

        // a BeanContextMembershipEvent is fired...
        BeanContextMembershipEvent[] bcme = ml.getEvents();
        assertEquals(1, bcme.length);
        assertTrue(bcme[0].contains(proxy));

        // the child recieved a PropertyEvent that is parent was changed
        PropertyChangeEvent[] pce = cl.getEvents();
        assertEquals(1, pce.length);
        assertEquals("beanContext", pce[0].getPropertyName());
        assertNull(pce[0].getNewValue());
        assertEquals(cbcs, pce[0].getOldValue());

        // that the child was removed from the collection
        Object[] children = cbcs.toArray();
        assertEquals(0, children.length);
    }

    /**
     * Implicitly remove a child from the bean context by changing the value of the child's bean context.
     */
    public void testImplicitRemoveSingleChild() {

        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        // add the child
        assertTrue(cbcs.add(child));

        // setup listeners
        ChangeListener cl = new ChangeListener();
        child.addPropertyChangeListener("beanContext", cl);

        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);

        // set the child's bean context (implicitly remove from cbcs)
        try {
            child.setBeanContext(null);
        }
        catch (PropertyVetoException pve) {
            fail("Unexpected PropertyVetoException!!!");
        }

        // verify that....

        // a BeanContextMembershipEvent is fired...
        BeanContextMembershipEvent[] bcme = ml.getEvents();
        assertEquals(1, bcme.length);
        assertTrue(bcme[0].contains(child));

        // the child recieved a PropertyChangeEvent that is parent was changed
        PropertyChangeEvent[] pce = cl.getEvents();
        assertEquals(1, pce.length);
        assertEquals("beanContext", pce[0].getPropertyName());
        assertNull(pce[0].getNewValue());
        assertEquals(cbcs, pce[0].getOldValue());

        // that the child was removed from the collection
        Object[] children = cbcs.toArray();
        assertEquals(0, children.length);
    }

    /**
     * Test an implicit remove of a child from a BeanContext which has a peer.
     */
    public void testImplicitRemoveSingleChild_Peer() {
        BeanContextPeer bcp = new BeanContextPeer();
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = new ControlBeanContextSupport(bcp);
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        // add the child
        assertTrue(cbcs.add(child));

        // setup listeners
        ChangeListener cl = new ChangeListener();
        child.addPropertyChangeListener("beanContext", cl);

        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);

        // set the child's bean context (implicitly remove from cbcs)
        try {
            child.setBeanContext(null);
        }
        catch (PropertyVetoException pve) {
            fail("Unexpected PropertyVetoException!!!");
        }

        // verify that....

        // a BeanContextMembershipEvent is fired...
        BeanContextMembershipEvent[] bcme = ml.getEvents();
        assertEquals(1, bcme.length);
        assertTrue(bcme[0].contains(child));
        assertEquals(bcp, bcme[0].getSource());

        // the child recieved a PropertyChangeEvent that is parent was changed
        PropertyChangeEvent[] pce = cl.getEvents();
        assertEquals(1, pce.length);
        assertEquals("beanContext", pce[0].getPropertyName());
        assertNull(pce[0].getNewValue());
        assertEquals(bcp, pce[0].getOldValue());
        assertEquals(child, pce[0].getSource());

        // that the child was removed from the collection
        Object[] children = cbcs.toArray();
        assertEquals(0, children.length);
    }

    /**
     * Remove a child which is not currently a child of the bean context.
     */
    public void testRemoveNonExistentChild() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        assertFalse(cbcs.remove(child));
    }

    /**
     * Remove a child which veto's its removal from the bean context.
     */
    public void testRemoveChildWhoVetos() {
        ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        // add the child
        assertTrue(cbcs.add(child));

        // setup listeners
        AlwaysVetoListener avl = new AlwaysVetoListener();
        child.addVetoableChangeListener("beanContext", avl);

        // remove the child
        try {
            cbcs.remove(child);
        }
        catch (IllegalStateException ise) {
            return;
        }
        fail("Expected IllegalStateException!!!");
    }

    //
    // BeanContext Api tests
    //

    /*
    *   addBeanContextMembershipListener -- see collection add methods
    */

    /*
     *  removeBeanContextMembershipListener -- see collection remove methods
     */


    /**
     * Test the instantiateChild() BeanContext api.  Should create bean and add
     * to BeanContext.
     */
    public void testInstantiateBean() {
        ControlBeanContextSupport cbcs = getContext();
        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);


        VisibilityImpl child = null;
        try {
            child = (VisibilityImpl) cbcs.instantiateChild("org.apache.beehive.controls.test.controls.beancontext.VisibilityImpl");
        }
        catch (IOException e) {
            fail("Unexpected IOException during instantiateChild() test!!");
        }
        catch (ClassNotFoundException e) {
            fail("Unexpected ClassNotFoundException during instantiateChild() test!!");
        }

        BeanContextMembershipEvent[] evts = ml.getEvents();
        assertEquals(1, evts.length);
        assertTrue(evts[0].contains(child));

        assertEquals(1, cbcs.size());
        assertTrue(cbcs.contains(child));
    }

    /**
     * Test the getResource() BeanContext API.
     */
    public void testGetResource() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        assertTrue(cbcs.add(child));

        URL location = cbcs.getResource("org/apache/beehive/controls/test/controls/beancontext/Resource.txt", child);
        assertNotNull(location);
    }

    /**
     * Test the getResource() BeanContext API, where the child parameter is not a child of the BeanContext.
     */
    public void testGetResourceBadChild() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();
        try {
            cbcs.getResource("org/apache/beehive/controls/test/controls/beancontext/Resource.txt", child);
        }
        catch (IllegalArgumentException iae) {
            return;
        }
        fail("Expected IllegalArgumentException since specified child is not a child of the BeanContext!!");
    }

    /**
     * Test the getResourceAsStream() BeanContext API.
     */
    public void testGetResourceAsStream() throws IOException {
        ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        assertTrue(cbcs.add(child));

        InputStream is = cbcs.getResourceAsStream("org/apache/beehive/controls/test/controls/beancontext/Resource.txt", child);
        assertNotNull(is);
        is.close();
    }

    /**
     * Test the getResourceAsStream() BeanContext API, where the child parameter is not a child of the BeanContext.
     */
    public void testGetResourceAsStreamBadChild() {
        ControlBeanContextSupport cbcs = getContext();
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport child = new ControlBeanContextChildSupport();

        try {
            cbcs.getResourceAsStream("org/apache/beehive/controls/test/controls/beancontext/Resource.txt", child);
        }
        catch (IllegalArgumentException iae) {
            return;
        }
        fail("Expected IllegalArgumentException since specified child is not a child of the BeanContext!!");
    }

    //
    // some simple visibility API tests
    //

    /**
     * Add a child which needGui, verify that it is picked up correctly.
     */
    public void testNeedsGui() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        VisibilityImpl child = new VisibilityImpl();
        child.setNeedsGui(true);

        assertTrue(cbcs.add(child));
        assertTrue(cbcs.needsGui());
    }

    /**
     * Test the negative case of needsGui() Visiblity API.
     */
    public void testNeedsGuiNegative() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        VisibilityImpl child = new VisibilityImpl();
        child.setNeedsGui(false);

        assertTrue(cbcs.add(child));
        assertFalse(cbcs.needsGui());
    }

    /**
     * Test the dontUseGui() Visibility API.
     */
    public void testDontUseGui() {
        ControlBeanContextSupport cbcs = getContext();
        VisibilityImpl child = new VisibilityImpl();

        assertTrue(cbcs.add(child));
        cbcs.dontUseGui();
        assertFalse(child.getCanUseGui());
    }

    /**
     * Test the okToUseGui() Visibility API.
     */
    public void testOkToUseGui() {
        ControlBeanContextSupport cbcs = getContext();
        VisibilityImpl child = new VisibilityImpl();

        assertTrue(cbcs.add(child));
        cbcs.okToUseGui();
        assertTrue(child.getCanUseGui());
    }

    /**
     * Test the avoidingGui Visibility API.
     */
    public void testAvoidingGui() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        VisibilityImpl child = new VisibilityImpl();
        child.setNeedsGui(true);

        assertTrue(cbcs.add(child));
        cbcs.dontUseGui();

        assertTrue(child.avoidingGui());
    }

    //
    // Serialization tests
    //

    /**
     * Serialize/deserializeCBCS an empty bean context.
     */
    public void testSerializationEmptyBeanContext() throws IOException, ClassNotFoundException {
        ControlBeanContextSupport cbcs = getContext();
        cbcs.setDesignTime(true);

        File serFile = serializeCBCS(cbcs, "cbcs_1");
        cbcs = deserializeCBCS(serFile);

        assertTrue(cbcs.isEmpty());
        assertTrue(cbcs.isDesignTime());
    }

    /**
     * Serialize BC with two children, no event listeners.
     */
    public void testSerializationWithChildren() throws IOException, ClassNotFoundException {
        ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child1 = new ControlBeanContextChildSupport();
        ControlBeanContextChildSupport child2 = new ControlBeanContextChildSupport();

        assertTrue(cbcs.add(child1));
        assertTrue(cbcs.add(child2));

        File serFile = serializeCBCS(cbcs, "cbcs_2");
        cbcs = deserializeCBCS(serFile);

        assertFalse(cbcs.isEmpty());
        assertEquals(2, cbcs.size());
    }

    /**
     * Serailize BC which has Peer and two child, children should NOT be serialized.
     */
    public void testSerializationWithChildren_Peer() throws IOException, ClassNotFoundException {
        BeanContextPeer bcp = new BeanContextPeer();
        ControlBeanContextSupport cbcs = new ControlBeanContextSupport(bcp);
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport child1 = new ControlBeanContextChildSupport();
        ControlBeanContextChildSupport child2 = new ControlBeanContextChildSupport();

        assertTrue(cbcs.add(child1));
        assertTrue(cbcs.add(child2));

        File serFile = serializeCBCS(cbcs, "cbcs_2p");
        cbcs = deserializeCBCS(serFile);

        // when peer is present children should not be serialized.
        assertTrue(cbcs.isEmpty());
    }

    /**
     * Serialization test with property change listerner's registered.
     */
    public void testSerializationWithListeners() throws IOException, ClassNotFoundException {
        ControlBeanContextSupport cbcs = getContext();

        ControlBeanContextChildSupport child1 = new ControlBeanContextChildSupport();
        ChangeListener cl1 = new ChangeListener();
        child1.addPropertyChangeListener("beanContext", cl1);

        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextChildSupport child2 = new ControlBeanContextChildSupport();
        ChangeListener cl2 = new ChangeListener();
        child2.addPropertyChangeListener("beanContext", cl2);

        assertTrue(cbcs.add(child1));
        assertTrue(cbcs.add(child2));

        File serFile = serializeCBCS(cbcs, "cbcs_3");
        cbcs = deserializeCBCS(serFile);

        assertFalse(cbcs.isEmpty());
        assertEquals(2, cbcs.size());

        // todo: this is about all the further to test, none of our event listeners
        // can be accessed in their deserialized state (dont have a reference to them)
        // should add additional tests to make sure everything works as expected.
    }

    /**
     * Test serialization with registered BeanContextMembershipListeners
     */
    public void testSerializationWithMembershipListeners() throws IOException, ClassNotFoundException {
        ControlBeanContextSupport cbcs = getContext();
        MembershipListener ml = new MembershipListener();
        cbcs.addBeanContextMembershipListener(ml);

        ControlBeanContextChildSupport child1 = new ControlBeanContextChildSupport();
        assertTrue(cbcs.add(child1));

        File serFile = serializeCBCS(cbcs, "cbcs_4");
        cbcs = deserializeCBCS(serFile);

        assertFalse(cbcs.isEmpty());
        assertEquals(1, cbcs.size());
    }

    /**
     * Test serialization with a child which is not serializable.
     */
    public void testSerializationWithNonSerializableChild() throws IOException, ClassNotFoundException {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport cbcs = getContext();
        ControlBeanContextChildSupport child1 = new ControlBeanContextChildSupport();
        assertTrue(cbcs.add(child1));
        VisibilityImpl child2 = new VisibilityImpl();
        assertTrue(cbcs.add(child2));

        File serFile = serializeCBCS(cbcs, "cbcs_5");
        cbcs = deserializeCBCS(serFile);
        assertFalse(cbcs.isEmpty());

        // cbcs's size should only be 1 since one of its children was not Serializable
        assertEquals(1, cbcs.size());
    }

    //
    // helper methods
    //
    protected org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport getContext() {
        return new ControlBeanContextSupport();
    }

    private File serializeCBCS(ControlBeanContextSupport cbcs, String serFileName) throws IOException {
        File serFile = File.createTempFile(serFileName, "ser");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile));
        oos.writeObject(cbcs);
        oos.close();
        return serFile;
    }

    private org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport deserializeCBCS(File serFile) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
        try {
            return (ControlBeanContextSupport) ois.readObject();
        } finally {
            ois.close();
            serFile.delete();
        }
    }

    public static Test suite() {
        return new TestSuite(ControlBeanContextSupportTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
