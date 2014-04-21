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

package org.apache.beehive.controls.runtime.webcontext;

import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.beans.beancontext.BeanContextServiceRevokedListener;
import java.beans.beancontext.BeanContextServices;
import java.beans.beancontext.BeanContextServicesListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;

/**
 * Implementation of BeanContextServices for Beehive Controls.  Assumes single threaded usage.
 */
public class ControlBeanContextServicesSupport extends ControlBeanContextSupport implements BeanContextServices {

    private transient Map<Class/*service class*/, ServiceProvider> _serviceProviders;
    private transient List<BeanContextServicesListener> _bcsListeners;

    /**
     * Constructor.
     */
    public ControlBeanContextServicesSupport() {
        super();
    }

    /**
     * Constructor which allows delegate to be passed in.
     *
     * @param peer BeanContextServices peer.
     */
    public ControlBeanContextServicesSupport(BeanContextServices peer) {
        super(peer);
    }

    /**
     * Adds a service to this BeanContext.
     * <code>BeanContextServiceProvider</code>s call this method
     * to register a particular service with this context.
     * If the service has not previously been added, the
     * <code>BeanContextServices</code> associates
     * the service with the <code>BeanContextServiceProvider</code> and
     * fires a <code>BeanContextServiceAvailableEvent</code> to all
     * currently registered <code>BeanContextServicesListeners</code>.
     * The method then returns <code>true</code>, indicating that
     * the addition of the service was successful.
     * If the given service has already been added, this method
     * simply returns <code>false</code>.
     *
     * @param serviceClass    the service to add
     * @param serviceProvider the <code>BeanContextServiceProvider</code>
     *                        associated with the service
     * @return true if service was added.
     */
    public boolean addService(Class serviceClass, BeanContextServiceProvider serviceProvider) {
        // todo: for multithreaded usage this block needs to be synchronized
        if (!_serviceProviders.containsKey(serviceClass)) {
            _serviceProviders.put(serviceClass, new ServiceProvider(serviceProvider));
            BeanContextServiceAvailableEvent bcsae = new BeanContextServiceAvailableEvent((BeanContextServices)getPeer(), serviceClass);
            fireServiceAvailableEvent(bcsae);
            return true;
        }
        // end synchronized
        return false;
    }

