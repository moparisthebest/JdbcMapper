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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.ejb.EJBObject;
import javax.ejb.EJBLocalObject;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlImplementation;

/**
 * The SessionEJBControlImpl class is the control implementation class for
 * Stateless/Stateful Session EJBs.
 * <p/>
 * Currently, this is a noop since no session-bean specific control APIs
 * are defined, but having a unique control interface/impl class still allows
 * JellyBeans to create two different control types.
 */
@ControlImplementation(assembler = EJBControlAssembler.class)
public class SessionEJBControlImpl
    extends EJBControlImpl
    implements SessionEJBControl, java.io.Serializable {

    static final long serialVersionUID = 1L;

    /**
     * Override ejbControl.onCreate to perform additional processing
     */
    public void onCreate() {

        super.onCreate();
        if (_beanType != EJBControlImpl.SESSION_BEAN)
            throw new ControlException("Attempting to use a session bean control with a bean that is not a session bean");
    }

    /**
     * Implements auto-create semantics for Session beans.
     */
    protected Object resolveBeanInstance() {

        // First, try to resolve from a cached EJB handle (if any)
        Object fromHandle = resolveBeanInstanceFromHandle();
        if (fromHandle != null)
            return fromHandle;

        // Find null arg create() on the home interface, and use it to get an instance
        try {
            /* todo: for efficiency, this could be done once per home interface and cached */
            Method createMethod = _homeInterface.getMethod("create", new Class []{});
            Object beanInstance = createMethod.invoke(_homeInstance, (Object[])null);
            _autoCreated = true;
            return beanInstance;
        }
        catch (NoSuchMethodException e) {
            throw new ControlException("Cannot auto-create session bean instance because no null argument create() method exists.  To use this bean, you will need to call create() directly with the appropriate parameters");
        }
        catch (InvocationTargetException e) {
            _lastException = e.getTargetException();

            throw new ControlException("Unable to create session bean instance", _lastException);
        }
        catch (Exception e) {
            throw new ControlException("Unable to invoke home interface create method", e);
        }
    }

    protected void releaseBeanInstance(boolean alreadyRemoved) {
        //
        // For session EJBs, releasing the instance implies the physical
        // removal of the bean instance.  If the bean was auto-created by
        // the control, it has responsibility for deleting.
        //
        if (_beanInstance != null && _autoCreated && !alreadyRemoved) {
            try {
                if (EJBObject.class.isAssignableFrom(_beanInterface))
                    ((EJBObject) _beanInstance).remove();
                else
                    ((EJBLocalObject) _beanInstance).remove();
            }
            /* RemoteException or RemoveException */
            catch (Exception e) {
                _lastException = e;
            }
        }

        super.releaseBeanInstance(alreadyRemoved);
    }

    /**
     * Set to true if the EJB instance was autocreated as a result of a bean method
     * call (as opposed to direct invocation of the home create() method).
     */
    private boolean _autoCreated = false;
}
