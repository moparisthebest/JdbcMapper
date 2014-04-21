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

import java.beans.beancontext.BeanContext;
import java.beans.beancontext.BeanContextServices;
import java.beans.PropertyChangeSupport;
import java.beans.VetoableChangeSupport;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.Vector;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.properties.BaseProperties;
import org.apache.beehive.controls.api.properties.AnnotatedElementMap;
import org.apache.beehive.controls.api.properties.PropertyMap;
import org.apache.beehive.controls.api.properties.BeanPropertyMap;
import org.apache.beehive.controls.api.properties.PropertyKey;
import org.apache.beehive.controls.api.properties.PropertySetProxy;
import org.apache.beehive.controls.api.versioning.VersionRequired;
import org.apache.beehive.controls.api.versioning.Version;
import org.apache.beehive.controls.api.bean.Threading;
import org.apache.beehive.controls.api.bean.ThreadingPolicy;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.events.EventRef;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.spi.context.ControlBeanContextFactory;
import org.apache.beehive.controls.spi.svc.Interceptor;
import org.apache.beehive.controls.spi.svc.InterceptorPivotException;
import org.apache.commons.discovery.tools.DiscoverClass;

/**
 * The ControlBean class is an abstract base class for the JavaBean classes generated to support
 * Beehive Controls.
 * <p>
 * The ControlBean class indirectly implements BeanContextProxy; the
 * {@link org.apache.beehive.controls.api.context.ControlBeanContext} that it contains/scopes acts as that proxy.
 * <p>
 * All support APIs (which may be called from derived subclasses or contextual services)
 * are generally marked as protected and have names that start with an underscore.  This avoids the
 * possibility that the name might conflict with a user-defined method on a control's public or
 * extended Control interfaces.
 * <p>
 * NOTE: Adding public methods should be done with great care; any such method becomes part of the
 * public API, and occupies the method namespace for all controls.
 */
