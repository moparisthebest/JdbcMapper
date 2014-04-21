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
package org.apache.beehive.controls.system.ejb;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import javax.ejb.Handle;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.NameNotFoundException;
import javax.rmi.PortableRemoteObject;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.bean.ControlExtension;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ControlBeanContext.LifeCycle;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.context.ResourceContext.ResourceEvents;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * The Enterprise Java Bean Control implementation class
 */
@ControlImplementation
public abstract class EJBControlImpl
    implements EJBControl, Extensible, java.io.Serializable {

    static final long serialVersionUID = 1L;
    private static final Log LOGGER = LogFactory.getLog(EJBControlImpl.class);

    public static final int SESSION_BEAN = 1;
    public static final int ENTITY_BEAN = 2;

    public static final String JNDI_GLOBAL_PREFIX = "jndi:";
    public static final String JNDI_APPSCOPED_PREFIX = "java:comp/env/";

    @EventHandler(field = "context", eventSet = LifeCycle.class, eventName = "onCreate")
    public void onCreate() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: onCreate()");
        }

        EJBHome ejbHome = context.getControlPropertySet(EJBHome.class);
        if (ejbHome == null)
            throw new ControlException("No @EJBHome property is defined");

        _jndiName = ejbHome.jndiName();
        if (_jndiName == null || _jndiName.length() == 0) {
            String ejbLink = ejbHome.ejbLink();
            if (ejbLink.length() == 0) {
                //
                // Should be caught by the compiler
                //
                throw new ControlException("Either the jndiName() or ejbLink() member of @EJBHome must be defined.");
            }

            //
            // Generate a unique local jndi name to associate w/ the link,
            // based upon the local control service uri and control id
            //
            _jndiName = JNDI_APPSCOPED_PREFIX + EJBInfo.getEJBRefName(context.getControlInterface());
        }

        // Obtain the JCX interface and identify the home/remote
        // interfaces.
        EJBInfo beanInfo = new EJBInfo(context.getControlInterface());
        _homeInterface = beanInfo._homeInterface;
        _beanInterface = beanInfo._beanInterface;
        _beanType = beanInfo._beanType.equals("Session") ? SESSION_BEAN : ENTITY_BEAN;
    }

    protected static boolean methodThrows(Method m, Class exceptionClass) {
        Class [] exceptions = m.getExceptionTypes();
        for (int j = 0; j < exceptions.length; j++)
            if (exceptionClass.isAssignableFrom(exceptions[j]))
                return true;
        return false;
    }

    protected boolean isHomeMethod(Method m) {
        return m.getDeclaringClass().isAssignableFrom(_homeInterface);
    }

    /**
     * Return true if the method is from the ControlBean.
     * @param m Method to check.
     */
    protected boolean isControlBeanMethod(Method m) {
        return (m.getDeclaringClass().getAnnotation(ControlExtension.class) != null);
    }

    /**
     * Map a control bean method to an EJB method.
     *
     * @param m The control bean method.
     * @return The corresponding method of the EJB.
     */
    protected Method mapControlBeanMethodToEJB(Method m) {
        Method ejbMethod = findEjbMethod(m, _homeInterface);
        if (ejbMethod == null) {
            if (_beanInstance == null) {
                _beanInstance = resolveBeanInstance();
                if (_beanInstance == null) {
                    throw new ControlException("Unable to resolve bean instance");
                }
            }
            ejbMethod = findEjbMethod(m, _beanInstance.getClass());
            if (ejbMethod == null) {
                throw new ControlException("Unable to map ejb control interface method to EJB method: " + m.getName());
            }
        }
        return ejbMethod;
    }

    /**
     * Find the method which has the same signature in the specified class.
     *
     * @param controlBeanMethod Method signature find.
     * @param ejbInterface Class to search for method signature.
     * @return Method from ejbInterface if found, null if not found.
     */
    protected Method findEjbMethod(Method controlBeanMethod, Class ejbInterface) {
        final String cbMethodName = controlBeanMethod.getName();
        final Class cbMethodReturnType = controlBeanMethod.getReturnType();
        final Class[] cbMethodParams = controlBeanMethod.getParameterTypes();

        Method[] ejbMethods = ejbInterface.getMethods();
        for (Method m : ejbMethods) {
            if (!cbMethodName.equals(m.getName())
                    || !cbMethodReturnType.equals(m.getReturnType())) {
                continue;
            }

            Class[] params = m.getParameterTypes();
            if (cbMethodParams.length == params.length) {
                int i;
                for (i = 0; i < cbMethodParams.length; i++) {
                    if (cbMethodParams[i] != params[i]) break;
                }
                if (i == cbMethodParams.length)
                    return m;
            }
        }
        return null;
    }

    protected static boolean isCreateMethod(Method m) {
        return methodThrows(m, CreateException.class);
    }

    protected static boolean isFinderMethod(Method m) {
        if (!m.getName().startsWith("find")) // EJB enforced pattern
            return false;
        return methodThrows(m, FinderException.class);
    }

    protected boolean isSelectorMethod(Method m) {
        return isHomeMethod(m) && m.getReturnType().equals(_beanInterface);
    }

    static protected boolean isRemoveMethod(Method m) {
        if (!m.getName().equals("remove") || (m.getParameterTypes().length != 0))
            return false;
        else return true;
    }

    protected Object homeNarrow(Object obj) {
        if (javax.ejb.EJBHome.class.isAssignableFrom(_homeInterface))
            return PortableRemoteObject.narrow(obj, _homeInterface);
        else return obj;
    }

    protected Object beanNarrow(Object obj) {
        if (javax.ejb.EJBObject.class.isAssignableFrom(_beanInterface))
            return PortableRemoteObject.narrow(obj, _beanInterface);
        else return obj;
    }

    /*
     * This method is implemented by the appropriate bean type-specific
     * control to provide auto create/find semantics for bean instances.
     *
     * IT SHOULD ALWAYS THROW A RUNTIME EXCEPTION WITH A TYPE-SPECIFIC
     * ERROR MESSAGE IF RESOLUTION CANNOT TAKE PLACE.  IT SHOULD _NEVER_
     * HAVE A NON-EXCEPTED RETURN WHERE _beanInstance == null.
     */
    abstract protected Object resolveBeanInstance();

    //
    // Is there is a cached EJB handle associated with this bean, then
    // is it to restore the associate EJB object reference.
    //
    protected Object resolveBeanInstanceFromHandle() {
        if (_beanHandle == null)
            return null;

        try {
            return _beanHandle.getEJBObject();
        }
        catch (java.rmi.RemoteException re) {
            throw new ControlException("Unable to convert EJB handle to object", re);
        }
    }

    //
    // Attempts to save the contents of the current bean reference in persisted
    // control state.  Returns true if state could be saved, false otherwise
    //
    protected boolean saveBeanInstance() {
        // Nothing to save == success
        if (_beanInstance == null)
            return true;

        //
        // Save using a bean handle, but handles only exist for remote objects.
        //
        if (_beanInstance instanceof EJBObject) {
            try {
                _beanHandle = ((EJBObject) _beanInstance).getHandle();
            }
            catch (java.rmi.RemoteException re) {
                throw new ControlException("Unable to get bean instance from handle", re);
            }

            return true;
        }
        return false;
    }

    //
    // This is called whenever a bean reference is being dropped, and is the
    // provides an opportunity to reset cached state or release non-persisted
    // resources associated with the instance.
    //
    protected void releaseBeanInstance(boolean alreadyRemoved) {
        _beanInstance = null;
        _beanHandle = null;
    }

    protected javax.naming.Context getInitialContext() throws NamingException {
        if (_context == null) {
            //If naming context information is provided, then use that to create the initial context
            JNDIContextEnv env = context.getControlPropertySet(JNDIContextEnv.class);
            String value = env.contextFactory();
            if (value != null && value.length() > 0) {
                Hashtable<String, String> ht = new Hashtable<String, String>();
                ht.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, value);
                value = env.providerURL();
                if (value != null && value.length() > 0)
                    ht.put(javax.naming.Context.PROVIDER_URL, value);
                value = env.principal();
                if (value != null && value.length() > 0)
                    ht.put(javax.naming.Context.SECURITY_PRINCIPAL, value);
                value = env.credentials();
                if (value != null && value.length() > 0)
                    ht.put(javax.naming.Context.SECURITY_CREDENTIALS, value);
                _context = new InitialContext(ht);
            }
            else {
                _context = new InitialContext();
            }
        }
        return _context;

    }

    @EventHandler(field = "resourceContext", eventSet = ResourceEvents.class, eventName = "onAcquire")
    public void onAcquire() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: onAquire()");
        }

        // Compute the home instance cache lookup key.  The Service URI must
        // be taken into account because different services use different
        // class loaders.  The JNDI home must be taken into account because
        // it is possible to be a remote client of the same bean type on two
        // different providers.
        //
        if (_homeInstance == null) {
            // If JNDI name is an URL using a JNDI protocol
            if (_jndiName.toLowerCase().startsWith(JNDI_GLOBAL_PREFIX)) {

                try {
                    URL url = new URL(_jndiName);
                    URLConnection jndiConn = url.openConnection();
                    _homeInstance = jndiConn.getContent();
                }
                catch (MalformedURLException mue) {
                    throw new ControlException(_jndiName + " is not a valid JNDI URL", mue);
                }
                catch (IOException ioe) {
                    throw new ControlException("Error during JNDI lookup from " + _jndiName, ioe);
                }
            }
            else {

                try {
                    _homeInstance = lookupHomeInstance();
                }
                catch (NamingException ne) {
                    throw new ControlException("Error during JNDI lookup from " + _jndiName, ne);
                }
            }

            if (!_homeInterface.isAssignableFrom(_homeInstance.getClass())) {
                throw new ControlException("JNDI lookup of " + _jndiName + " failed to return an instance of " + _homeInterface);
            }
        }
    }

    @EventHandler(field = "resourceContext", eventSet = ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: onRelease()");
        }
        releaseBeanInstance(false);
    }

    //@EventHandler(field="context", eventSet=LifeCycle.class, eventName="onReset")
    public void onReset() {
        _lastException = null;
        // other work in onRelease(), delivered prior to reset event
    }

    /**
     * Extensible.invoke
     * Handles all extended interface methods (i.e. EJB home and remote
     * interface invocation)
     */
    public Object invoke(Method m, Object[] args) throws Throwable {
        Object retval = null;

        if (isControlBeanMethod(m)) {
            m = mapControlBeanMethodToEJB(m);
        }

        if (isHomeMethod(m)) {
            try {
                retval = m.invoke(_homeInstance, args);
            }
            catch (Exception e) {
                Throwable t = e;
                if (e instanceof InvocationTargetException)
                    t = ((InvocationTargetException) e).getTargetException();
                _lastException = t;
                throw t;
            }

            // If the method was successful and returns an instance of
            // the bean interface class, then reset the target instance.
            if (isSelectorMethod(m)) {
                releaseBeanInstance(false);
                retval = beanNarrow(retval);
                _beanInstance = retval;
            }

            return retval;
        }
        // is remote / bean interface
        else {
            if (_beanInstance == null)
                _beanInstance = resolveBeanInstance();

            // By convention, the below cond should never be true.  The bean
            // type-specific resolve should throw an appropriate exception
            // that is more specific.  This is a safety net.
            if (_beanInstance == null)
                throw new ControlException("Unable to resolve bean instance");

            try {
                return m.invoke(_beanInstance, args);
            }
            catch (Exception e) {
                Throwable t = e;
                if (e instanceof InvocationTargetException)
                    t = ((InvocationTargetException) e).getTargetException();
                _lastException = t;

                throw t;
            }
            finally {
                // Handle remove method properly
                if (isRemoveMethod(m))
                    releaseBeanInstance(true);
            }
        }
    }

    /**
     * EJBControl.getEJBHomeInstance()
     */
    public Object getEJBHomeInstance() {
        return _homeInstance;
    }

    /**
     * EJBControl.getEJBBeanInstance()
     */
    public boolean hasEJBBeanInstance() {
        return _beanInstance != null;
    }

    /**
     * EJBControl.getEJBBeanInstance()
     */
    public Object getEJBBeanInstance() {
        return _beanInstance;
    }

    /**
     * EJBControl.getEJBException()
     */
    public Throwable getEJBException() {
        return _lastException;
    }

    /**
     * Do a JNDI lookup for the home instance of the ejb.  Attempt the lookup
     * first using an app scoped jndi prefix, if not successful, attempt without
     * the prefix.
     *
     * @return HomeInstance object.
     * @throws NamingException If HomeInstance cannot be found in JNDI registry.
     */
    private Object lookupHomeInstance() throws NamingException {

        javax.naming.Context ctx = getInitialContext();

        try {
            return ctx.lookup(_jndiName);
        }
        catch (NameNotFoundException nnfe) {
            // attempt again without application scoping
            if (!_jndiName.startsWith(JNDI_APPSCOPED_PREFIX)) {
                throw nnfe;
            }
        }
        return ctx.lookup(_jndiName.substring(JNDI_APPSCOPED_PREFIX.length()));
    }

    @Context
    ControlBeanContext context;

    @Context
    ResourceContext resourceContext;

    protected Class _controlInterface;
    protected Class _homeInterface;
    protected Class _beanInterface;
    protected int _beanType;
    protected String _jndiName;
    protected Handle _beanHandle;
    protected transient javax.naming.Context _context; // don't persist
    protected transient Throwable _lastException;      // don't persist
    protected transient Object _beanInstance;          // don't persist
    protected transient Object _homeInstance;          // don't persist
}
