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
package org.apache.beehive.controls.runtime.bean;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.beans.beancontext.BeanContextServiceProvider;
import java.beans.beancontext.BeanContextServices;
import java.beans.beancontext.BeanContextChild;
import java.beans.beancontext.BeanContextServiceRevokedListener;
import java.beans.beancontext.BeanContextServicesListener;
import java.beans.beancontext.BeanContextMembershipListener;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;
import java.util.Vector;
import java.util.Collection;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.URL;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.context.ControlHandle;
import org.apache.beehive.controls.api.properties.AnnotatedElementMap;
import org.apache.beehive.controls.api.properties.BeanPropertyMap;
import org.apache.beehive.controls.api.properties.PropertyMap;
import org.apache.beehive.controls.api.properties.PropertySet;
import org.apache.beehive.controls.api.properties.PropertySetProxy;

/**
 * The ControlBeanContext implements the basic BeanContextServices implementation
 * for ControlBeans.
 *
 * It provides several basic functions:
 *  - it defines the generic services that are available for all control containers
 *  - it acts as the base class for other container service implementations
 *  - it acts as the BeanContextServicesRevokedListener when an associated control
 *    bean has lost access to services.
 */
public class ControlBeanContext
    implements org.apache.beehive.controls.api.context.ControlBeanContext,
    java.beans.PropertyChangeListener,
    java.beans.VetoableChangeListener,
    java.io.Serializable
{
    /**
     * Creates a new ControlBeanContext instance associated with a specific control bean.  If the
     * <code>ControlBean</code> is null, this ControlBeanContext object represents a top-level Control
     * container.  This constructor uses the default implementation of the
     * {@link java.beans.beancontext.BeanContextServices} interface from the JDK.
     *
     * @param bean The control bean that contains/scopes the ControlBeanContext.  If null, it means the
     *        ControlBeanContext is for a top-level container.
     */
    protected ControlBeanContext(ControlBean bean)
    {
        this(bean, DEFAULT_BEAN_CONTEXT_SERVICES_FACTORY);
    }

    /**
     * Creates a new ControlBeanContext instance associated with a specific control bean.  If the
     * <code>ControlBean</code> is null, this ControlBeanContext object represents a top-level Control
     * container.  This constructor uses the <code>beanContextServicesDelegate</code> instance as the
     * implementation of the {@link java.beans.beancontext.BeanContextServices} interface.
     *
     * @param bean The control bean
     * @param beanContextServicesFactory A factory that can be used to create the BeanContextServicesFactory object
     *        that implements support for the {@link BeanContextServices} interface.
     */
    protected ControlBeanContext(ControlBean bean, BeanContextServicesFactory beanContextServicesFactory) {
        super();

        _bean = bean;

        // ensure that there is a valid factory for creating the BCS delegate
        if(beanContextServicesFactory == null)
            beanContextServicesFactory = DEFAULT_BEAN_CONTEXT_SERVICES_FACTORY;

        _beanContextServicesDelegate = beanContextServicesFactory.instantiate(this);
        initialize();
    }

    /**
     * Called by BeanContextSupport superclass during construction and deserialization to
     * initialize subclass transient state
     */
    public void initialize()
    {
        //
        // Register the ControlBeanContext provider on all new context instances.
        //
        addService(org.apache.beehive.controls.api.context.ControlBeanContext.class, CONTROL_BEAN_CONTEXT_PROVIDER);
    }

    /**
     * Implements the
     * {@link java.beans.beancontext.BeanContextServiceRevokedListener#serviceRevoked(java.beans.beancontext.BeanContextServiceRevokedEvent)}
     * method.  This is called if the associated {@link ControlBean} has requested a service that is being subsequently
     * revoked.
     */
    public void serviceRevoked(BeanContextServiceRevokedEvent bcsre)
    {
        //
        // This can happen, if the control is disassociated from a parent context that is
        // providing services.
        //
    }

    /**
     * Overrides the {@link java.beans.beancontext.BeanContextChild#setBeanContext(java.beans.beancontext.BeanContext)}
     * method.  This hook is used to perform additional processing that needs to occur when the control is associated
     * with a new nesting context.
     */
    public synchronized void setBeanContext(BeanContext beanContext)
        throws PropertyVetoException
    {
        ControlBeanContext cbcs = null;

        if (beanContext != null)
        {
            //
            // ControlBeans can only be nested in context service instances that derive
            // from ControlBeanContext.
            //
            if (!(beanContext instanceof ControlBeanContext))
            {
                PropertyChangeEvent pce = new PropertyChangeEvent(_bean, "beanContext", getBeanContext(), beanContext);
                throw new PropertyVetoException("Context does not support nesting controls: " +
                                                beanContext.getClass(), pce);
            }

            cbcs = (ControlBeanContext)beanContext;
        }


        _beanContextServicesDelegate.setBeanContext(beanContext);

        resetControlID();

        _hasSingleThreadedParent = cbcs != null ? cbcs.isSingleThreadedContainer() : false;

        //
        // Notify the bean that its context (container) has been set.
        //
        if (_bean != null)
            _bean.setBeanContext(beanContext);
    }

    /**
     * The NameGenerator class is a simple helper class that creates new unique names based
     * upon a base prefix and an incrementing counter
     */
    private static class NameGenerator implements java.io.Serializable
    {
        NameGenerator(String namePrefix)
        {
            _namePrefix = namePrefix;
        }

        /**
         * Get the next unique name
         */
        public synchronized String next()
        {
            return _namePrefix + _nextIndex++;
        }

        int _nextIndex = 0;
        String _namePrefix;
    }

    /**
     * Returns a new NameGenerator instance based upon a particular naming
     * prefix.
     */
    private NameGenerator getNameGenerator(String namePrefix)
    {
        synchronized(this)
        {
            if (_nameGenerators == null)
                _nameGenerators = new HashMap<String,NameGenerator>();

            NameGenerator nameGenerator = _nameGenerators.get(namePrefix);
            if (nameGenerator == null)
            {
                nameGenerator = new NameGenerator(namePrefix);
                _nameGenerators.put(namePrefix, nameGenerator);
            }
            return nameGenerator;
        }
    }

    /**
     * Generates a new unique control ID for an instance of the target class
     */
    public String generateUniqueID(Class clazz)
    {
        String namePrefix = clazz.getName();
        int dotIndex = namePrefix.lastIndexOf('.');
        if (dotIndex > 0)
            namePrefix = namePrefix.substring(dotIndex+1);
        NameGenerator nameGenerator = getNameGenerator(namePrefix);
        return nameGenerator.next();
    }

    /**
     * Overrides the BeanContextSupport.add() method to perform additional validation
     * that is unique for ControlBeans containers.
     */
    public boolean add(Object targetChild)
    {
        //
        // The context can contain ControlBeans and other types of objects, such as a control
        // factory.
        //
        String beanID = null;
        if (targetChild instanceof ControlBean)
        {
            ControlBean bean = (ControlBean)targetChild;
            beanID = bean.getLocalID();

            //
            // The bean is anonymous, so we must generate a new unique name within this context.
            //
            if (beanID == null)
            {
                beanID = generateUniqueID(bean.getClass());
                bean.setLocalID(beanID);
            }

            ControlBean existingBean = (ControlBean)_childMap.get(beanID);
            if (existingBean != null && existingBean != targetChild)
            {
                throw new IllegalArgumentException("Attempting to add control with duplicate ID: " +
                                                   beanID);
            }
        }

        boolean added = _beanContextServicesDelegate.add(targetChild);
        if (added && beanID != null)
            _childMap.put(beanID, targetChild);

        return added;
    }

    /**
     * Overrides the BeanContextSupport.remove() method to perform additional post-processing
     * on child removal.
     */
    public boolean remove(Object targetChild)
    {
        assert targetChild instanceof ControlBean;  // should be guaranteed above
        boolean removed = _beanContextServicesDelegate.remove(targetChild);

        if (removed)
        {
            //
            // Remove from the locally maintained child map
            //
            String localID = ((ControlBean)targetChild).getLocalID();
            Object removedChild = _childMap.remove(localID);
            assert removedChild == targetChild;     // just being safe
        }
        return removed;
    }

    /**
     * Returns a ControlBean instance nested the current BeanContext.
     * @param id the identifier for the target control, relative to the current
     *           context.
     */
    public ControlBean getBean(String id)
    {
        // If no control id separator found, the bean is a direct child of this context
        int delim = id.indexOf(org.apache.beehive.controls.api.bean.ControlBean.IDSeparator);
        if (delim < 0)  // child is a direct descendent
            return (ControlBean)_childMap.get(id);

        // Find the child referenced by the first element in the path
        ControlBean bean = (ControlBean)_childMap.get(id.substring(0, delim));
        if (bean == null)
            return bean;

        // Get the BeanContext associated with the found child, and then ask it
        // to resolve the rest of the path
        return bean.getBeanContextProxy().getBean(id.substring(delim+1));
    }

    /**
     * Returns the ControlBean associated directly with the ControlBeanContext.  If the
     * context represents a top-level container, will return null.
     */
    public ControlBean getControlBean()
    {
        return _bean;
    }

    public synchronized boolean hasSingleThreadedParent()
    {
        return _hasSingleThreadedParent;
    }

    /**
     *  Returns true if this container associated with this context service enforces
     *  single-threaded invocation, false otherwise.
     *
     *  This MUST be overridden by container-specific subclasses in order to change
     * the default behavior.  If a single-threaded container intends to guarantee
     *  single-threaded behavior (such as the EJB container), this should return true.
     */
    public synchronized boolean isSingleThreadedContainer()
    {
        return ( hasSingleThreadedParent() || ( _bean != null && _bean.hasSingleThreadedImpl() ));
    }

    /**
     * The initializeControl method is invoked when the implementation instance associated
     * with the context has been instantiated and initialized.
     */
    public void initializeControl()
    {
        //
        // Deliver the onCreate event to any register lifecycle listeners
        //
        if (_lifeCycleListeners != null)
        {
            for (LifeCycle lifeCycleListener : _lifeCycleListeners) {
                lifeCycleListener.onCreate();
            }
        }
    }

    /**
     * Returns the PropertyMap containing default properties for an AnnotatedElement
     * in the current context.  The initialization of PropertyMap binding is always
     * done by delegating to a {@link ControlContainerContext}, enabling containers to implement
     * property binding mechanisms (such as external config) that may override the values
     * contained within the element annotations.
     */
    public PropertyMap getAnnotationMap(AnnotatedElement annotElem)
    {
        ControlBeanContext beanContext = this;
        while (beanContext != null)
        {
            // REVIEW: should ControlContainerContext-derived classes override getBeanAnnotationMap?  Not sure
            // that name makes sense, and perhaps it shouldn't take a ControlBean.
            if (beanContext instanceof ControlContainerContext)
                return beanContext.getBeanAnnotationMap(_bean, annotElem);
            beanContext = (ControlBeanContext)beanContext.getBeanContext();
        }

        // No ControlContainerContext was found, so just use the default implementation
        return getBeanAnnotationMap(_bean, annotElem);
    }

    /**
     * The default implementation of getBeanAnnotationMap.  This returns a map based purely
     * upon annotation reflection
     */
    protected PropertyMap getBeanAnnotationMap(ControlBean bean, AnnotatedElement annotElem)
    {
        PropertyMap map = new AnnotatedElementMap(annotElem);

        // REVIEW: is this the right place to handle the general control client case?
        if ( bean != null )
            setDelegateMap( map, bean, annotElem );

        return map;
    }

    static protected void setDelegateMap( PropertyMap map, ControlBean bean, AnnotatedElement annotElem )
    {
        //
        // If building an annotation map for a method or field, we want to delegate back
        // to the base control type.
        //
        Class annotClass = null;
        if (annotElem instanceof Field)
        {
           annotClass = ((Field)annotElem).getType();
        }
        else if (annotElem instanceof Method)
        {
           annotClass = bean.getControlInterface();
        }

        if (annotClass != null)
        {
            PropertyMap delegateMap = bean.getAnnotationMap(annotClass);
            map.setDelegateMap(delegateMap);
        }
    }

    //
    // ControlBeanContext.getControlInterface
    //
    public Class getControlInterface()
    {
        return _bean.getControlInterface();
    }

    //
    // ControlBeanContext.getControlPropertySet
    //
    public <T extends Annotation> T getControlPropertySet(Class<T> propertySet)
    {
        PropertyMap map = _bean.getPropertyMap();

        //
        // Optional properties are not exposed to clients using traditional JavaBean
        // setters/getters (because there is way to represent an 'unset' value); for
        // these properties, the impl can tell if the PropertySet is unset because
        // this method will return null.
        //
        if (!map.containsPropertySet(propertySet))
        {
            PropertySet psAnnot = propertySet.getAnnotation(PropertySet.class);
            if (psAnnot.optional())
                return null;
        }

        //
        // Construct a new PropertySet proxy instance that derives its values from
        // the bean property map.
        //
        return PropertySetProxy.getProxy(propertySet, map);
    }

    //
    // ControlBeanContext.getMethodPropertySet
    //
    public <T extends Annotation> T getMethodPropertySet(Method m, Class<T> propertySet)
    {
        PropertyMap map = _bean.getAnnotationMap(m);

        //
        // Optional properties are not exposed to clients using traditional JavaBean
        // setters/getters (because there is way to represent an 'unset' value); for
        // these properties, the impl can tell if the PropertySet is unset because
        // this method will return null.
        //
        if (!map.containsPropertySet(propertySet))
        {
            PropertySet psAnnot = propertySet.getAnnotation(PropertySet.class);
            if (psAnnot.optional())
                return null;
        }

        //
        // Construct a new PropertySet proxy instance that derives its values from
        // the method property map.
        //
        return PropertySetProxy.getProxy(propertySet, _bean.getAnnotationMap(m));
    }

    //
    // ControlBeanContext.getParameterPropertySet
    //
    public <T extends Annotation> T getParameterPropertySet(Method m, int i, Class<T> propertySet)
        throws IllegalArgumentException, IndexOutOfBoundsException
    {
        if (i >= m.getParameterTypes().length)
            throw new IndexOutOfBoundsException("Invalid parameter index for method:" + m);

        // todo: Currently, there is no external override mechanism for method parameters
        Annotation [] paramAnnots = m.getParameterAnnotations()[i];
        for (Annotation paramAnnot : paramAnnots)
            if (propertySet.isAssignableFrom(paramAnnot.getClass()))
                return (T) paramAnnot;

        return null;
    }

    //
    // ControlBeanContext.getParameterNames
    //
    public String [] getParameterNames(Method m)
        throws IllegalArgumentException
    {
        return _bean.getParameterNames(m);
    }

    //
    // ControlBeanContext.getNamedParameterValue
    //
    public Object getParameterValue(Method m, String parameterName, Object [] parameters)
        throws IllegalArgumentException
    {
        String [] names = getParameterNames(m);

        // Validate the input parameter array
        if (parameters.length != names.length)
            throw new IllegalArgumentException("Expected " + names.length + " parameters," +
                                               "Found " + parameters.length);

        // Finding the index of the matching parameter name
        int i = 0;
        while (i < names.length)
        {
            if (names[i].equals(parameterName))
                break;
            i++;
        }
        if (i == names.length)
            throw new IllegalArgumentException("No method parameter with name: " + parameterName);

        // Return the parameter value at the matched index
        return parameters[i];
    }

    //
    // ControlBeanContext.getPropertyMap
    //
    public PropertyMap getControlPropertyMap()
    {
        //
        // Return a wrapped copy of the original bean property map, so any edits
        // don't impact the bean properties.
        //
        return new BeanPropertyMap(_bean.getPropertyMap());
    }

    //
    // ControlBeanContext.getService
    //
    public <T> T getService(Class<T> serviceClass, Object selector)
    {
        //
        // If the requested service is a ControlBeanContext instance, the current instance
        // can be returned.
        //
        if (serviceClass.equals(org.apache.beehive.controls.api.context.ControlBeanContext.class))
            return (T)this;

        //
        // The parent BeanContext is responsible for providing requested services.  If
        // no parent context is available or it is does not manage services, then no service.
        //
        BeanContext bc = getBeanContext();
        if (bc == null || !(bc instanceof BeanContextServices))
            return null;

        //
        // Call getService on the parent context, using this bean as the requestor and the
        // this context as the child context and ServicesRevoked event listener parameters.
        //
        try
        {
            return  (T)((BeanContextServices)bc).getService(this, _bean, serviceClass, selector, this);
        }
        catch (TooManyListenersException tmle)
        {
            // This would be highly unusual... it implies that the registration for service
            // revocation notifications failed for some reason.
            throw new ControlException("Unable to register for service events", tmle);
        }
    }

    //
    // ControlBeanContext.getControlHandle
    //
    public ControlHandle getControlHandle()
    {
        //
        // Find the associated ControlContainerContext, which provides a container-specific
        // implementation of ControlHandle
        //
        ControlBeanContext beanContext = this;
        while (beanContext != null && !(beanContext instanceof ControlContainerContext))
            beanContext = (ControlBeanContext)beanContext.getBeanContext();

        if (beanContext == null)
            return null;

        //
        // Ask the container for a ControlHandle instance referencing the target bean
        //
        return ((ControlContainerContext)beanContext).getControlHandle(_bean);
    }

    //
    // ControlBeanContext.getClassLoader
    //
    public java.lang.ClassLoader getClassLoader()
    {
        return getControlInterface().getClassLoader();
    }

    //
    // ControlBeanContext.addLifeCycleListener
    //
    synchronized public void addLifeCycleListener(LifeCycle listener)
    {
        if (_lifeCycleListeners == null)
        {
            _lifeCycleListeners = new Vector<LifeCycle>();

            //
            // Since bound/constrained property changes are exposed as lifecycle events, we
            // need to register ourselves as a listener for these events the first time a
            // lifecycle listener is added.
            //
            _bean.getPropertyChangeSupport().addPropertyChangeListener(this);
            _bean.getVetoableChangeSupport().addVetoableChangeListener(this);
        }
        _lifeCycleListeners.addElement(listener);
    }

    //
    // ControlBeanContext.removeLifeCycleListener
    //
    synchronized public void removeLifeCycleListener(LifeCycle listener)
    {
        if (_lifeCycleListeners != null)
            _lifeCycleListeners.removeElement(listener);
    }

    //
    // PropertyChangeListener.propertyChange
    //
    public void propertyChange(PropertyChangeEvent pce)
    {
        if (_lifeCycleListeners != null)
        {
            for (LifeCycle lifeCycleListener : _lifeCycleListeners) {
                lifeCycleListener.onPropertyChange(pce);
            }
        }
    }

    //
    // VetoableChangeListener.vetoableChange
    //
    public void vetoableChange(PropertyChangeEvent pce) throws PropertyVetoException
    {
        if (_lifeCycleListeners != null)
        {
            for (LifeCycle lifeCycleListener : _lifeCycleListeners) {
                lifeCycleListener.onVetoableChange(pce);
            }
        }
    }

    /* package */ String getControlID()
    {
        if (_controlID != null || _bean == null)
            return _controlID;

        // Initially set to the local beans relative ID
        String id = _bean.getLocalID();

        // If there is a parent context, prepend its ID and the ID separator
        BeanContext bc = getBeanContext();
        if (bc != null && bc instanceof ControlBeanContext)
        {
            String parentID = ((ControlBeanContext)bc).getControlID();
            if (parentID != null)
            {
                id = parentID +
                     org.apache.beehive.controls.api.bean.ControlBean.IDSeparator +
                     id;
            }
        }

        // Cache the computed value
        _controlID = id;

        return id;
    }

    /**
     * Resets the composite control ID for this context and all children beneath it.  This
     * can be used to invalidate cached values when necessary (for example, when a context
     * is reparented).
     */
    private void resetControlID()
    {
        _controlID = null;
        for (Object child : this) {
            if (child instanceof ControlBeanContext)
                ((ControlBeanContext) child).resetControlID();
        }
    }

    //
    // BeanContextServices.getCurrentServiceClasses
    // Override the default implementation of getCurrentServiceClasses inherited from
    // java.beans.beancontext.BeanContextServicesSuppport.  The reason for this is a bug/
    // flaw in its underlying implementation.  It does not include any services exposed
    // by any nesting contexts.  This is contradictory to the implementation of hasService()
    // and getService() which do delegate up to a parent context to find services if not
    // available on the local context.  This means hasService() could return 'true' for a
    // service that isn't returned by getCurrentServiceClasses(), which seems like a bug.
    //
    synchronized public Iterator getCurrentServiceClasses()
    {
        Set classSet = new HashSet();
        BeanContextServices bcs = _beanContextServicesDelegate;

        while (bcs != null)
        {
            Iterator iter = bcs.getCurrentServiceClasses();
            while (iter.hasNext())
                classSet.add(iter.next());

            // Go up to the parent, if it is a service provider as well
            BeanContext bc = getBeanContext();
            if (bc instanceof BeanContextServices && bcs != bc)
                bcs = (BeanContextServices)bc;
            else
                bcs = null;
        }
        return classSet.iterator();
    }

    //
    // BeanContextServices.getCurrentServiceSelectors
    // Override getCurrentServiceSelectors for the same reason as above
    //
    public Iterator getCurrentServiceSelectors(Class serviceClass)
    {
        if (hasService(serviceClass))
            return _beanContextServicesDelegate.getCurrentServiceSelectors(serviceClass);

        BeanContext bc = getBeanContext();
        if (bc instanceof BeanContextServices)
            return ((BeanContextServices)bc).getCurrentServiceSelectors(serviceClass);

        return null;
    }

    private synchronized void writeObject(ObjectOutputStream oos)
        throws IOException {
        oos.defaultWriteObject();
        if(_beanContextServicesDelegate instanceof java.beans.beancontext.BeanContextSupport) {
            ((java.beans.beancontext.BeanContextSupport)_beanContextServicesDelegate).writeChildren(oos);
        } else if(_beanContextServicesDelegate instanceof org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport) {
            ((org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport)_beanContextServicesDelegate).writeChildren(oos);
        }
        else assert false;
    }

    private synchronized void readObject(ObjectInputStream ois)
        throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        if(_beanContextServicesDelegate instanceof java.beans.beancontext.BeanContextSupport) {
            ((java.beans.beancontext.BeanContextSupport)_beanContextServicesDelegate).readChildren(ois);
        } else if(_beanContextServicesDelegate instanceof org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport) {
            ((org.apache.beehive.controls.runtime.webcontext.ControlBeanContextSupport)_beanContextServicesDelegate).readChildren(ois);
        }
        else assert false;

        // Re-initialize a deserialized control hierarchy.
        initialize();
    }

    protected BeanContextServicesFactory getBeanContextServicesFactory() {
        return DEFAULT_BEAN_CONTEXT_SERVICES_FACTORY;
    }

    public boolean equals(Object o) {
        /* todo: make sure this logic is right / sufficient */
        if (this == o)
            return true;

        if(!(o instanceof org.apache.beehive.controls.api.context.ControlBeanContext))
            return false;

        return o instanceof ControlBeanContext &&
            _beanContextServicesDelegate.equals(((ControlBeanContext)o)._beanContextServicesDelegate);
    }

    public int hashCode() {
        /* todo: make sure this logic is right / sufficient */
        int result;
        result = (_bean != null ? _bean.hashCode() : 0);
        result = 31 * result + (_beanContextServicesDelegate != null ? _beanContextServicesDelegate.hashCode() : 0);
        return result;
    }

    /* --------------------------------------------------------------------------

       Implementation of java.beans.beancontext.BeanContextServices

       -------------------------------------------------------------------------- */
    public boolean addService(Class serviceClass, BeanContextServiceProvider serviceProvider) {
        return _beanContextServicesDelegate.addService(serviceClass, serviceProvider);
    }

    public void revokeService(Class serviceClass, BeanContextServiceProvider serviceProvider, boolean revokeCurrentServicesNow) {
        _beanContextServicesDelegate.revokeService(serviceClass, serviceProvider, revokeCurrentServicesNow);
    }

    public boolean hasService(Class serviceClass) {
        return _beanContextServicesDelegate.hasService(serviceClass);
    }

    public Object getService(BeanContextChild child, Object requestor, Class serviceClass, Object serviceSelector, BeanContextServiceRevokedListener bcsrl) throws TooManyListenersException {
        return _beanContextServicesDelegate.getService(child, requestor, serviceClass, serviceSelector, bcsrl);
    }

    public void releaseService(BeanContextChild child, Object requestor, Object service) {
        _beanContextServicesDelegate.releaseService(child, requestor, service);
    }

    public void addBeanContextServicesListener(BeanContextServicesListener bcsl) {
        _beanContextServicesDelegate.addBeanContextServicesListener(bcsl);
    }

    public void removeBeanContextServicesListener(BeanContextServicesListener bcsl) {
        _beanContextServicesDelegate.removeBeanContextServicesListener(bcsl);
    }

    public Object instantiateChild(String beanName) throws IOException, ClassNotFoundException {
        return _beanContextServicesDelegate.instantiateChild(beanName);
    }

    public InputStream getResourceAsStream(String name, BeanContextChild bcc) throws IllegalArgumentException {
        return _beanContextServicesDelegate.getResourceAsStream(name, bcc);
    }

    public URL getResource(String name, BeanContextChild bcc) throws IllegalArgumentException {
        return _beanContextServicesDelegate.getResource(name, bcc);
    }

    public void addBeanContextMembershipListener(BeanContextMembershipListener bcml) {
        _beanContextServicesDelegate.addBeanContextMembershipListener(bcml);
    }

    public void removeBeanContextMembershipListener(BeanContextMembershipListener bcml) {
        _beanContextServicesDelegate.removeBeanContextMembershipListener(bcml);
    }

    public BeanContext getBeanContext() {
        return _beanContextServicesDelegate.getBeanContext();
    }

    public void addPropertyChangeListener(String name, PropertyChangeListener pcl) {
        _beanContextServicesDelegate.addPropertyChangeListener(name, pcl);
    }

    public void removePropertyChangeListener(String name, PropertyChangeListener pcl) {
        _beanContextServicesDelegate.removePropertyChangeListener(name, pcl);
    }

    public void addVetoableChangeListener(String name, VetoableChangeListener vcl) {
        _beanContextServicesDelegate.addVetoableChangeListener(name, vcl);
    }

    public void removeVetoableChangeListener(String name, VetoableChangeListener vcl) {
        _beanContextServicesDelegate.removeVetoableChangeListener(name, vcl);
    }

    public int size() {
        return _beanContextServicesDelegate.size();
    }

    public boolean isEmpty() {
        return _beanContextServicesDelegate.isEmpty();
    }

    public boolean contains(Object o) {
        return _beanContextServicesDelegate.contains(o);
    }

    public Iterator iterator() {
        return _beanContextServicesDelegate.iterator();
    }

    public Object[] toArray() {
        return _beanContextServicesDelegate.toArray();
    }

    public Object[] toArray(Object[] a) {
        return _beanContextServicesDelegate.toArray(a);
    }

    public boolean containsAll(Collection c) {
        return _beanContextServicesDelegate.containsAll(c);
    }

    public boolean addAll(Collection c) {
        return _beanContextServicesDelegate.addAll(c);
    }

    public boolean removeAll(Collection c) {
        return _beanContextServicesDelegate.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        return _beanContextServicesDelegate.retainAll(c);
    }

    public void clear() {
        _beanContextServicesDelegate.clear();
    }

    public void setDesignTime(boolean designTime) {
        _beanContextServicesDelegate.setDesignTime(designTime);
    }

    public boolean isDesignTime() {
        return _beanContextServicesDelegate.isDesignTime();
    }

    public boolean needsGui() {
        return _beanContextServicesDelegate.needsGui();
    }

    public void dontUseGui() {
        _beanContextServicesDelegate.dontUseGui();
    }

    public void okToUseGui() {
        _beanContextServicesDelegate.okToUseGui();
    }

    public boolean avoidingGui() {
        return _beanContextServicesDelegate.avoidingGui();
    }

    public void serviceAvailable(BeanContextServiceAvailableEvent bcsae) {
        _beanContextServicesDelegate.serviceAvailable(bcsae);
    }

    /* --------------------------------------------------------------------------

       Static / deprecated methods.

       -------------------------------------------------------------------------- */

    /**
     * Applies externally defined (via INTERCEPTOR_CONFIG_FILE) ordering priority for
     * controls interceptor services.
     *
     * @param interceptors
     * @return String[]
     * @deprecated Use {@link InterceptorUtils#prioritizeInterceptors(String[])} instead.  This method will
     *             be removed in the next point release.
     */
    public static String[] prioritizeInterceptors( String [] interceptors ) {
        return InterceptorUtils.prioritizeInterceptors(interceptors);
    }

    /**
     * Returns the default binding based entirely upon annotations or naming conventions.
     * @param controlIntf the control interface class
     * @return the class name of the default control implementation binding
     * @deprecated Use {@link ControlUtils#getDefaultControlBinding(Class)} insated.  This method will be
     *             removed in the next point release.
     */
    public static String getDefaultControlBinding(Class controlIntf)
    {
        return ControlUtils.getDefaultControlBinding(controlIntf);
    }

    /**
     * Implements the default control implementation binding algorithm ( <InterfaceName> + "Impl" ).  See
     * documentation for the org.apache.beehive.controls.api.bean.ControlInterface annotation.
     *
     * @param implBinding the value of the defaultBinding attribute returned from a ControlInterface annotation
     * @param controlClass the actual name of the interface decorated by the ControlInterface annotation
     * @return the resolved defaultBinding value
     * @deprecated Use {@link ControlUtils#resolveDefaultBinding(String, String)} insated.  This method
     *             will be removed in the next point release.
     */
    public static String resolveDefaultBinding( String implBinding, String controlClass )
    {
        return ControlUtils.resolveDefaultBinding(implBinding, controlClass);
    }

    /**
     * The ControlBeanContextProvider inner class acts as a single BeanContext service
     * provider for the ControlBeanContext service class.  The implementation is simple,
     * because the runtime ControlBeanContext implementation class directly implements
     * this interface.
     */
    private static class ControlBeanContextProvider implements BeanContextServiceProvider
    {
        //
        // BeanContextServiceProvider.getService()
        //
        public Object getService(BeanContextServices bcs, Object requestor, Class serviceClass,
                                 Object serviceSelector)
        {
            //
            // Contextual services for a ControlBean is provided by the peer context
            // instance.
            //
            if (requestor instanceof ControlBean)
                return ((ControlBean)requestor).getControlBeanContext();

            return null;
        }

        //
        // BeanContextServiceProvider.releaseService()
        //
        public void releaseService(BeanContextServices bcs, Object requestor, Object service)
        {
            // noop, because context exists whether referenced or not
        }

        //
        // BeanContextServiceProvider.getContextServiceSelectors()
        //
        public Iterator getCurrentServiceSelectors(BeanContextServices bcs, Class serviceClass)
        {
            return null;    // no selectors
        }
    }

    /*package*/ static abstract class BeanContextServicesFactory {
        protected abstract BeanContextServices instantiate(ControlBeanContext controlBeanContext);
    }

    private static final class DefaultBeanContextServicesFactory
        extends BeanContextServicesFactory {
        protected BeanContextServices instantiate(ControlBeanContext controlBeanContext) {
            return new java.beans.beancontext.BeanContextServicesSupport(controlBeanContext);
        }
    }

    /**
     * A singleton instance of the ControlBeanContextProvider class is that will be registered
     * on all ControlBeanContext instances.  The provider can be a singleton because it is
     * completely stateless and thread-safe.
     */
    private static final ControlBeanContextProvider CONTROL_BEAN_CONTEXT_PROVIDER =
        new ControlBeanContextProvider();

    /**
     * A singleton instance of the BeanContextServicesFactory class that can be implemented by subclasses
     * to allow top-level Control containers to provide their own implementations of the
     * {@link java.beans.beancontext.BeanContextServices} interface.  This field is considered an implementation
     * detail and should not be referenced directly.
     */
    private static final BeanContextServicesFactory DEFAULT_BEAN_CONTEXT_SERVICES_FACTORY =
        new DefaultBeanContextServicesFactory();

    /**
     * The ControlBean instance that this context is providing services for.  This value can
     * be null, if the context instance is associated with top-level (non-control) context.
     */
    private ControlBean _bean;

    /**
     * Indicates whether this context's parent guarantees single-threaded behaviour.
     */
    private boolean _hasSingleThreadedParent = false;

    /**
     * Maps children by the local (relative) ID of the child to the actual bean instance.
     * This needs to be synchronized, because adds/removes/gets are not necessarily guaranteed
     * to happen within the scope of the global hierarchy lock.   It would be relatively easy
     * to synchronize add/remove, since setBeanContext on the child is inside this lock scope,
     * but gets on the map are another story.
     */
    private Map<String,Object> _childMap = Collections.synchronizedMap(new HashMap<String,Object>());

    /**
     * Maintains a set of NameGenerators (for control ID generation) keyed by a
     * base prefix.  The map itself is lazily constructed, so there is minimal
     * overhead of no id generation is needed in this context.
     */
    private Map<String,NameGenerator> _nameGenerators;

    /**
     * Maintains the list of lifecycle event listeners (if any) for this context.
     */
    private transient Vector<LifeCycle> _lifeCycleListeners;

    /**
     * Caches the full composite control ID, that includes the entire path from the root
     * ContainerContext to the associated bean.  This value can be transient, since it
     * can be easily recomputed when needed.
     */
    private transient String _controlID;

    /**
     * Object that implements the java.beans.beancontext APIs from the JDK to provide compliance with the
     * JavaBeans BeanContext / BeanContextChild specification.  The ControlBeanContext class uses
     * this object as a delegate to provide this functionality rather than extending the BeanContext
     * support classes directly.  This allows for more flexibility in how the BeanContextServices (et al)
     * API implementations evolve over time.
     */
    private BeanContextServices _beanContextServicesDelegate;
}