    /**
     * BeanContextServiceProviders wishing to remove
     * a currently registered service from this context
     * may do so via invocation of this method. Upon revocation of
     * the service, the <code>BeanContextServices</code> fires a
     * <code>BeanContextServiceRevokedEvent</code> to its
     * list of currently registered
     * <code>BeanContextServiceRevokedListeners</code> and
     * <code>BeanContextServicesListeners</code>.
     *
     * @param serviceClass             the service to revoke from this BeanContextServices
     * @param serviceProvider          the BeanContextServiceProvider associated with
     *                                 this particular service that is being revoked
     * @param revokeCurrentServicesNow a value of <code>true</code>
     *                                 indicates an exceptional circumstance where the
     *                                 <code>BeanContextServiceProvider</code> or
     *                                 <code>BeanContextServices</code> wishes to immediately
     *                                 terminate service to all currently outstanding references
     *                                 to the specified service.
     */
    public void revokeService(Class serviceClass, BeanContextServiceProvider serviceProvider, boolean revokeCurrentServicesNow) {
        // todo: for multithreaded usage this block needs to be synchronized
        if (!_serviceProviders.containsKey(serviceClass)) {
            return;
        }

        // propagate to any children implementing BeanContextServices
        Iterator i = iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof BeanContextServices) {
                ((BeanContextServices) o).revokeService(serviceClass, serviceProvider, revokeCurrentServicesNow);
            }
        }

        BeanContextServiceRevokedEvent bcsre =
                new BeanContextServiceRevokedEvent((BeanContextServices)getPeer(), serviceClass, revokeCurrentServicesNow);
        fireServiceRevokedEvent(bcsre);

        // fire revoked event to requestor listeners, if the service is delegated the owner of the
        // service should fire these events.
        ServiceProvider sp = _serviceProviders.get(serviceClass);
        if (!sp.isDelegated()) {
            sp.revoke(bcsre);
        }

        if (revokeCurrentServicesNow || !sp.hasRequestors()) {
            _serviceProviders.remove(serviceClass);
        }
        // end synchronized
    }

    /**
     * Reports whether or not a given service is
     * currently available from this context.
     *
     * @param serviceClass the service in question
     * @return true if the service is available
     */
    public synchronized boolean hasService(Class serviceClass) {

        // todo: for multithreaded usage this block needs to be synchronized
        ServiceProvider sp = _serviceProviders.get(serviceClass);
        if (sp != null && !sp.isRevoked()) {
            return true;
        }

        // if service not found locally check in nested context
        BeanContext bc = getBeanContext();
        if (bc instanceof BeanContextServices) {
            return ((BeanContextServices) bc).hasService(serviceClass);
        }
        return false;
        // end synchronized
    }

    /**
     * A <code>BeanContextChild</code>, or any arbitrary object
     * associated with a <code>BeanContextChild</code>, may obtain
     * a reference to a currently registered service from its
     * nesting <code>BeanContextServices</code>
     * via invocation of this method. When invoked, this method
     * gets the service by calling the getService() method on the
     * underlying <code>BeanContextServiceProvider</code>.
     *
     * @param child           the <code>BeanContextChild</code>
     *                        associated with this request
     * @param requestor       the object requesting the service
     * @param serviceClass    class of the requested service
     * @param serviceSelector the service dependent parameter
     * @param bcsrl           the
     *                        <code>BeanContextServiceRevokedListener</code> to notify
     *                        if the service should later become revoked
     * @return a reference to this context's named
     *         Service as requested or <code>null</code>
     * @throws java.util.TooManyListenersException
     *
     */
    public Object getService(BeanContextChild child, Object requestor,
                             Class serviceClass, Object serviceSelector, BeanContextServiceRevokedListener bcsrl)
            throws TooManyListenersException {

        if (!contains(child)) {
            throw new IllegalArgumentException(child + "is not a child of this context!");
        }

        Object service;
        BeanContext bc = getBeanContext();

        // todo: for multithreaded usage this block needs to be synchronized
        // check to see if this is a known service
        ServiceProvider sp = _serviceProviders.get(serviceClass);
        if (sp != null) {
            if (sp.isRevoked()) {
                return null;
            }
            else if (sp.isDelegated()) {
                service = ((BeanContextServices) bc).getService(getPeer(), requestor, serviceClass, serviceSelector, bcsrl);
            }
            else {
                service = sp.getBCServiceProvider().getService((BeanContextServices)getPeer(), requestor, serviceClass, serviceSelector);
            }

            if (service != null) {
                sp.addChildReference(requestor, bcsrl);
            }
            return service;
        }

        // unknown service provider, delegate the request to the nested BeanContextServices (if available)
        if (bc instanceof BeanContextServices) {
            service = ((BeanContextServices) bc).getService(getPeer(), requestor, serviceClass, serviceSelector, bcsrl);
            if (service != null) {
                sp = new ServiceProvider();
                sp.addChildReference(requestor, bcsrl);
                _serviceProviders.put(serviceClass, sp);
            }
            return service;
        }
        return null;
    }

    /**
     * Releases a <code>BeanContextChild</code>'s
     * (or any arbitrary object associated with a BeanContextChild)
     * reference to the specified service by calling releaseService()
     * on the underlying <code>BeanContextServiceProvider</code>.
     *
     * @param child     the <code>BeanContextChild</code>
     * @param requestor the requestor
     * @param service   the service
     */
    public void releaseService(BeanContextChild child, Object requestor, Object service) {

        if (!contains(child)) {
            throw new IllegalArgumentException(child + "is not a child of this context!");
        }

        // todo: for multithreaded usage this block needs to be synchronized
        Class serviceClass = findServiceClass(service);
        ServiceProvider sp = _serviceProviders.get(serviceClass);
        sp.removeChildReference(requestor);

        // if this is a delegated service, delegate the release request
        // delegated services are removed from the _serviceProviders table
        // as soon as their reference count drops to zero
        if (sp.isDelegated()) {
            BeanContextServices bcs = (BeanContextServices) getBeanContext();
            bcs.releaseService(this, requestor, service);
            if (!sp.hasRequestors()) {
                _serviceProviders.remove(serviceClass);
            }
        }
        else {
            sp.getBCServiceProvider().releaseService((BeanContextServices)getPeer(), requestor, service);
        }
        // end synchronized
    }

    /**
     * Gets the currently available services for this context.
     *
     * @return an <code>Iterator</code> consisting of the
     *         currently available services
     */
    public Iterator getCurrentServiceClasses() {

        ArrayList<Class> currentClasses = new ArrayList<Class>();
        for (Class serviceClass : _serviceProviders.keySet()) {
            ServiceProvider sp = _serviceProviders.get(serviceClass);
            if (!sp.isRevoked() && !sp.isDelegated()) {
                currentClasses.add(serviceClass);
            }
        }
        return currentClasses.iterator();
    }

    /**
     * Gets the list of service dependent service parameters
     * (Service Selectors) for the specified service, by
     * calling getCurrentServiceSelectors() on the
     * underlying BeanContextServiceProvider.
     *
     * @param serviceClass the specified service
     * @return the currently available service selectors
     *         for the named serviceClass
     */
    public Iterator getCurrentServiceSelectors(Class serviceClass) {

        if (_serviceProviders.containsKey(serviceClass)) {
            ServiceProvider sp = _serviceProviders.get(serviceClass);
            if (!sp.isDelegated() && !sp.isRevoked()) {
                return sp.getBCServiceProvider().getCurrentServiceSelectors((BeanContextServices)getPeer(), serviceClass);
            }
        }
        return null;
    }

    /**
     * Adds a <code>BeanContextServicesListener</code> to this BeanContext.
     *
     * @param bcsl the <code>BeanContextServicesListener</code> to add
     */
    public void addBeanContextServicesListener(BeanContextServicesListener bcsl) {
        if (!_bcsListeners.contains(bcsl)) {
            _bcsListeners.add(bcsl);
        }
    }

    /**
     * Removes a <code>BeanContextServicesListener</code>
     * from this <code>BeanContext</code>.
     *
     * @param bcsl the <code>BeanContextServicesListener</code>
     *             to remove from this context
     */
    public void removeBeanContextServicesListener(BeanContextServicesListener bcsl) {
        _bcsListeners.remove(bcsl);
    }

    /*
     * **********************************************************************************
     *                          Protected
     * **********************************************************************************
     */

    /**
     * Invoked when all resources obtained from the current nested bean context
     * need to be released. For BeanContextServices this means revoke any services
     * obtained from a delegate services provider. Typically invoked when the parent
     * context is changed.
     */
    protected synchronized void releaseBeanContextResources() {

        for (Class serviceClass : _serviceProviders.keySet()) {
            ServiceProvider sp = _serviceProviders.get(serviceClass);
            if (sp.isDelegated()) {
                sp.revoke(new BeanContextServiceRevokedEvent((BeanContextServices)getPeer(), serviceClass, true));
            }
        }
    }

    /*
     * **********************************************************************************
     *                              Private
     * **********************************************************************************
     */

    /**
     * first a service available event.
     *
     * @param bcsae
     */
    private void fireServiceAvailableEvent(BeanContextServiceAvailableEvent bcsae) {
        for (BeanContextServicesListener bcsl : _bcsListeners) {
            bcsl.serviceAvailable(bcsae);
        }
    }

    /**
     * Fire a service revoked event.
     *
     * @param bcsre
     */
    private void fireServiceRevokedEvent(BeanContextServiceRevokedEvent bcsre) {
        for (BeanContextServicesListener bcsl : _bcsListeners) {
            bcsl.serviceRevoked(bcsre);
        }
    }

    /**
     * Initialize data structures.
     */
    protected void initialize() {
        super.initialize();
        _serviceProviders = Collections.synchronizedMap(new HashMap<Class, ServiceProvider>());
        _bcsListeners = Collections.synchronizedList(new ArrayList<BeanContextServicesListener>());
    }

    /**
     * Try to find the registered service for a service object instance.
     * May return null if the object instance is not from a service registered
     * with this service provider.
     *
     * @return Service class for service instance.
     * @throws IllegalArgumentException if service class can not be found.
     */
    private Class findServiceClass(Object service) {
        for (Class sc : _serviceProviders.keySet()) {
            if (sc.isInstance(service)) {
                return sc;
            }
        }
        throw new IllegalArgumentException("Cannot find service provider for: " + service.getClass().getCanonicalName());
    }

    /**
     * Deserialization support.
     *
     * @param ois
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private synchronized void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        int svcsSize = ois.readInt();
        for (int i = 0; i < svcsSize; i++) {
            _serviceProviders.put((Class) ois.readObject(), (ServiceProvider) ois.readObject());
        }

        int listenersSize = ois.readInt();
        for (int i = 0; i < listenersSize; i++) {
            _bcsListeners.add((BeanContextServicesListener) ois.readObject());
        }
    }

    /**
     * Serialize this instance including any serializable services and BeanContextServicesListeners.
     * Any services or listeners which are not Serializable will not be present once deserialized.
     *
     * @param oos
     * @throws IOException
     */
    private synchronized void writeObject(ObjectOutputStream oos) throws IOException {

        int serializable = 0;
        oos.defaultWriteObject();

        // write out the service providers
        Set<Map.Entry<Class, ServiceProvider>> providers = _serviceProviders.entrySet();
        for (Map.Entry<Class, ServiceProvider> provider : providers) {
            if (provider.getValue().isSerializable()) {
                serializable++;
            }
        }

        oos.writeInt(serializable);
        if (serializable > 0) {
            for (Map.Entry<Class, ServiceProvider> entry : providers) {
                if (entry.getValue().isSerializable()) {
                    oos.writeObject(entry.getKey());
                    oos.writeObject(entry.getValue());
                }
            }
        }

        // write out the event listeners
        serializable = 0;
        for (BeanContextServicesListener l : _bcsListeners) {
            if (l instanceof Serializable) {
                serializable++;
            }
        }

        oos.writeInt(serializable);
        if (serializable > 0) {
            for (BeanContextServicesListener l : _bcsListeners) {
                if (l instanceof Serializable) {
                    oos.writeObject(l);
                }
            }
        }
    }

    /*
    * ***************************************************************************************
    *          Inner Classes
    * ***************************************************************************************
    */

    /**
     * A ServiceProvider instance is associated with a service class in a Map.  One ServiceProvider
     * exists for a given service type.  The Service manager keeps track of all of the requestors
     * for the given service.  It provides functionality to add new references to services, remove
     * references to services, and to notify referenants that a service has been revoked.
     */
    private static final class ServiceProvider implements Serializable {

        private transient HashMap<Object/*requestor*/, ChildServiceReference> _requestors;
        private BeanContextServiceProvider _bcss;
        private boolean _revoked;
        private final boolean _serializable;

        /**
         * Constructor for delegated service provider.
         */
        private ServiceProvider() {
            _bcss = null;
            _serializable = false;
        }

        /**
         * Constructor for local service provider.
         */
        private ServiceProvider(BeanContextServiceProvider bcss) {
            _bcss = bcss;
            _serializable = _bcss instanceof Serializable;
        }

        /**
         * Add a child reference to this service provider.
         *
         * @param requestor
         * @param bcsrl
         * @throws TooManyListenersException
         */
        private synchronized void addChildReference(Object requestor, BeanContextServiceRevokedListener bcsrl)
                throws TooManyListenersException {

            ChildServiceReference csr = getRequestors().get(requestor);
            if (csr == null) {
                csr = new ChildServiceReference(bcsrl);
                getRequestors().put(requestor, csr);
                return;
            }

            if (bcsrl != null && !bcsrl.equals(csr.getListener())) {
                throw new TooManyListenersException();
            }
            csr.incrementRefCount();
        }

        /**
         * Remove a child reference from this Service provider.
         *
         * @param requestor
         */
        private synchronized void removeChildReference(Object requestor) {
            ChildServiceReference csr = getRequestors().get(requestor);
            if (csr == null) return;
            int refCount = csr.decrementRefCount();
            if (refCount <= 0) {
                getRequestors().remove(requestor);
            }
        }

        /**
         * Notify all the active requestors for this service that the service
         * has been revoked (per spec).
         */
        private synchronized void revoke(BeanContextServiceRevokedEvent bcsre) {
                for (ChildServiceReference csr : getRequestors().values()) {
                    csr.getListener().serviceRevoked(bcsre);
                }
            _revoked = true;
        }

        /**
         * Get the BeanContextServiceProvider.
         */
        private BeanContextServiceProvider getBCServiceProvider() {
            return _bcss;
        }

        /**
         * True if this is a delegated service.
         */
        private boolean isDelegated() {
            return _bcss == null;
        }

        /**
         * True if this service has been revoked, but still has references.
         */
        private boolean isRevoked() {
            return _revoked;
        }

        /**
         * Can this service provider be serialized?
         */
        private boolean isSerializable() {
            return _serializable && !_revoked && !isDelegated();
        }

        /**
         * True if any requestors are being tracked for this service.
         */
        private boolean hasRequestors() {
            return !getRequestors().isEmpty();
        }

        /**
         * Get a reference to the transient requestors hashmap.
         */
        private synchronized HashMap<Object, ChildServiceReference> getRequestors() {
            if (_requestors == null) {
                _requestors = new HashMap<Object, ChildServiceReference>();
            }
            return _requestors;
        }
    }

    /**
     * Keeps track of the number of references to a service for a single requestor.
     * Associated with the requestor in the ServiceProvider's _requestors Map.
     */
    private static final class ChildServiceReference {

        private int _refCount;
        private BeanContextServiceRevokedListener _bcsrl;

        private ChildServiceReference(BeanContextServiceRevokedListener bcsrl) {
            _bcsrl = bcsrl;
            _refCount = 1;
        }

        private int incrementRefCount() {
            return ++_refCount;
        }

        private int decrementRefCount() {
            return --_refCount;
        }

        private BeanContextServiceRevokedListener getListener() {
            return _bcsrl;
        }
    }
}
