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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.beans.beancontext.BeanContextServiceRevokedListener;
import java.beans.beancontext.BeanContextServicesListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.util.EventListener;

/**
 * Implementation of the BeanContextChild api for Beehive controls.
 */
public class ControlBeanContextChildSupport
        implements BeanContextChild, BeanContextServiceRevokedListener,
        BeanContextServicesListener, Serializable, EventListener {
    private static final long serialVersionUID = 1;

    private transient BeanContext _beanContext;
    private transient boolean _vetodOnce;
    private BeanContextChild _peer;
    private PropertyChangeSupport _propertyChangeSupport;
    private VetoableChangeSupport _vetoableChangeSupport;

    /**
     * Constructor.
     */
    public ControlBeanContextChildSupport() {
        _beanContext = null;
        _peer = this;
        _vetodOnce = false;
        _propertyChangeSupport = new PropertyChangeSupport(_peer);
        _vetoableChangeSupport = new VetoableChangeSupport(_peer);
    }

    /**
     * Constructor -- java bean implements BeanContextChild and delegates the interface
     * to this implementation.
     *
     * @param bcc
     */
    public ControlBeanContextChildSupport(BeanContextChild bcc) {
        _beanContext = null;
        _vetodOnce = false;
        _peer = (bcc != null) ? bcc : this;
        _propertyChangeSupport = new PropertyChangeSupport(_peer);
        _vetoableChangeSupport = new VetoableChangeSupport(_peer);
    }

    /**
     * <p/>
     * Objects that implement this interface,
     * shall fire a java.beans.PropertyChangeEvent, with parameters:
     * <p/>
     * propertyName "beanContext", oldValue (the previous nesting
     * <code>BeanContext</code> instance, or <code>null</code>),
     * newValue (the current nesting
     * <code>BeanContext</code> instance, or <code>null</code>).
     * <p/>
     * A change in the value of the nesting BeanContext property of this
     * BeanContextChild may be vetoed by throwing the appropriate exception.
     * </p>
     *
     * @param bc The <code>BeanContext</code> with which
     *           to associate this <code>BeanContextChild</code>.
     */
    public synchronized void setBeanContext(BeanContext bc) throws PropertyVetoException {

        if (bc == _beanContext) return;

        // track if veto'd the first time, then then second time remove anyway (dont except);
        if (!_vetodOnce) {
            try {
                _vetoableChangeSupport.fireVetoableChange("beanContext", _beanContext, bc);
            }
            catch (PropertyVetoException pve) {
                _vetodOnce = true;
                throw pve;
            }
        }

        releaseBeanContextResources();
        BeanContext oldValue = _beanContext;
        _beanContext = bc;
        _vetodOnce = false;
        firePropertyChange("beanContext", oldValue, _beanContext);
    }

    /**
     * Gets the <code>BeanContext</code> associated
     * with this <code>BeanContextChild</code>.
     *
     * @return the <code>BeanContext</code> associated
     *         with this <code>BeanContextChild</code>.
     */
    public synchronized BeanContext getBeanContext() {
        return _beanContext;
    }

    /**
     * Adds a <code>PropertyChangeListener</code>
     * to this <code>BeanContextChild</code>
     * in order to receive a <code>PropertyChangeEvent</code>
     * whenever the specified property has changed.
     *
     * @param name the name of the property to listen on
     * @param pcl  the <code>PropertyChangeListener</code> to add
     */
    public void addPropertyChangeListener(String name, PropertyChangeListener pcl) {
        _propertyChangeSupport.addPropertyChangeListener(name, pcl);
    }

    /**
     * Removes a <code>PropertyChangeListener</code> from this
     * <code>BeanContextChild</code>  so that it no longer
     * receives <code>PropertyChangeEvents</code> when the
     * specified property is changed.
     *
     * @param name the name of the property that was listened on
     * @param pcl  the <code>PropertyChangeListener</code> to remove
     */
    public void removePropertyChangeListener(String name, PropertyChangeListener pcl) {
        _propertyChangeSupport.removePropertyChangeListener(name, pcl);
    }

    /**
     * Adds a <code>VetoableChangeListener</code> to
     * this <code>BeanContextChild</code>
     * to receive events whenever the specified property changes.
     *
     * @param name the name of the property to listen on
     * @param vcl  the <code>VetoableChangeListener</code> to add
     */
    public void addVetoableChangeListener(String name, VetoableChangeListener vcl) {
        _vetoableChangeSupport.addVetoableChangeListener(name, vcl);
    }

    /**
     * Removes a <code>VetoableChangeListener</code> from this
     * <code>BeanContextChild</code> so that it no longer receives
     * events when the specified property changes.
     *
     * @param name the name of the property that was listened on.
     * @param vcl  the <code>VetoableChangeListener</code> to remove.
     */
    public void removeVetoableChangeListener(String name, VetoableChangeListener vcl) {
        _vetoableChangeSupport.addVetoableChangeListener(name, vcl);
    }

    /**
     * The service named has been registered. getService requests for
     * this service may now be made.
     *
     * @param bcsae the <code>BeanContextServiceAvailableEvent</code>
     */
    public void serviceAvailable(BeanContextServiceAvailableEvent bcsae) {
        // noop
    }

    /**
     * The service named has been revoked. getService requests for
     * this service will no longer be satisifed.
     *
     * @param bcsre the <code>BeanContextServiceRevokedEvent</code> received
     *              by this listener.
     */
    public void serviceRevoked(BeanContextServiceRevokedEvent bcsre) {
        // Noop
    }

    /**
     * ***************************************************************
     */

    /**
     * Get the delegate for this child.
     */
    protected BeanContextChild getPeer() {
        return _peer;
    }

    /**
     * Fire a property change event.
     * @param name
     * @param oldValue
     * @param newValue
     */
    protected void firePropertyChange(String name, Object oldValue, Object newValue) {
        _propertyChangeSupport.firePropertyChange(name, oldValue, newValue);
    }

    /**
     * Release any resources that may have been acumlated from the current bean context, invoked
     * by setBeanContext BEFORE the context is changed.
     */
    protected void releaseBeanContextResources() {
        // noop
    }

    /**
     * Serialization support -- throw IOException if a non-serializable delegate is present.
     *
     * @param out
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        if (!_peer.equals(this) && !(_peer instanceof Serializable)) {
            throw new IOException("Bean context child delegate does not support serialization!!!");
        }
        out.defaultWriteObject();
    }

    /**
     * Deserialization support -- just deserialize.
     *
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
    }
}
