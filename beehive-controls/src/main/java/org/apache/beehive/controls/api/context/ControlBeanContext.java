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
package org.apache.beehive.controls.api.context;

import java.beans.beancontext.BeanContextServices;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.apache.beehive.controls.api.bean.ControlBean;
import org.apache.beehive.controls.api.events.EventSet;
import org.apache.beehive.controls.api.properties.PropertyMap;

/**
 * The ControlBeanContext interface defines the basic set of contextual services and lifecycle
 * events for Java ControlBean implementations.
 * <p>
 * ControlBeanContext also extends the <code>java.beans.beancontext.BeanContextServices</code>
 * interface, so it also provide core Java Beans services for managing contained controls,
 * looking up contextual services, and locating the parent {@link java.beans.beancontext.BeanContext} context.
 * <p>
 * A Control implementation class can obtain access to the ControlBeanContext associated
 * with it by declaring an instance field of this type and annotating it with the
 * <code>org.apache.beehive.controls.api.context.Context</code> annotation, as in the following
 * example:
 *
 * <code><pre>
 * import org.apache.beehive.controls.api.context.Context;
 * import org.apache.beehive.controls.api.context.ControlBeanContext;
 *
 * <sp>@ControlImplementation
 * public class MyControlImpl
 * {
 *     <sp>@Context
 *     ControlBeanContext myContext;
 * }
 * </pre></code>
 * The Java Control runtime will automatically initialize this field to a reference to the
 * ControlBeanContext associated with the implementation instance.
 */
public interface ControlBeanContext extends BeanContextServices
{
    /**
     * Returns the public or extension interface associated with the context
     */
    public Class getControlInterface();

    /**
     * Returns the current value of PropertySet for the associated control, or
     * null if the property set has not been bound.  Actual bindings for property
     * values may be the result of annotations on the control field or class,
     * property setting via factory arguments or setter APIs, or external
     * configuration.
     *
     * @param propertySet the PropertySet to return
     * @return the requested PropertySet instance, or null if not bound
     *
     * @see org.apache.beehive.controls.api.properties.PropertySet
     */
    public <T extends Annotation> T getControlPropertySet(Class<T> propertySet);

    /**
     * Returns the current value of PropertySet for the provided method, or null
     * if the property set has not been bound for this method.
     *
     * @param m the Method to check for properties.
     * @param propertySet the PropertySet to return
     * @return the requested PropertySet instance, or null if not bound
     *
     * @see org.apache.beehive.controls.api.properties.PropertySet
     */
    public <T extends Annotation> T getMethodPropertySet(Method m, Class<T> propertySet)
        throws IllegalArgumentException;

    /**
     * Returns the current value of PropertySet for the selected (by index) method parameter,
     * or null if the property set has not been bound for this method.
     *
     * @param m the Method to check for properties
     * @param i the index of the method parameter to check for the request PropertySet
     * @param propertySet the PropertySet to return
     * @return the request PropertySet instance, or null if not bound
     */
    public <T extends Annotation> T getParameterPropertySet(Method m, int i, Class<T> propertySet)
        throws IllegalArgumentException, IndexOutOfBoundsException;

    /**
     * Returns an array containing the parameter names for the specified method
     *
     * @param m the Method whose parameter names should be returned.
     * @return the array of parameter names (or an empty array if no parameters)
     */
    public String [] getParameterNames(Method m)
        throws IllegalArgumentException;

    /**
     * Returns the value of a named method parameter from the input parameter array.
     *
     * @param m the Method associated with the input parameter list
     * @param parameterName the name of the requested parameter
     * @param parameters the array of method parameters
     * @return the element in the input parameter array that corresponds to the requested
     *         parameter
     */
    public Object getParameterValue(Method m, String parameterName, Object [] parameters)
        throws IllegalArgumentException;

    /**
     * Returns the current set of properties (in PropertyMap format) for the control 
     * associated with the context.  The return map will contain the values for all bound 
     * properties for the control.
     * @return the PropertyMap containing properties of the control.  This map is read-only;
     * any changes to it will not effect the local bean instance.
     *
     * @see org.apache.beehive.controls.api.properties.PropertyMap
     */
    public PropertyMap getControlPropertyMap();

    /**
     * Returns an instance of a contextual service based upon the local context.  If
     * no provider for this service is available, then null will be returned.
     *
     * @param serviceClass the class of the requested service
     * @param selector the service dependent parameter
     * @return an instance of the request service, or null if unavailable
     *
     * @see java.beans.beancontext.BeanContextServices#getService
     */
    public <T> T getService(Class<T> serviceClass, Object selector);

    /**
     * Returns a ControlHandle instance that enables operations and events to be dispatched
     * to the target control, if it is running inside of a container that supports external
     * event dispatch.  If the runtime container for the control does not support this
     * functionality, a value of null will be returned.
     *
     * @return a ControlHandle instance for the control, or null.
     *
     * @see org.apache.beehive.controls.api.context.ControlHandle
     */
    public ControlHandle getControlHandle();

    /**
     * Returns the PropertyMap containing default properties for an AnnotatedElement
     * in the current context.
     */
    public PropertyMap getAnnotationMap(AnnotatedElement annotElem);

    /**
     * Returns the ClassLoader used to load the ControlBean class associated with the control
     * implementation instance.  This is useful for loading other classes or resources that may 
     * have been packaged with the public interfaces of the Control type (since they may not 
     * necessarily have been packaged directly with the implementation class).
     */
    public java.lang.ClassLoader getClassLoader();

    /**
     * Returns true if this container guarantees single-threaded behaviour.
     */
    public boolean isSingleThreadedContainer();

    /**
     * Returns the peer ControlBean associated with this ControlBeanContext.  If the context
     * represents a top-level container (i.e. not a Control containing other controls), null
     * will be returned.
     */
    public ControlBean getControlBean();
     
    /**
     * Returns any child ControlBean that is nested in the ControlBeanContext, or null
     * if no matching child is found.  The <code>id</code> parameter is relative to
     * the current nesting context, not an absolute control id.
     */
    public ControlBean getBean(String id);

    /**
     * The Lifecycle event interface defines a set of lifecycle events exposed by the
     * ControlBeanContext to any registered listener.
     */
    @EventSet
    public interface LifeCycle
    {
        /**
         * The onCreate event is delivered when the control implementation instance for
         * the associated bean has been instantiated and fully initialized.
         */
        public void onCreate();

        /**
         * The onPropertyChange event is delivered when a property setter method is
         * called for a bound property on the Java Control.
         *
         * @see org.apache.beehive.controls.api.packaging.PropertyInfo
         */
        public void onPropertyChange(PropertyChangeEvent pce);

        /**
         * The onVetoableChange event is delivered when a property setter method is
         * called for a constrained property on the Java Control.  A PropertyVetoException
         * may be thrown to veto the change made by the client.
         *
         * @see org.apache.beehive.controls.api.packaging.PropertyInfo
         */
        public void onVetoableChange(PropertyChangeEvent pce) throws PropertyVetoException;
    }

    /**
     * Registers a new listener for LifeCycle events on the context.
     *
     * @see org.apache.beehive.controls.api.context.ControlBeanContext.LifeCycle
     */
    public void addLifeCycleListener(LifeCycle listener);

    /**
     * Removes a currently registered LifeCycle event listener on the context.
     *
     * @see org.apache.beehive.controls.api.context.ControlBeanContext.LifeCycle
     */
    public void removeLifeCycleListener(LifeCycle listener);
}