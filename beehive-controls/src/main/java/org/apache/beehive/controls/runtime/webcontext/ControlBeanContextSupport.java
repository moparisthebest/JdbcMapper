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

import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.Visibility;
import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextMembershipEvent;
import java.beans.beancontext.BeanContextMembershipListener;
import java.beans.beancontext.BeanContextProxy;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * BeanContext implementation for Beehive Controls.
 */
public class ControlBeanContextSupport extends ControlBeanContextChildSupport
        implements BeanContext, Serializable, PropertyChangeListener, VetoableChangeListener {

    private static final long serialVersionUID = 1L;

    private transient Map<Object, BCChild> _children;
    private transient List<BeanContextMembershipListener> _bcMembershipListeners;

    // change listeners used for children, in an attempt to prevent
    // the unintentional serialization of this bean context by a
    // problematic child.
    private transient PropertyChangeListener _childPcl;
    private transient VetoableChangeListener _childVcl;

    private boolean _designTime = false;
    private boolean _mayUseGui = false;

    /**
     * Constructor.
     */
    public ControlBeanContextSupport() {
        super();
        initialize();
    }

    /**
     * Constructor.
     *
     * @param peer
     */
    public ControlBeanContextSupport(BeanContext peer) {
        super(peer);
        initialize();
    }

    /**
     * Instantiate the javaBean named as a
     * child of this <code>BeanContext</code>.
     * The implementation of the JavaBean is
     * derived from the value of the beanName parameter,
     * and is defined by the
     * <code>java.beans.Beans.instantiate()</code> method.
     *
     * @param beanName The name of the JavaBean to instantiate
     *                 as a child of this <code>BeanContext</code>
     * @throws IOException
     * @throws ClassNotFoundException if the class identified
     *                                by the beanName parameter is not found
     */
    public Object instantiateChild(String beanName) throws IOException, ClassNotFoundException {
        BeanContextChild bcc = getPeer();
        return Beans.instantiate(bcc.getClass().getClassLoader(), beanName, (BeanContext) bcc);
    }

    /**
     * Analagous to <code>java.lang.ClassLoader.getResourceAsStream()</code>,
     * this method allows a <code>BeanContext</code> implementation
     * to interpose behavior between the child <code>Component</code>
     * and underlying <code>ClassLoader</code>.
     *
     * @param name the resource name
     * @param bcc  the specified child
     * @return an <code>InputStream</code> for reading the resource,
     *         or <code>null</code> if the resource could not
     *         be found.
     * @throws IllegalArgumentException if the resource is not valid
     */
    public InputStream getResourceAsStream(String name, BeanContextChild bcc) throws IllegalArgumentException {

        // bcc must be a child of this context
        if (!contains(bcc)) {
            throw new IllegalArgumentException("Child is not a member of this context");
        }

        ClassLoader cl = bcc.getClass().getClassLoader();
        InputStream is;
        if (cl != null && (is = cl.getResourceAsStream(name)) != null) {
            return is;
        }
        return ClassLoader.getSystemResourceAsStream(name);
    }

    /**
     * Analagous to <code>java.lang.ClassLoader.getResource()</code>, this
     * method allows a <code>BeanContext</code> implementation to interpose
     * behavior between the child <code>Component</code>
     * and underlying <code>ClassLoader</code>.
     *
     * @param name the resource name
     * @param bcc  the specified child
     * @return a <code>URL</code> for the named
     *         resource for the specified child
     * @throws IllegalArgumentException if the resource is not valid
     */
    public URL getResource(String name, BeanContextChild bcc) throws IllegalArgumentException {

        // bcc must be a child of this context
        if (!contains(bcc)) {
            throw new IllegalArgumentException("Child is not a member of this context");
        }

        ClassLoader cl = bcc.getClass().getClassLoader();
        URL url;
        if (cl != null && (url = cl.getResource(name)) != null) {
            return url;
        }
        return ClassLoader.getSystemResource(name);
    }

    /**
     * Adds the specified <code>BeanContextMembershipListener</code>
     * to receive <code>BeanContextMembershipEvents</code> from
     * this <code>BeanContext</code> whenever it adds
     * or removes a child <code>Component</code>(s).
     *
     * @param bcml the <code>BeanContextMembershipListener</code> to be added
     */
    public void addBeanContextMembershipListener(BeanContextMembershipListener bcml) {
        _bcMembershipListeners.add(bcml);
    }

    /**
     * Removes the specified <code>BeanContextMembershipListener</code>
     * so that it no longer receives <code>BeanContextMembershipEvent</code>s
     * when the child <code>Component</code>(s) are added or removed.
     *
     * @param bcml the <code>BeanContextMembershipListener</code>
     *             to be removed
     */
    public void removeBeanContextMembershipListener(BeanContextMembershipListener bcml) {
        _bcMembershipListeners.remove(bcml);
    }

    /**
     * Returns the number of children in this BeanContext.  If this BeanContext
     * contains more than <tt>Integer.MAX_VALUE</tt> children, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    public int size() {
        return _children.size();
    }

    /**
     * Returns <tt>true</tt> if this BeanContext has no children.
     */
    public boolean isEmpty() {
        return _children.isEmpty();
    }

    /**
     * Returns true if this BeanContext contains the specified child.
     *
     * @param o element whose presence in this BeanContext is to be tested.
     * @return true if this BeanContext contains the specified child
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this collection (optional).
     * @throws NullPointerException if the specified element is null and this
     *                              collection does not support null elements (optional).
     */
    public boolean contains(Object o) {
        return _children.containsKey(o);
    }

    /**
     * Returns an iterator over the elements in this collection.  The
     * iterator's collection is non-modifiable and element does not
     * correspond to the order that children are added.
     *
     * @return an <tt>Iterator</tt> over the children of this BeanContext
     */
    public Iterator iterator() {
        return Collections.unmodifiableSet(_children.keySet()).iterator();
    }

    /**
     * Returns an array containing all of the children in this BeanContext.
     * <p/>
     * The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.<p>
     * <p/>
     * This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this collection
     */
    public Object[] toArray() {
        return _children.keySet().toArray();
    }

    /**
     * Add a child to this BeanContext.  If the child is already a child
     * of this bean context this method returns immediately with a return
     * value of false.
     * <p/>
     * If the child implements the BeanContextProxy interface, the child
     * AND the BeanContextChild referenced by the proxy are added to this
     * BeanContext.
     *
     * @param o element whose presence in this collection is to be ensured.
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     * @throws UnsupportedOperationException <tt>add</tt> is not supported by
     *                                       this collection.
     * @throws ClassCastException            class of the specified element prevents it
     *                                       from being added to this collection.
     * @throws NullPointerException          if the specified element is null and this
     *                                       collection does not support null elements.
     * @throws IllegalArgumentException      some aspect of this element prevents
     *                                       it from being added to this collection.
     */
    public boolean add(Object o) {
        return internalAdd(o, true);
    }

    /**
     * Remove the specified child from this BeanContext.  If the child
     * to be removed implements the BeanContextProxy interface or is
     * referenced from an existing BeanContextProxy child both children
     * will be removed.
     *
     * @param o element to be removed from this collection, if present.
     * @return <tt>true</tt> if this collection changed as a result of the
     *         call
     * @throws ClassCastException            if the type of the specified element
     *                                       is incompatible with this collection (optional).
     * @throws NullPointerException          if the specified element is null and this
     *                                       collection does not support null elements (optional).
     * @throws UnsupportedOperationException remove is not supported by this
     *                                       collection.
     */
    public boolean remove(Object o) {
        return internalRemove(o, true);
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException
     */
    public boolean addAll(Collection c) {
        // NOOP : Not Supported
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException
     */
    public void clear() {
        // NOOP: Not supported
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException
     */
    public boolean retainAll(Collection c) {
        // NOOP: Not supported
        throw new UnsupportedOperationException();
    }

    /**
     * Not supported.
     *
     * @throws UnsupportedOperationException
     */
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns <tt>true</tt> if this BeanContext contains all of the children
     * in the specified collection.
     *
     * @param c collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements
     *         in the specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible with this
     *                              collection (optional).
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this collection does not support null
     *                              elements (optional).
     * @throws NullPointerException if the specified collection is
     *                              <tt>null</tt>.
     * @see #contains(Object)
     */
    public boolean containsAll(Collection c) {
        return _children.keySet().containsAll(c);
    }

    /**
     * Returns an array containing all of the children of this BeanContext;
     * the runtime type of the returned array is that of the specified array.
     *
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the same
     *          runtime type is allocated for this purpose.
     * @return an array containing the elements of this collection
     * @throws ArrayStoreException  the runtime type of the specified array is
     *                              not a supertype of the runtime type of every element in this
     *                              collection.
     * @throws NullPointerException if the specified array is <tt>null</tt>.
     */
    public Object[] toArray(Object[] a) {
        return _children.keySet().toArray(a);
    }

    /**
     * Sets the "value" of the "designTime" property.
     * <p/>
     * If the implementing object is an instance of java.beans.beancontext.BeanContext,
     * or a subinterface thereof, then that BeanContext should fire a
     * PropertyChangeEvent, to its registered BeanContextMembershipListeners, with
     * parameters:
     * <ul>
     * <li><code>propertyName</code> - <code>java.beans.DesignMode.PROPERTYNAME</code>
     * <li><code>oldValue</code> - previous value of "designTime"
     * <li><code>newValue</code> - current value of "designTime"
     * </ul>
     * Note it is illegal for a BeanContextChild to invoke this method
     * associated with a BeanContext that it is nested within.
     *
     * @param designTime the current "value" of the "designTime" property
     * @see java.beans.beancontext.BeanContext
     * @see java.beans.beancontext.BeanContextMembershipListener
     * @see java.beans.PropertyChangeEvent
     */
    public void setDesignTime(boolean designTime) {
        if (designTime == _designTime) return;
        _designTime = designTime;
        firePropertyChange("designTime", !_designTime, designTime);
    }

    /**
     * A value of true denotes that JavaBeans should behave in design time
     * mode, a value of false denotes runtime behavior.
     *
     * @return the current "value" of the "designTime" property.
     */
    public boolean isDesignTime() {
        return _designTime;
    }

    /**
     * Determines whether this bean needs a GUI.
     *
     * @return True if the bean absolutely needs a GUI available in
     *         order to get its work done.
     */
    public boolean needsGui() {
        BeanContextChild bcc = getPeer();
        if (bcc != this && bcc instanceof Visibility) {
            return ((Visibility) bcc).needsGui();
        }

        // check children
        for (Object o : _children.keySet()) {
            if (o instanceof Visibility) {
                if (((Visibility) o).needsGui()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method instructs the bean that it should not use the Gui.
     */
    public void dontUseGui() {
        if (!_mayUseGui) return;

        _mayUseGui = false;

        for (Object o : _children.keySet()) {
            if (o instanceof Visibility) {
                ((Visibility) o).dontUseGui();
            }
        }
    }

    /**
     * This method instructs the bean that it is OK to use the Gui.
     */
    public void okToUseGui() {
        if (_mayUseGui) return;

        _mayUseGui = true;

        for (Object o : _children.keySet()) {
            if (o instanceof Visibility) {
                ((Visibility) o).okToUseGui();
            }
        }
    }

    /**
     * Determines whether this bean is avoiding using a GUI.
     *
     * @return true if the bean is currently avoiding use of the Gui.
     *         e.g. due to a call on dontUseGui().
     */
    public boolean avoidingGui() {
        return !_mayUseGui && needsGui();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // monitor "beanContext" property
        if ("beanContext".equals(evt.getPropertyName()) && contains(evt.getSource())) {
            BeanContext bc = (BeanContext) getPeer();
            if (bc.equals(evt.getOldValue()) && !bc.equals(evt.getNewValue())) {
                internalRemove(evt.getSource(), false);
            }
        }
    }

    /**
     * This method gets called when a constrained property is changed.
     *
     * @param evt a <code>PropertyChangeEvent</code> object describing the
     *            event source and the property that has changed.
     * @throws java.beans.PropertyVetoException
     *          if the recipient wishes the property
     *          change to be rolled back.
     */
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        // monitor "beanContext" property
        if ("beanContext".equals(evt.getPropertyName())
                && contains(evt.getOldValue())) {
            // noop: at this point doesn't veto
        }
    }

    /**
     * *************************************************************************************
     */


    /**
     * Init this classes data structures.
     */
    protected void initialize() {
        _bcMembershipListeners = new ArrayList<BeanContextMembershipListener>();
        _children = Collections.synchronizedMap(new HashMap<Object, BCChild>());

        _childPcl = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent pce) {
                ControlBeanContextSupport.this.propertyChange(pce);
            }
        };

        _childVcl = new VetoableChangeListener() {
            public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException {
                ControlBeanContextSupport.this.vetoableChange(pce);
            }
        };
    }

    /**
     * Fire a BeanContextMembershipEvent.
     *
     * @param bcme          Event to fire.
     * @param childrenAdded True if add event, false if remove event.
     */
    private void fireMembershipEvent(BeanContextMembershipEvent bcme, boolean childrenAdded) {

        for (BeanContextMembershipListener bcml : _bcMembershipListeners) {
            if (childrenAdded) {
                bcml.childrenAdded(bcme);
            }
            else {
                bcml.childrenRemoved(bcme);
            }
        }
    }

    /**
     * The internalAdd method is used in two different cases. If an add is done
     * through the public add() api, this method is invoked with the publicApi
     * parameter set to true.  During deserialization of children this method
     * is invoked with publicApi set to false.  During deserialization it is
     * not necessary to set Visibility features or re-register as a listener.
     *
     * @param o
     * @param publicApi
     * @return true if added.
     */
    private boolean internalAdd(Object o, boolean publicApi) {

        if (contains(o)) return false;

        // todo: for multithreaded usage this block needs to be synchronized
        // spec: if the object being added implements BeanContextChild or BeanContextProxy
        // need to set the bean context of the object to this bean context.
        BeanContextChild bcc = null;
        BeanContextProxy bcp = null;

        if (o instanceof BeanContextProxy) {
            if (o instanceof BeanContext) {
                throw new IllegalArgumentException("May not implement both BeanContextProxy and BeanContext!!");
            }
            bcp = (BeanContextProxy) o;
            bcc = bcp.getBeanContextProxy();
        }
        else if (o instanceof BeanContextChild) {
            bcc = (BeanContextChild) o;
        }

        if (bcc != null) {
            try {
                bcc.setBeanContext((BeanContext) getPeer());
            }
            catch (PropertyVetoException e) {
                throw new IllegalStateException(e);
            }

            bcc.addPropertyChangeListener("beanContext", _childPcl);
            bcc.addVetoableChangeListener("beanContext", _childVcl);
        }

        if (publicApi) {
            if (o instanceof Visibility) {
                if (_mayUseGui) {
                    ((Visibility) o).okToUseGui();
                }
                else {
                    ((Visibility) o).dontUseGui();
                }
            }
            if (o instanceof BeanContextMembershipListener) {
                addBeanContextMembershipListener((BeanContextMembershipListener) o);
            }
        }

        if (bcp == null) {
            _children.put(o, new BCChild(o));
        }
        else {
            _children.put(bcp, new BCChild(bcp, bcc, true));
            _children.put(bcc, new BCChild(bcp, bcc, false));
        }

        BeanContextMembershipEvent bcme = new BeanContextMembershipEvent((BeanContext) getPeer(), new Object[]{o});
        fireMembershipEvent(bcme, true);
//        }
        return true;
    }

    /**
     * There are two ways a object can be removed from a BeanContext, by either explicitly invoking the
     * remove() api or if the child implements BeanContextChild by calling its setBeanContext() api.
     *
     * @param o
     * @param publicApi
     * @return true if successful
     */
    private boolean internalRemove(Object o, boolean publicApi) {

        if (!contains(o)) return false;

        // todo: for multithreaded usage this block needs to be synchronized
        BeanContextChild bcc = null;
        if (o instanceof BeanContextProxy) {
            bcc = ((BeanContextProxy) o).getBeanContextProxy();
        }
        else if (o instanceof BeanContextChild) {
            bcc = (BeanContextChild) o;
        }

        if (bcc != null) {

            /*
             If remove invoked as a result of the BeanContext receiving an unexpected PropertyChangeEvent
             notification as a result of a 3rd party invoking setBeanContext() then the remove implementation
             shall not invoke setBeanContext(null) on that child as part of the remove() semantics, since
             doing so would overwrite the value previously set by the 3rd party.
            */
            if (publicApi) {

                // remove the property/veto listeners -- we know we want to remove the bean
                // and don't need to be notified if we have initiated the removal
                bcc.removePropertyChangeListener("beanContext", _childPcl);
                bcc.removeVetoableChangeListener("beanContext", _childVcl);

                try {
                    bcc.setBeanContext(null);
                }
                catch (PropertyVetoException e) {
                    // rewire the listeners we removed above then except
                    bcc.addPropertyChangeListener("beanContext", _childPcl);
                    bcc.addVetoableChangeListener("beanContext", _childVcl);
                    throw new IllegalStateException(e);
                }
            }
        }

        if (o instanceof BeanContextMembershipListener) {
            removeBeanContextMembershipListener((BeanContextMembershipListener) o);
        }

        BCChild bc = _children.get(o);
        if (bc.isProxy()) {
            _children.remove(bc.getChild());
        }
        else if (bc.hasProxy()) {
            _children.remove(bc.getProxy());
        }
        _children.remove(o);

        BeanContextMembershipEvent bcme = new BeanContextMembershipEvent((BeanContext) getPeer(), new Object[]{o});
        fireMembershipEvent(bcme, false);
        // end synchronized block
        return true;
    }

    /**
     * Serialize all serializable children (unless this BeanContext has a peer).  Any
     * children which are not serializable not be present upon deserialization. Also
     * serialize any BeanContextMembership listeners which are serializable.
     *
     * @param out ObjectOutputStream to serialize to.
     */
    private synchronized void writeObject(ObjectOutputStream out) throws IOException {

        // todo: for multithreaded usage this block needs to be synchronized
        out.defaultWriteObject();

        // spec: only write children if not using a peer
        if (this.equals(getPeer())) {
            writeChildren(out);
        }
        else {
            out.writeInt(0);
        }

        // write event handlers
        int serializable = 0;
        for (BeanContextMembershipListener listener : _bcMembershipListeners) {
            if (listener instanceof Serializable) serializable++;
        }

        out.writeInt(serializable);
        if (serializable > 0) {
            for (BeanContextMembershipListener listener : _bcMembershipListeners) {
                if (listener instanceof Serializable) {
                    out.writeObject(listener);
                }
            }
        }
        // end synchronized block
    }

    /**
     * Necessary for the case of this bean context having a peer.  The specification
     * states that a bean context which has a peer should not serialize its children,
     * this hook is necessary to allow the peer to serialize children.
     *
     * @param oos ObjectOutputStream
     * @throws IOException
     */
    public final void writeChildren(ObjectOutputStream oos) throws IOException {
        int serializable = 0;
        Set<Map.Entry<Object, BCChild>> bcChildren = _children.entrySet();
        for (Map.Entry<Object, BCChild> entry : bcChildren) {
            if (entry.getValue().isSerializable()) {
                serializable++;
            }
        }

        oos.writeInt(serializable);
        if (serializable > 0) {
            for (Map.Entry<Object, BCChild> bc : bcChildren) {
                if (bc.getValue().isSerializable()) {
                    oos.writeObject(bc.getKey());
                }
            }
        }
    }

    /**
     * Deserialize this an instance of this class, including any children and
     * BeanContextMembershipListeners which were present during serialization and
     * were serializable.
     *
     * @param in ObjectInputStream to deserialize from.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        // todo: for multithreaded usage this block needs to be synchronized
        in.defaultReadObject();
        initialize();

        // only deserialize child if not using a peer
        if (this.equals(getPeer())) {
            readChildren(in);
        }

        int listenerCount = in.readInt();
        for (int i = 0; i < listenerCount; i++) {
            addBeanContextMembershipListener((BeanContextMembershipListener) in.readObject());
        }
        // end synchronized block
    }

    /**
     * This public api is necessary to allow a bean context with a peer to deserialize its children.
     * This api is not part any standard api.
     *
     * @param in ObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public final void readChildren(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int childCount = in.readInt();
        for (int i = 0; i < childCount; i++) {
            internalAdd(in.readObject(), false);
        }
    }

    /**
     * A child of this BeanContext.  This class is used to manage the relationship
     * between a BeanContextProxy and its BeanContextChild.  When a BeanContextProxy
     * is added or removed from this context the BeanContextChild it references must
     * also be added or removed.  This requires that both the BeanContextChild and
     * BeanContextProxy are added/removed to the list of children.  This class
     * is used to map from the proxy -> child and child -> proxy.
     * <p/>
     * The _child field is always guarenteed to be non-null, the proxy field may
     * be null if this child does not have a proxy.
     */
    private final static class BCChild {

        private Object _child;
        private BeanContextProxy _proxy;
        private boolean _isProxy;
        private boolean _serializable;

        /**
         * Construct a new BCChild for a child which is not related to a BeanContextProxy.
         *
         * @param child child to add -- must not be an instance of BeanContextProxy.
         */
        protected BCChild(Object child) {
            assert child != null;
            assert !(child instanceof BeanContextProxy);

            _child = child;
            _proxy = null;
            _isProxy = false;
            _serializable = _child instanceof Serializable;
        }

        /**
         * Construct a new BCChild for a proxy -> child relationship.
         *
         * @param proxy   BeanContextProxy
         * @param child   BeanContextChild
         * @param isProxy true if this will be entered into the child map keyed on the proxy.
         */
        protected BCChild(BeanContextProxy proxy, BeanContextChild child, boolean isProxy) {
            assert child != null;
            assert proxy != null;

            _child = child;
            _proxy = proxy;
            _isProxy = isProxy;
            _serializable = (_isProxy) && _child instanceof Serializable && _proxy instanceof Serializable;
        }

        /**
         * Get the proxy.
         */
        protected BeanContextProxy getProxy() {
            return _proxy;
        }

        /**
         * Get the child.
         */
        protected Object getChild() {
            return _child;
        }

        /**
         * True if a proxy was set for this child.
         */
        protected boolean hasProxy() {
            return _proxy != null;
        }

        /**
         * True if this child was keyed by its proxy in the child map.
         */
        protected boolean isProxy() {
            return _isProxy;
        }

        /**
         * True if this BCChild is serializable.
         */
        protected boolean isSerializable() {
            return _serializable;
        }
    }
}
