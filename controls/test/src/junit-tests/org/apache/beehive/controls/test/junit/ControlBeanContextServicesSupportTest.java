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

import java.beans.PropertyVetoException;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;
import java.util.Iterator;
import java.util.TooManyListenersException;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport;
import org.apache.beehive.controls.test.controls.beancontextservices.BCChild;
import org.apache.beehive.controls.test.controls.beancontextservices.BCChildNoServiceRelease;
import org.apache.beehive.controls.test.controls.beancontextservices.ServiceListener;
import org.apache.beehive.controls.test.controls.beancontextservices.TestBCServiceProviderImpl;
import org.apache.beehive.controls.test.controls.beancontextservices.TestService;
import org.apache.beehive.controls.test.controls.beancontextservices.TestNonSerializableBCServiceProviderImpl;
import org.apache.beehive.controls.test.controls.beancontextservices.TestNonSerializableService;
import org.apache.beehive.controls.test.controls.beancontextservices.NonSerializableServiceListener;
import org.apache.beehive.controls.test.controls.beancontextservices.ServicesBeanPeer;

/**
 */
public class ControlBeanContextServicesSupportTest extends ControlBeanContextSupportTest {

    /**
     * Create a new ControlBeanContextServicesSupport.
     */
    public void testCreateCBCSS() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport _cbcsSupport = new org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport();
        assertNotNull(_cbcsSupport);
    }

    /**
     * Create a new ControlBeanContextServicesSupport instance with a bean delegate.
     */
    public void testCreateCBCSSWithDelegate() {
        ControlBeanContextServicesSupport _cbcsSupport =
                new ControlBeanContextServicesSupport(new ServicesBeanPeer());
        assertNotNull(_cbcsSupport);
    }

    /**
     * Add a service / make sure it was registered.
     */
    public void testAddService() {
        ControlBeanContextServicesSupport cbcss = getContext();
        TestBCServiceProviderImpl sp = new TestBCServiceProviderImpl();
        cbcss.addService(TestService.class, sp);
        assertTrue(cbcss.hasService(TestService.class));
    }

    /**
     * Add a service which already exists in ServicesSupport.
     */
    public void testAddDuplicateService() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        TestBCServiceProviderImpl sp = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, sp));
        assertTrue(cbcss.hasService(TestService.class));

        assertFalse(cbcss.addService(TestService.class, sp));
    }

    /**
     * Add a service with a service listener registered.
     */
    public void testAddServiceWithListener() {
        ControlBeanContextServicesSupport cbcss = getContext();
        ServiceListener sl = new ServiceListener();
        cbcss.addBeanContextServicesListener(sl);

        TestBCServiceProviderImpl sp = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, sp));

        EventObject[] evts = sl.getEvents();
        assertEquals(1, evts.length);
        assertTrue(evts[0] instanceof BeanContextServiceAvailableEvent);
        assertTrue(((BeanContextServiceAvailableEvent) evts[0]).getServiceClass().equals(TestService.class));
        assertTrue(((BeanContextServiceAvailableEvent) evts[0]).getSourceAsBeanContextServices().equals(cbcss));
    }

    /**
     * Add a service to a beancontextservices which has a peer, verify the event source is the peer.
     */
    public void testAddServiceWithListener_Peer() {
        ServicesBeanPeer sbp = new ServicesBeanPeer();
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = new org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport(sbp);
        ServiceListener sl = new ServiceListener();
        cbcss.addBeanContextServicesListener(sl);

        TestBCServiceProviderImpl sp = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, sp));

        EventObject[] evts = sl.getEvents();
        assertEquals(1, evts.length);
        assertTrue(evts[0] instanceof BeanContextServiceAvailableEvent);
        assertTrue(((BeanContextServiceAvailableEvent) evts[0]).getServiceClass().equals(TestService.class));
        assertEquals(sbp, evts[0].getSource());
    }

    /**
     * Verify that a services listener can be removed.
     */
    public void testRemoveServiceListenerTest() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        ServiceListener sl = new ServiceListener();
        cbcss.addBeanContextServicesListener(sl);
        cbcss.removeBeanContextServicesListener(sl);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        EventObject[] evts = sl.getEvents();
        assertEquals(0, evts.length);
    }

    /**
     * Test the revocation of a service which has no references to it.
     */
    public void testRevokeService_NoReferences() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        ServiceListener sl = new ServiceListener();
        cbcss.addBeanContextServicesListener(sl);
        cbcss.revokeService(TestService.class, service1, false);

        // verify that the service was removed and that the Revoked event was fired and looks correct.
        assertFalse(cbcss.hasService(TestService.class));

        EventObject[] evts = sl.getEvents();
        assertEquals(1, evts.length);
        assertTrue(evts[0] instanceof BeanContextServiceRevokedEvent);
        assertTrue(((BeanContextServiceRevokedEvent) evts[0]).getSourceAsBeanContextServices().equals(cbcss));
        assertFalse(((BeanContextServiceRevokedEvent) evts[0]).isCurrentServiceInvalidNow());
        assertTrue(((BeanContextServiceRevokedEvent) evts[0]).isServiceClass(TestService.class));
    }

    /**
     * Test the revocation of a service when the when the cbcss has a peer.
     */
    public void testRevokeService_NoReferences_Peer() {
        ServicesBeanPeer sbp = new ServicesBeanPeer();
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = new org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport(sbp);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        ServiceListener sl = new ServiceListener();
        cbcss.addBeanContextServicesListener(sl);
        cbcss.revokeService(TestService.class, service1, false);

        // verify that the service was removed and that the Revoked event was fired and looks correct.
        assertFalse(cbcss.hasService(TestService.class));

        EventObject[] evts = sl.getEvents();
        assertEquals(1, evts.length);
        assertTrue(evts[0] instanceof BeanContextServiceRevokedEvent);
        assertTrue(((BeanContextServiceRevokedEvent) evts[0]).getSourceAsBeanContextServices().equals(sbp));
        assertFalse(((BeanContextServiceRevokedEvent) evts[0]).isCurrentServiceInvalidNow());
    }

    /**
     * Test the immediate revocation of a service which has no references to it.
     */
    public void testRevokeServiceNOW_NoReferences() {
        ControlBeanContextServicesSupport cbcss = getContext();

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        ServiceListener sl = new ServiceListener();
        cbcss.addBeanContextServicesListener(sl);
        cbcss.revokeService(TestService.class, service1, true);

        // verify that the service was removed and that the Revoked event was fired and looks correct.
        assertFalse(cbcss.hasService(TestService.class));

        EventObject[] evts = sl.getEvents();
        assertEquals(1, evts.length);
        assertTrue(evts[0] instanceof BeanContextServiceRevokedEvent);
        assertTrue(((BeanContextServiceRevokedEvent) evts[0]).getSourceAsBeanContextServices().equals(cbcss));
        assertTrue(((BeanContextServiceRevokedEvent) evts[0]).isCurrentServiceInvalidNow());
        assertTrue(((BeanContextServiceRevokedEvent) evts[0]).isServiceClass(TestService.class));
    }

    /**
     * Test iterator over service classes.
     */
    public void testGetCurrentServiceClasses() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        Iterator i = cbcss.getCurrentServiceClasses();
        assertTrue(i.hasNext());
        assertEquals(TestService.class, i.next());
        assertFalse(i.hasNext());
    }

    /**
     * Test iterator over service classes, where the service has been revoked,
     * expected behavior in this case is not to include service class in iterator.
     */
    public void testGetCurrentServiceClasses_RevokedService() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));
        cbcss.revokeService(TestService.class, service1, true);

        Iterator i = cbcss.getCurrentServiceClasses();
        assertFalse(i.hasNext());
    }

    /**
     * Test for the hasService() for api test completness.
     */
    public void testHasService() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));
        assertTrue(cbcss.hasService(TestService.class));
    }

    /**
     * Test for getCurrentServicesSelectors() api.
     */
    public void testGetCurrentServiceSelectors() {
        ControlBeanContextServicesSupport cbcss = getContext();
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));
        Iterator i = cbcss.getCurrentServiceSelectors(TestService.class);
        assertEquals("ONE", i.next());
        assertEquals("TWO", i.next());
        assertEquals("THREE", i.next());
        assertFalse(i.hasNext());
    }

    //
    // Service References
    //

    /**
     * Attempt to get a service with an invalid child.
     */
    public void testGetService_IllegalChild() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        try {
            cbcss.getService(cbcss, this, TestService.class, null, null);
        }
        catch (IllegalArgumentException iae) {
            return;
        }
        catch (TooManyListenersException e) {
            fail("UnExpected TooManyListenersException thrown!! " + e);
        }
        fail("Expected IllegalArgumentException to be throw!!");
    }

    /**
     * Add a child, the child is registered as a listener, when the
     * TestService service is registered the child should get a reference
     * to it.
     */
    public void testGetService() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        BCChild child = new BCChild();
        assertTrue(cbcss.add(child));
        cbcss.addBeanContextServicesListener(child);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        // test the following....
        // the child obtained a reference to the service
        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());
    }

    /**
     * Verify that the release of a service worked.
     */
    public void testReleaseService() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        BCChild child = new BCChild();
        assertTrue(cbcss.add(child));
        cbcss.addBeanContextServicesListener(child);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());

        // revoke the service -- triggers release call from child
        cbcss.revokeService(TestService.class, service1, false);
        assertEquals(0, child.getRefCount());
        assertEquals(0, service1.getRefCount());

        // since no references should exist this call will return false
        assertFalse(cbcss.hasService(TestService.class));
    }

    /**
     * Revoke a service which has a reference which is not released on the revoke.
     */
    public void testRevokeOnReferencedService() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        BCChildNoServiceRelease child = new BCChildNoServiceRelease();
        assertTrue(cbcss.add(child));
        cbcss.addBeanContextServicesListener(child);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());

        // revoke the service -- triggers release call from child
        cbcss.revokeService(TestService.class, service1, false);
        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());
        assertFalse(cbcss.hasService(TestService.class));
    }

    /**
     * Forcibly revoke a service which has a reference,
     * verify that it no longer exists.
     */
    public void testForceRevokeOnReferencedService() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        BCChildNoServiceRelease child = new BCChildNoServiceRelease();
        assertTrue(cbcss.add(child));
        cbcss.addBeanContextServicesListener(child);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());

        // remove the listener so the child doesn't get two revoked events
        cbcss.removeBeanContextServicesListener(child);
        cbcss.revokeService(TestService.class, service1, true);
        assertEquals(0, child.getRefCount());

        // service ref count is not updated since this is a forced
        // revocation it is just removed from the cbcss
        assertEquals(1, service1.getRefCount());
        assertFalse(cbcss.hasService(TestService.class));
    }

    /**
     * Create a cbcss which has a child cbcss which contains a bean context child
     * who invokes getService() when a new service is registered with th parent context.
     */
    public void testGetDelegatedService() {

        // create the parent
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport parent_cbcss = getContext();

        // create a child cbcss
        ControlBeanContextServicesSupport child_cbcss = getContext();
        assertTrue(parent_cbcss.add(child_cbcss));

        // add a bcchild which will reqest the service as soon as its added to the top-most parent
        BCChild child = new BCChild();
        assertTrue(child_cbcss.add(child));

        parent_cbcss.addBeanContextServicesListener(child);

        // add the service
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(parent_cbcss.addService(TestService.class, service1));

        // verify the service has been aquired by the child
        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());

        // verify the expected behavior in the child cbcss
        assertTrue(child_cbcss.hasService(TestService.class));
    }

    /**
     * Building on the previous test, aquire the service then release it.
     */
    public void testRevokeDelegateService() {

        // create the parent
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport parent_cbcss = getContext();

        // create a child cbcss
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport child_cbcss = getContext();
        assertTrue(parent_cbcss.add(child_cbcss));

        // add a bcchild which will reqest the service as soon as its added to the top-most parent
        BCChild child = new BCChild();
        assertTrue(child_cbcss.add(child));

        parent_cbcss.addBeanContextServicesListener(child);

        // add the service
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(parent_cbcss.addService(TestService.class, service1));

        // remove the listener so we don't get invoked twice to remove
        // the service
        parent_cbcss.removeBeanContextServicesListener(child);

        // revoke the service, this will cause the child holding a reference
        // to the service to release it.
        parent_cbcss.revokeService(TestService.class, service1, false);

        // verify the service has been released by the child
        assertEquals(0, child.getRefCount());
        assertEquals(0, service1.getRefCount());

        // verify the expected behavior in the child cbcss
        assertFalse(child_cbcss.hasService(TestService.class));
    }

    /**
     * Test the forcible revocation of a delegated service.
     */
    public void testForcedRevokeOfDelegatedService() {

        // create the parent
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport parent_cbcss = getContext();

        // create a child cbcss
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport child_cbcss = getContext();
        assertTrue(parent_cbcss.add(child_cbcss));

        // add a bcchild which will reqest the service as soon as its added to the top-most parent
        BCChildNoServiceRelease child = new BCChildNoServiceRelease();
        assertTrue(child_cbcss.add(child));

        parent_cbcss.addBeanContextServicesListener(child);

        // add the service
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(parent_cbcss.addService(TestService.class, service1));

        // remove the listener so we don't get invoked twice to remove
        // the service
        parent_cbcss.removeBeanContextServicesListener(child);

        // revoke the service, this will cause the child holding a reference
        // to the service to release it.
        parent_cbcss.revokeService(TestService.class, service1, true);

        // verify the service has been released by the child
        assertEquals(0, child.getRefCount());

        // service ref is not decremented in this case since the service gets dereferenced
        assertEquals(1, service1.getRefCount());

        // verify the expected behavior in the child cbcss
        assertFalse(child_cbcss.hasService(TestService.class));
    }

    /**
     * Cause a service to be implicitly revoked (for a child) by changing the parent
     * of a child which contains a reference to that service.
     */
    public void testImplicitServiceRevoke() {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();

        // add a bcchild which will reqest the service as soon as its added
        BCChild child = new BCChild();
        assertTrue(cbcss.add(child));
        cbcss.addBeanContextServicesListener(child);

        // add the service
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());
        cbcss.removeBeanContextServicesListener(child);

        try {
            child.setBeanContext(null);
        }
        catch (PropertyVetoException e) {
            fail("Unexpected ProperyVetoException!!");
        }

        assertEquals(0, child.getRefCount());
        assertEquals(0, service1.getRefCount());
        assertTrue(cbcss.hasService(TestService.class));
    }
    
    /**
     * Test reparenting a cbcss which is a service provider to a child that
     * has a reference to the service.  The expected behavior here is that
     * the child should still have a reference to the service since the
     * service was not a delegated service.
     */
    public void testReparentChild() {
        // create the parent
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport parent_cbcss = getContext();

        // create a child cbcss
        ControlBeanContextServicesSupport child_cbcss = getContext();
        assertTrue(parent_cbcss.add(child_cbcss));

        // add a bcchild which will reqest the service as soon as its added to the top-most parent
        BCChild child = new BCChild();
        assertTrue(child_cbcss.add(child));

        child_cbcss.addBeanContextServicesListener(child);

        // add the service
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(child_cbcss.addService(TestService.class, service1));

        // remove the listener
        child_cbcss.removeBeanContextServicesListener(child);
        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());

        try {
            child_cbcss.setBeanContext(null);
        }
        catch (PropertyVetoException e) {
            fail("Unexpected ProperyVetoException!!!");
        }

        // verify that....

        // the service is still available from the re-parented cbcss
        assertTrue(child_cbcss.hasService(TestService.class));

        // the child has been notified of the revocation
        assertEquals(1, child.getRefCount());

        // the service's refcount should still be the same
        assertEquals(1, service1.getRefCount());
    }

    /**
     * Cause a delegated service to be implicitly revoked (for child which implements the
     * BeanContextServices API) by changing the parent of the child which contains a reference
     * to that service.
     */
    public void testImplicitDelegatedServiceRevoke() {

        // create the parent
        ControlBeanContextServicesSupport parent_cbcss = getContext();

        // create a child cbcss
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport child_cbcss = getContext();
        assertTrue(parent_cbcss.add(child_cbcss));

        // add a bcchild which will reqest the service as soon as its added to the top-most parent
        BCChild child = new BCChild();
        assertTrue(child_cbcss.add(child));

        parent_cbcss.addBeanContextServicesListener(child);

        // add the service
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(parent_cbcss.addService(TestService.class, service1));

        // remove the listener so we don't get invoked twice to remove
        // the service
        parent_cbcss.removeBeanContextServicesListener(child);
        assertEquals(1, child.getRefCount());
        assertEquals(1, service1.getRefCount());

        try {
            child_cbcss.setBeanContext(null);
        }
        catch (PropertyVetoException e) {
            fail("Unexpected ProperyVetoException!!!");
        }

        // verify that....

        // the service is no longer available from the re-parented cbcss
        assertFalse(child_cbcss.hasService(TestService.class));

        // the child has been notified of the revocation
        assertEquals(0, child.getRefCount());

        // the service's refcount does not get decremented since
        // this was an immediate revoke (seems odd but spec says it should work this way)
        assertEquals(1, service1.getRefCount());
    }

    //
    // Serialization tests
    //

    /**
     * Test the serialization of an empty ControlBeanContextServicesSupport.
     */
    public void testSerializationEmptyBCS() throws IOException, ClassNotFoundException {

        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        cbcss.setDesignTime(true);

        File serFile = serializeCBCSS(cbcss, "cbcss_1");
        cbcss = deserializeCBCSS(serFile);
        assertTrue(cbcss.isEmpty());
        assertTrue(cbcss.isDesignTime());
    }

    /**
     * Test Serialization with a service which is serializable.
     */
    public void testSerializationWithService() throws IOException, ClassNotFoundException {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        File serFile = serializeCBCSS(cbcss, "cbcss_2");
        cbcss = deserializeCBCSS(serFile);
        assertTrue(cbcss.hasService(TestService.class));
    }

    /**
     * Test serialization with a BeanContextServicesListener registered.
     */
    public void testSerializationWithListeners() throws IOException, ClassNotFoundException {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        ServiceListener sl = new ServiceListener();
        cbcss.addBeanContextServicesListener(sl);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        File serFile = serializeCBCSS(cbcss, "cbcss_3");
        cbcss = deserializeCBCSS(serFile);

        assertTrue(cbcss.hasService(TestService.class));
        // todo: it would be nice to be able to verify the listener's state
        // the current implementation does not notify listeners that a service
        // as been deserialized.
    }

    /**
     * Test serialization with a non-serializable BeanContextServicesListener registered.
     */
    public void testSerializationWithNonSerializableListeners() throws IOException, ClassNotFoundException {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        NonSerializableServiceListener sl = new NonSerializableServiceListener();
        cbcss.addBeanContextServicesListener(sl);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        File serFile = serializeCBCSS(cbcss, "cbcss_3");
        cbcss = deserializeCBCSS(serFile);

        assertTrue(cbcss.hasService(TestService.class));
        // todo: it would be nice to be able to verify the listener's state
        // the current implementation does not notify listeners that a service
    }

    /**
     * Test serailization of the CBCSS when a non-serializable service is added.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void testSerializationWithNonSerializableService() throws IOException, ClassNotFoundException {
        ControlBeanContextServicesSupport cbcss = getContext();
        TestNonSerializableBCServiceProviderImpl service1 = new TestNonSerializableBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestNonSerializableService.class, service1));

        File serFile = serializeCBCSS(cbcss, "cbcss_4");
        cbcss = deserializeCBCSS(serFile);
        assertFalse(cbcss.hasService(TestNonSerializableService.class));
    }

    /**
     *  Test serialization with a child that has a reference to a service.
     */
    public void testSerialziationWithChildReference() throws IOException, ClassNotFoundException {
        org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport cbcss = getContext();
        BCChild child = new BCChild();
        assertTrue(cbcss.add(child));
        cbcss.addBeanContextServicesListener(child);

        TestBCServiceProviderImpl service1 = new TestBCServiceProviderImpl();
        assertTrue(cbcss.addService(TestService.class, service1));

        // the child obtained a reference to the service
        assertEquals(1, child.getRefCount());
        File serFile = serializeCBCSS(cbcss, "cbcss_5");
        cbcss = deserializeCBCSS(serFile);

        // test that....

        // the service is available after serialization
        assertTrue(cbcss.hasService(TestService.class));

        // the child doesn't contian a reference to the service
        // it is the child's responsibility to release any service references it may
        // have before it is serialized.
        Object[] children = cbcss.toArray();
        assertEquals(1, children.length);
        assertEquals(0, ((BCChild)children[0]).getRefCount());
    }

    //
    // helpers
    //

    protected org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport getContext() {
        return new ControlBeanContextServicesSupport();
    }

    /**
     * Serialize a ControlBeanContextServicesSupport instance.
     *
     * @param cbcss       Object to serialize.
     * @param serFileName Name of file to serialize to.
     * @return Serialization file.
     * @throws IOException
     */
    private File serializeCBCSS(ControlBeanContextServicesSupport cbcss, String serFileName) throws IOException {
        File serFile = File.createTempFile(serFileName, "ser");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile));
        oos.writeObject(cbcss);
        oos.close();
        return serFile;
    }

    /**
     * Deserialize a ControlBeanContextServicesSupport instance.
     *
     * @param serFile File to deserialize from.
     * @return New instance.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private ControlBeanContextServicesSupport deserializeCBCSS(File serFile) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
        try {
            return (org.apache.beehive.controls.runtime.webcontext.ControlBeanContextServicesSupport) ois.readObject();
        } finally {
            ois.close();
            serFile.delete();
        }
    }

    public static Test suite() {
        return new TestSuite(ControlBeanContextServicesSupportTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ControlBeanContextServicesSupportTest.suite());
    }
}