abstract public class ControlBean
    implements org.apache.beehive.controls.api.bean.ControlBean
{
    /**
     *
     * @param context        the containing ControlBeanContext.  May be null, in which case the bean will attempt to
     *                       associate with an active context at runtime (via thread-locals).   
     * @param id             the local ID for the control, scoped to the control bean context.
     * @param initProperties a PropertyMap containing initial properties for the control
     * @param controlIntf    the control interface or extension directly implemented by the control bean
     */
    protected ControlBean(org.apache.beehive.controls.api.context.ControlBeanContext context,
                          String id,
                          PropertyMap initProperties,
                          Class controlIntf)
    {
        super();

        _localID = id;
        _controlIntf = controlIntf;

        //
        // If no containing context was specified during construction, see if there is a current
        // active container context and implicitly associated the control with it.
        //
        if (context == null)
            context = ControlThreadContext.getContext();

        ControlBeanContextFactory cbcFactory = lookupControlBeanContextFactory(context);
        _cbc = cbcFactory.instantiate(this);

        //
        // Associate this bean with the context.  Beans may run without a context!
        // Note that the add() call has the side-effect of calling ControlBean.setBeanContext(), which does
        // additional setup work, so we make sure we always call that anyways!
        //
        if (context != null)
            context.add(this);
        else
            setBeanContext(null);

        //
        // Get the default map for the control class.  This contains the default property
        // values for all beans of the class.
        //
        PropertyMap classMap = getAnnotationMap(controlIntf);
        if (initProperties != null)
        {
            //
            // If the initialization map derives its values from a Java annotated element,
            // then allow container overrides on this element to be applied.  This will also
            // coelesce maps referencing the same element.
            //
            AnnotatedElement annotElem = null;
            if (initProperties instanceof AnnotatedElementMap)
            {
                annotElem = ((AnnotatedElementMap)initProperties).getAnnotatedElement();
                initProperties = getAnnotationMap(annotElem);
            }

            //
            // If an initial property map was provided, set it to delegate to the default
            // map, and then create a wrapper map around it for storing per instance
            // properties.
            //
            if (annotElem != controlIntf)
                initProperties.setDelegateMap(classMap);
            _properties = new BeanPropertyMap(initProperties);
        }
        else
        {
            //
            // If no initial map was provided, simply create an empty map wrapping the
            // control class default.
            //
            _properties = new BeanPropertyMap(classMap);
        }

    }

    /**
     * Configure this bean to be thread-safe given the threading settings of the impl class and
     * the outer container.
     */
    private void ensureThreadingBehaviour()
    {
        //
        // If the implementation class requires a guarantee of single-threaded behavior and the
        // outer container does not guarantee it, then enable invocation locking on this
        // bean instance.
        //
        if (hasSingleThreadedImpl() && ! _cbc.hasSingleThreadedParent())
            _invokeLock = new Semaphore(1, true);
        else
            _invokeLock = null;
    }
    /**
     * Return the BeanContextService proxy associated with this bean instance
     */
    final public ControlBeanContext getBeanContextProxy()
    {
        return _cbc;
    }

    /**
     * Returns the nesting BeanContext for this ControlBean.  This is thread-safe even though it
     * is not synchronized.
     */
    final public BeanContext getBeanContext()
    {
        //
        // Indirect through the bean proxy for this control bean and up to its parent nesting
        // context.  Both these calls (getBeanContextProxy() and getBeanContext()) are, and must
        // remain, thread-safe.
        //
        return getBeanContextProxy().getBeanContext();
    }

    /**
     * Called by the BeanContextProxy (_cbc) whenever the _parent_ context containing this control bean is
     * changed.  This is the place to do any initialization (or reinitialization) that is dependent
     * upon attributes of the container for the ControlBean.
     *
     * Note: this is called in the ControlBean ctor, when a parent context calls add() on the nascent
     * bean.
     *
     * @param bc the new parent context containing this control bean (not _cbc)
     */
    final public synchronized void setBeanContext(BeanContext bc)
    {
        ensureThreadingBehaviour();
    }

    /**
     * Returns the control ID for this control
     */
    final public String getControlID()
    {
        return _cbc.getControlID();
    }

    /**
     * Returns the public interface for this control.
     */
    final public Class getControlInterface()
    {
        return _controlIntf;
    }

    /**
     * Returns true if the implementation class for this ControlBean requires the framework
     * to ensure thread-safety for it.
     */
    /*package*/ boolean hasSingleThreadedImpl()
    {
        return _threadingPolicy == ThreadingPolicy.SINGLE_THREADED;
    }

    /**
     * Obtains an instance of the appropriate ImplInitializer class
     */
    protected synchronized ImplInitializer getImplInitializer()
    {
        if (_implInitializer == null)
        {
            try
            {
                Class initClass = _implClass.getClassLoader().loadClass(
                                        _implClass.getName() + "Initializer");
                _implInitializer = (ImplInitializer)initClass.newInstance();
            }
            catch (Exception e)
            {
                throw new ControlException("Control initialization failure", e);
            }
        }
        return _implInitializer;
    }

    /**
     * Returns the target control instance associated with this ControlBean, performing lazy
     * instantiation and initialization of the instance.
     *
     * REVIEW: could probably improve the granularity of locking here, but start w/ just
     * synchronizing the entire fn.
     */
    public synchronized Object ensureControl()
    {
        if (_control == null)
        {
            //
            // See if the property map specifies an implementation class for the control; 
            // if not, use default binding.
            //

            String implBinding = null;
            BaseProperties bp = _properties.getPropertySet( BaseProperties.class );
            if ( bp != null )
                implBinding = bp.controlImplementation();
            else
                implBinding = ControlUtils.getDefaultControlBinding(_controlIntf);

            try
            {
                _implClass = _controlIntf.getClassLoader().loadClass(implBinding);

                //
                // Validate that the specified implementation class has an @ControlImplementation
                // annotation, else downstream requirements (such as having a valid control init
                // class) will not be met.
                //
                if (_implClass.getAnnotation(ControlImplementation.class) == null)
                {
                    throw new ControlException("@" + ControlImplementation.class.getName() + " annotation is missing from control implementation class: " + _implClass.getName());
                }
            }
            catch (ClassNotFoundException cnfe)
            {
                throw new ControlException("Unable to load control implementation: "  + implBinding, cnfe);
            }

            //
            // Cache the threading policy associated with the impl
            //
            Threading thr = (Threading)_implClass.getAnnotation(Threading.class);
            if ( thr != null )
                _threadingPolicy = thr.value();
            else
                _threadingPolicy = ThreadingPolicy.SINGLE_THREADED;    // default to single-threaded

            ensureThreadingBehaviour();

            try
            {
                //
                // Create and initialize the new instance
                //
                _control = _implClass.newInstance();

                try
                {
                    /*
                    Run the ImplInitializer.  This class is code generated based on metadata from a control
                    implementation.  If a Control implementation declares event handlers for the
                    ControlBeanContext or for the ResourceContext, executing this code generated class
                    will add the appropriate LifeCycle and / or Resource event listeners.
                     */
                    getImplInitializer().initialize(this, _control);
                    _hasServices = true;
                }
                catch (Exception e)
                {
                    throw new ControlException("Control initialization failure", e);
                }

                //
                // Once the control is initialized, then allow the associated context
                // to do any initialization.
                //
                ControlBeanContext cbcs = getBeanContextProxy();

                /*
                Implementation note: this call will run the LifeCycleListener(s) that have
                been wired-up to the ControlBeanContext object associated with this ControlBean.
                */
                cbcs.initializeControl();
            }
            catch (RuntimeException re) {
                // never mask RuntimeExceptions
                throw re;
            }
            catch (Exception e)
            {
                throw new ControlException("Unable to create control instance", e);
            }
        }

        //
        // If the implementation instance does not currently have contextual services, they
        // are lazily restored here.
        //
        if (!_hasServices)
        {
            getImplInitializer().initServices(this, _control);
            _hasServices = true;
        }

        return _control;
    }

    /**
     * Returns the implementation instance associated with this ControlBean.
     */
    /* package */ Object getImplementation() {
        return _control;
    }

    /**
     * The preinvoke method is called before all operations on the control.  In addition to
     * providing a basic hook for logging, context initialization, resource management, 
     * and other common services, it also provides a hook for interceptors.
     */
    protected void preInvoke(Method m, Object [] args, String [] interceptorNames)
        throws InterceptorPivotException
    {
        //
        // If the implementation expects single threaded behavior and our container does
        // not guarantee it, then enforce it locally here
        //
        if (_invokeLock != null)
        {
            try { _invokeLock.acquire(); } catch (InterruptedException ie) { }
        }

        //
        // Process interceptors
        //
        if ( interceptorNames != null )
        {
            for ( String n : interceptorNames )
            {
                Interceptor i = ensureInterceptor( n );
                try
                {
                    i.preInvoke( this, m, args );
                }
                catch (InterceptorPivotException ipe)
                {
                    ipe.setInterceptorName(n);
                    throw ipe;
                }
            }
        }

        Vector<InvokeListener> invokeListeners = getInvokeListeners();
        if (invokeListeners.size() > 0)
        {
            for (InvokeListener listener : invokeListeners)
                listener.preInvoke(m, args);
        }
    }

    /**
     * The preinvoke method is called before all operations on the control.  It is the basic
     * hook for logging, context initialization, resource management, and other common
     * services
     */
    protected void preInvoke(Method m, Object [] args)
    {
        try
        {
            preInvoke(m, args, null);
        }
        catch (InterceptorPivotException ipe)
        {
            //this will never happen because no interceptor is passed.
        }
    }

    /**
     * The postInvoke method is called after all operations on the control.  In addition to 
     * providing the basic hook for logging, context initialization, resource management, and other common
     * services, it also provides a hook for interceptors.  During preInvoke, interceptors will be 
     * called in the order that they are in the list.  During postInvoke, they will be called in the
     * reverse order.  Here is an example of the call sequence with I1, I2, and I3 being interceptors in the list:
     * 
     *   I1.preInvoke() -> I2.preInvoke() -> I3.preInvoke() -> invoke method
     *                                                             |
     *   I1.postInvoke() <- I2.postInvoke() <- I3.postInvoke() <--- 
     * 
     * In the event that an interceptor in the list pivoted during preInvoke, the "pivotedInterceptor"
     * parameter indicates the interceptor that had pivoted, and the interceptors succeeding it in the list will
     * not be called during postInvoke. 
     */
    protected void postInvoke(Method m, Object [] args, Object retval, Throwable t, String [] interceptorNames, String pivotedInterceptor)
    {
        try
        {
            Vector<InvokeListener> invokeListeners = getInvokeListeners();
            if (invokeListeners.size() > 0)
            {
                for (InvokeListener listener : invokeListeners)
                    listener.postInvoke(retval, t);
            }

            //
            // Process interceptors
            //
            if ( interceptorNames != null )
            {
                for (int cnt = interceptorNames.length-1; cnt >= 0; cnt-- )
                {
                    String n  = interceptorNames[cnt];
                    if (pivotedInterceptor == null || n.equals(pivotedInterceptor))
                    {
                        pivotedInterceptor = null;
                        Interceptor i = ensureInterceptor( n );
                        i.postInvoke( this, m, args, retval, t );
                    }
                }
            }
        }
        finally
        {
            //
            // Release any lock obtained above in preInvoke
            //
            if (_invokeLock != null)
                _invokeLock.release();
        }
    }

    /**
     * The postInvoke method is called after all operations on the control.  It is the basic
     * hook for logging, context initialization, resource management, and other common
     * services.
     */
    protected void postInvoke(Method m, Object [] args, Object retval, Throwable t)
    {
        postInvoke(m, args, retval, t, null, null);
    }


    /**
     * Sets the EventNotifier for this ControlBean
     */
    protected <T> void setEventNotifier(Class<T> eventSet, T notifier)
    {
        _notifiers.put(eventSet,notifier);

        //
        // Register this notifier for all EventSet interfaces up the interface inheritance
        // hiearachy as well
        //
        List<Class> superEventSets = new ArrayList<Class>();
        getSuperEventSets(eventSet, superEventSets);
        Iterator<Class> i = superEventSets.iterator();
        while (i.hasNext())
        {
            Class superEventSet = i.next();
            _notifiers.put(superEventSet,notifier);
        }
    }

    /**
     * Finds all of the EventSets extended by the input EventSet, and adds them to
     * the provided list. 
     * @param eventSet
     * @param superEventSets
     */
    private void getSuperEventSets(Class eventSet, List<Class> superEventSets)
    {
        Class[] superInterfaces = eventSet.getInterfaces();
        if (superInterfaces != null)
        {
            for (int i=0; i < superInterfaces.length; i++)
            {
                Class superInterface = superInterfaces[i];
                if (superInterface.isAnnotationPresent(EventSet.class))
                {
                    superEventSets.add(superInterface);

                    // Continue traversing up the EventSet inheritance hierarchy
                    getSuperEventSets(superInterface, superEventSets);
                }
            }
        }
    }

    /**
     * Returns an EventNotifier/UnicastEventNotifier for this ControlBean for the target event set
     */
    protected <T> T getEventNotifier(Class<T> eventSet)
    {
        return (T)_notifiers.get(eventSet);
    }

    /**
     * Returns the list of InvokeListeners for this ControlBean
     */
    /* package */ Vector<InvokeListener> getInvokeListeners()
    {
        if (_invokeListeners == null)
            _invokeListeners = new Vector<InvokeListener>();
        return _invokeListeners;
    }

    /**
     * Registers a new InvokeListener for this ControlBean.
     */
    /* package */ void addInvokeListener(InvokeListener invokeListener)
    {
        getInvokeListeners().addElement(invokeListener);
    }

    /**
     * Deregisters an existing InvokeListener for this ControlBean.
     */
    /* package */ void removeInvokeListener(InvokeListener invokeListener)
    {
        getInvokeListeners().removeElement(invokeListener);
    }

    /**
     * Returns the local (parent-relative) ID for this ControlBean
     */
    protected String getLocalID()
    {
        return _localID;
    }

    /**
     * Set the local (parent-relative) ID for this ControlBean.  It has package access because
     * the local ID should only be set from within the associated context, and only when the
     * bean is currently anonymous (hence the assertion below)
     */
    /* package */ void setLocalID(String localID)
    {
        assert _localID == null;    // should only set if not already set!
        _localID = localID;
    }

    /**
     * Returns the bean context instance associated with the this bean, as opposed to the
     * parent context returned by the public getBeanContext() API.
     */
    public ControlBeanContext getControlBeanContext()
    {
        //
        // The peer context instance is the context provider for this ControlBean
        //
        return getBeanContextProxy();
    }

    /**
     * Locates and obtains a context service from the BeanContextServices instance
     * supporting this bean.
     *
     * The base design for the BeanContextServicesSupport is that it will delegate up to peers
     * in a nesting context, so a nested control bean will look 'up' to find a service provider.
     */
    protected Object getControlService(Class serviceClass, Object selector)
                     throws TooManyListenersException

    {
        //
        // Get the associated context object, then use it to locate the (parent) bean context.
        // Services are always provided by the parent context.
        //
        ControlBeanContext cbc = getControlBeanContext();
        BeanContext bc = cbc.getBeanContext();
        if (bc == null || !(bc instanceof BeanContextServices))
            throw new ControlException("Can't locate service context: " + bc);

        //
        // Call getService on the parent context, using this bean as the requestor and the
        // associated peer context instance as the child and event listener parameters.
        //
        return  ((BeanContextServices)bc).getService(cbc, this, serviceClass, selector, cbc);
    }

    /**
     * Sets a property on the ControlBean instance.  All generated property setter methods
     * will delegate down to this method.
     */
    protected void setControlProperty(PropertyKey key, Object o)
    {
        AnnotationConstraintValidator.validate(key, o);
        _properties.setProperty(key, o);
    }

    /**
     * Dispatches the requested operation event on the ControlBean.
     * @see org.apache.beehive.controls.runtime.bean.ControlContainerContext#dispatchEvent
     */
    /* package */ Object dispatchEvent(EventRef event, Object [] args)
                         throws IllegalAccessException,IllegalArgumentException,
                                InvocationTargetException
    {
        ensureControl();

        //
        // Translate the EventRef back to an actual event method on the ControlInterface
        //
        Class controlInterface = getControlInterface();
        Method method = event.getEventMethod(controlInterface);

        //
        // Locate the target of the event
        //
        Object eventTarget = null;
        if (method.getDeclaringClass().isAssignableFrom(_control.getClass()))
        {
            //
            // If the control implementation implements that EventSet interface, then 
            // dispatch the event directly to it, and allow it do make the decision about 
            // how/when to dispatch to any external listeners (via a @Client notifier 
            // instance)
            //
            eventTarget = _control;
        }
        else
        {
            //
            // The associated control implementation does not directly handle the event,
            // so find the event notifier instance for the EventSet interface associated 
            // with the method.
            //
            eventTarget = _notifiers.get(method.getDeclaringClass());
            if (eventTarget == null)
                throw new IllegalArgumentException("No event notifier found for " + event);
        }

        //
        // Dispatch the event
        //
        return method.invoke(eventTarget, args);
    }

    /**
     * Returns a property on the ControlBean instance.   This version does not coerce
     * an annotation type property from a PropertyMap to a proxy instance of the
     * type.
     */
    protected Object getRawControlProperty(PropertyKey key)
    {
        return _properties.getProperty(key);
    }

    /**
     * Returns a property on the ControlBean instance.  All generated property getter methods
     * will delegate down to this method
     */
    protected Object getControlProperty(PropertyKey key)
    {
        Object value = getRawControlProperty(key);

        // If the held value is a PropertyMap, then wrap it in an annotation proxy of
        // the expected type.
        if (value instanceof PropertyMap)
        {
            PropertyMap map = (PropertyMap)value;
            value = PropertySetProxy.getProxy(map.getMapClass(), map);
        }

        return value;
    }

    /* this method is implemented during code generation by a ControlBean extension */
    /**
     * Returns the local cache for ControlBean property maps.
     */
    abstract protected Map getPropertyMapCache();

    /**
     * Returns the PropertyMap containing values associated with an AnnotatedElement.  Elements
     * that are associated with the bean's Control interface will be locally cached.
     */
    protected PropertyMap getAnnotationMap(AnnotatedElement annotElem)
    {
        Map annotCache = getPropertyMapCache();

        // If in the cache already , just return it
        if (annotCache.containsKey(annotElem))
            return (PropertyMap)annotCache.get(annotElem);

        //
        // Ask the associated ControlBeanContext to locate and initialize a PropertyMap, then
        // store it in the local cache.
        //
        PropertyMap map = getControlBeanContext().getAnnotationMap(annotElem);
        annotCache.put(annotElem, map);

        return map;
    }

    /**
     * Returns the property map containing the properties for the bean
     */
    /* package */ BeanPropertyMap getPropertyMap()
    {
        return _properties;
    }

    /**
     * This protected version is only available to concrete subclasses that expose bound
     * property support.   This method is synchronized to enable lazy instantiation, in
     * the belief that it is a bigger win to avoid allocating when there are no listeners
     * than it is to introduce synchronization overhead on access.
     */
    synchronized protected PropertyChangeSupport getPropertyChangeSupport()
    {
        if (_changeSupport == null)
            _changeSupport = new PropertyChangeSupport(this);

        return _changeSupport;
    }

    /**
     * Delivers a PropertyChangeEvent to any registered PropertyChangeListeners associated
     * with the property referenced by the specified key.
     *
     * This method *should not* be synchronized, as the PropertyChangeSupport has its own
     * built in synchronization mechanisms.
     */
    protected void firePropertyChange(PropertyKey propertyKey, Object oldValue, Object newValue)
    {
        // No change support instance means no listeners
        if (_changeSupport == null)
            return;

        _changeSupport.firePropertyChange(propertyKey.getPropertyName(), oldValue, newValue);
    }

    /**
     * This protected version is only available to concrete subclasses that expose bound
     * property support.   This method is synchronized to enable lazy instantiation, in
     * the belief that is a bigger win to avoid allocating when there are no listeners
     * than it is to introduce synchronization overhead on access.
     */
    synchronized protected VetoableChangeSupport getVetoableChangeSupport()
    {
        if (_vetoSupport == null)
            _vetoSupport = new VetoableChangeSupport(this);

        return _vetoSupport;
    }

    /**
     * Delivers a PropertyChangeEvent to any registered VetoableChangeListeners associated
     * with the property referenced by the specified key.
     *
     * This method *should not* be synchronized, as the VetoableChangeSupport has its own
     * built in synchronization mechanisms.
     */
    protected void fireVetoableChange(PropertyKey propertyKey, Object oldValue, Object newValue)
        throws java.beans.PropertyVetoException
    {
        // No veto support instance means no listeners
        if (_vetoSupport == null)
            return;

        _vetoSupport.fireVetoableChange(propertyKey.getPropertyName(), oldValue, newValue);
    }

    /**
     * Returns the parameter names for a method on the ControlBean.  Actual mapping is done
     * by generated subclasses, so if we reach the base ControlBean implementation, then
     * no parameter names are available for the target method.
     */
    protected String [] getParameterNames(Method m)
    {
        throw new IllegalArgumentException("No parameter name data for " + m);
    }

    /**
     * Computes the most derived ControlInterface for the specified ControlExtension.
     * @param controlIntf
     * @return the most derived ControlInterface
     * @deprecated Use {@link ControlUtils#getMostDerivedInterface(Class)} instead.  This method will
     *             be removed in the next release.
     */
    public static Class getMostDerivedInterface(Class controlIntf)
    {
        return ControlUtils.getMostDerivedInterface(controlIntf);
    }

    /**
     * Enforces the VersionRequired annotation at runtime (called from each ControlBean).
     * @param intfName
     * @param version
     * @param versionRequired
     */
    protected static void enforceVersionRequired(String intfName, Version version, VersionRequired versionRequired)
    {
        if ( versionRequired != null )
        {
            int majorRequired = versionRequired.major();
            int minorRequired = versionRequired.minor();

            if ( majorRequired < 0 )    // no real version requirement
                return;

            int majorPresent = -1;
            int minorPresent = -1;
            if ( version != null )
            {
                majorPresent = version.major();
                minorPresent = version.minor();

                if ( majorRequired <= majorPresent &&
                     (minorRequired < 0 || minorRequired <= minorPresent) )
                {
                    // Version requirement is satisfied
                    return;
                }
            }

            //
            // Version requirement failed
            //
            throw new ControlException( "Control extension " + intfName + " fails version requirement: requires interface version " +
                    majorRequired + "." + minorRequired + ", found interface version " +
                    majorPresent + "." + minorPresent + "." );
        }
    }


    /**
     * Implementation of the Java serialization writeObject method
     */
    private synchronized void writeObject(ObjectOutputStream oos)
        throws IOException
    {
        if (_control != null)
        {
            //
            // If the implementation class is marked as transient/stateless, then reset the
            // reference to it prior to serialization.  A new instance will be created by
            // ensureControl() upon first use after deserialization.
            // If the implementation class is not transient, then invoke the ImplInitializer
            // resetServices method to reset all contextual service references to null, as
            // contextual services should never be serializated and always reassociated on 
            // deserialization.
            //
            ControlImplementation implAnnot = (ControlImplementation)_implClass.getAnnotation(ControlImplementation.class);
            assert implAnnot != null;
            if (implAnnot.isTransient())
            {
                _control = null;
            }
            else
            {
                getImplInitializer().resetServices(this, _control);
                _hasServices = false;
            }
        }

        oos.defaultWriteObject();
    }

    /**
     * Called during XMLDecoder reconstruction of a ControlBean.
     */
    public void decodeImpl(Object impl)
    {
        if (impl != _control)
            throw new ControlException("Cannot change implementation");
    }

    /**
     * Internal method used to lookup a ControlBeanContextFactory.  This factory is used to create the
     * ControlBeanContext object for this ControlBean.  The factory is discoverable from either the containing
     * ControlBeanContext object or from the environment.  If the containing CBC object exposes a
     * contextual service of type {@link ControlBeanContextFactory}, the factory returned from this will
     * be used to create a ControlBeanContext object.
     *
     * @param context
     * @return the ControlBeanContextFactory discovered in the environment or a default one if no factory is configured
     */
    private ControlBeanContextFactory lookupControlBeanContextFactory
        (org.apache.beehive.controls.api.context.ControlBeanContext context) {

        // first, try to find the CBCFactory from the container
        if(context != null) {
            ControlBeanContextFactory cbcFactory = context.getService(ControlBeanContextFactory.class, null);

            if(cbcFactory != null) {
                return cbcFactory;
            }
        }

        // Create the context that acts as the BeanContextProxy for this bean (the context that this bean _defines_).
        try
        {
            DiscoverClass discoverer = new DiscoverClass();
            Class factoryClass =
                discoverer.find(ControlBeanContextFactory.class, DefaultControlBeanContextFactory.class.getName());

            return (ControlBeanContextFactory)factoryClass.newInstance();
        }
        catch (Exception e) {
            throw new ControlException("Exception creating ControlBeanContext", e);
        }
    }

    /**
     * Retrieves interceptor instances, creates them lazily.
     */
    protected Interceptor ensureInterceptor( String n )
    {
        Interceptor i = null;
        if ( _interceptors == null )
        {
            _interceptors = new HashMap<String,Interceptor>();
        }
        else
        {
            i = _interceptors.get( n );
        }

        if ( i == null )
        {
            try
            {
                i  = (Interceptor) getControlService( getControlBeanContext().getClassLoader().loadClass( n ), null );
            }
            catch ( Exception e )
            {
                // Couldn't instantiate the desired service; usually this is because the service interface itself
                // isn't present on this system at runtime (ClassNotFoundException), or if the container of the
                // control didn't registers the service.

                /* TODO log a message here to that effect, but just swallow the exception for now. */
            }
            finally
            {
                // We want to always return an interceptor, so if we can't get the one we want, we'll substitute
                // a "null" interceptor that does nothing.
                if ( i == null)
                    i = new NullInterceptor();

                _interceptors.put( n, i );
            }
        }
        return i;
    }

    /**
     * The "null" interceptor that does nothing.  Used when a specific interceptor
     * is unavailable at runtime.
     */
    static private class NullInterceptor
        implements Interceptor
    {
        public void preInvoke( org.apache.beehive.controls.api.bean.ControlBean cb, Method m, Object [] args ) {}
        public void postInvoke( org.apache.beehive.controls.api.bean.ControlBean cb, Method m, Object [] args, Object retval, Throwable t) {}
        public void preEvent( org.apache.beehive.controls.api.bean.ControlBean cb, Class eventSet, Method m, Object [] args) {}
        public void postEvent( org.apache.beehive.controls.api.bean.ControlBean cb, Class eventSet, Method m, Object [] args, Object retval, Throwable t ) {}
    }

    /** BEGIN unsynchronized fields */

    /**
     * The following fields are initialized in the constructor and never subsequently changed,
     * so they are safe for unsynchronized read access
     */

    /**
     * The control implementation class bound to this ControlBean
     */
    protected Class _implClass;

    /**
     * The threading policy associated with the control implementation wrapped by this
     * ControlBean.  Initialized to MULTI_THREADED in order to assume multi-threadedness
     * until a bean is associated with a specific (potentially single-threaded) implementation.
     */
    transient private ThreadingPolicy _threadingPolicy = ThreadingPolicy.MULTI_THREADED;

    /**
     *  Contains the per-instance properties set for this ControlBean.
     */
    private BeanPropertyMap _properties;

    /** END unsynchronized fields */

    /* BEGIN synchronized fields */

    /*
     * The following fields must be:
     * 1) only written in synchronized methods or (unsynchronized) constructors
     * 2) only read in synchronized methods or methods that are safe wrt the values changing during
     *    execution.
     */

    /**
     * The control implementation instance wrapped by this ControlBean
     */
    private Object _control;

    /**
     * The control bean context instance associated with this ControlBean
     */
    private ControlBeanContext _cbc;

    /**
     * An ImplInitializer instances used to initialize/reset the state of the associated
     * implementation instance.
     */
    transient private ImplInitializer _implInitializer;

    /**
     * Indicates whether the contextual services associated with the bean have been
     * fully initialized.
     */
    transient private boolean _hasServices = false;

    /**
     * Used to guarantee single threaded invocation when required.  If the
     * outer container provides the guarantee or the implementation itself
     * is threadsafe, then the value will be null.
     */
    transient private Semaphore _invokeLock;

    /**
     * This field manages PropertyChangeListeners (if supporting bound properties).
     */
    private PropertyChangeSupport _changeSupport;

    /**
     * This field manages VetoabbleChangeListeners (if supporting constrained properties)
     */
    private VetoableChangeSupport _vetoSupport;

    /** END synchronized fields */

    /**
     * The (context relative) control ID associated with this instance
     */
    private String _localID;

    /**
     * The public control interface associated with this ControlBean
     */
    private Class _controlIntf;

    /**
     * This field manages the register listener list(s) associated with event set interfaces
     * for the ControlBean.  The value objects are either UnicastEventNotifier or EventNotifier
     * instances, depending upon whether the associated EventSet interface is unicast or
     * multicast.
     */
    private HashMap<Class, Object> _notifiers = new HashMap<Class,Object>();

    /**
     * Maintains the list of callback event listeners (if any) for this ControlBean.
     */
    transient private Vector<InvokeListener> _invokeListeners;

    /**
     * HashMap to hold interceptor impl instances.
     * Populated lazily.  Maps interceptor interface name to impl.
     */
    transient private HashMap<String,Interceptor> _interceptors;
}
