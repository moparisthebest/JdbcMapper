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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import javax.ejb.EJBLocalObject;
import javax.ejb.EJBObject;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlImplementation;

/**
 * The EntityEJBControlImpl class is the control implementation class for
 * Entity EJBs.
 */
@ControlImplementation(assembler = EJBControlAssembler.class)
public class EntityEJBControlImpl
    extends EJBControlImpl
    implements EntityEJBControl, java.io.Serializable {

    static final long serialVersionUID = 1L;

    //
    // Implements auto-find semantics for entity beans
    //
    protected Object resolveBeanInstance() {
        // Already resolved and cached
        if (_beanInstance != null)
            return _beanInstance;

        // Attempt to resolve from a cached handle
        _beanInstance = resolveBeanInstanceFromHandle();
        if (_beanInstance != null)
            return _beanInstance;

        // Attempt to resolve bean instance from cached primary key
        if (_lastKey == null)
            throw new ControlException("Unable to locate a target bean instance, because a successful create or finder method has not been executed.");

        Class [] findArgType = new Class[]{_lastKey.getClass()};
        try {
            Method finder = _homeInterface.getMethod("findByPrimaryKey", findArgType);
            return finder.invoke(_homeInstance, new Object []{_lastKey});
        }
        catch (NoSuchMethodException nsme) {
            throw new ControlException("Unable to locate findByPrimaryKey method on home interface", nsme);
        }
        catch (InvocationTargetException ite) {
            _lastException = ite.getTargetException();
            throw new ControlException("Failure to locate entity instance associated with the last primary key", _lastException);
        }
        catch (Exception e) {
            throw new ControlException("Unexpected exception in auto-find", e);
        }
    }

    protected boolean saveBeanInstance() {
        //
        // First, delegate to parent (handle-based persistence)
        //
        if (super.saveBeanInstance())
            return true;

        //
        // Fall back to persistence using a saved primary key value
        //
        try {
            if (_beanInstance instanceof EJBObject)
                _lastKey = (Serializable) ((EJBObject) _beanInstance).getPrimaryKey();
            else
                _lastKey = (Serializable) ((EJBLocalObject) _beanInstance).getPrimaryKey();
        }
        catch (RemoteException re) {
            throw new ControlException("Unable to save bean instance", re);
        }
        return true;
    }

    //
    // Release the bean instance. Entity bean instances are *never* removed, except via
    // direct client calls to the remove method.
    //
    protected void releaseBeanInstance(boolean alreadyRemoved) {
        super.releaseBeanInstance(alreadyRemoved);

        // Release any cached primary key value
        _lastKey = null;
    }

    private boolean isMultiSelectorMethod(Method m) {
        return isFinderMethod(m) && m.getReturnType().isAssignableFrom(Collection.class);
    }

    //
    // This method wraps the base EJBControlImpl invoke, doing the additional
    // work to maintain the primary key cache for methods which alter the
    // target bean instance.
    //
    public Object invoke(Method m, Object [] args) throws Throwable {
        Throwable invokeException = null;
        Object retval = null;
        Object currentBeanInstance = _beanInstance;
        try {
            retval = super.invoke(m, args);
        }
        catch (Exception t) {
            // a tasty treat, but I'll throw up later
            invokeException = t;
        }

        if (isControlBeanMethod(m)) {
            m = mapControlBeanMethodToEJB(m);
        }

        if (isMultiSelectorMethod(m)) {
            releaseBeanInstance(false);

            Collection collection = (Collection) retval;
            if (collection != null && !collection.isEmpty()) {
                _colIter = collection.iterator();
                _beanInstance = beanNarrow(_colIter.next());
            }
            else releaseBeanInstance(false);
        }
        else if (isSelectorMethod(m)) {
            // Release collection results if a single select method is called.
            _colIter = null;
        }

        if (invokeException != null)
            throw invokeException;

        return retval;
    }

    /**
     * EntityEJBControl.getEJBNextBeanInstance()
     */
    public Object getEJBNextBeanInstance() {
        if (_colIter == null)
            return null;

        if (!_colIter.hasNext()) {
            releaseBeanInstance(false);
            return null;
        }

        _beanInstance = beanNarrow(_colIter.next());
        return _beanInstance;
    }

    //
    // Override the onCreate event handler that was already defined in EJBControlImpl to
    // add additional processing.
    //
    public void onCreate() {
        super.onCreate();
        if (_beanType != EJBControlImpl.ENTITY_BEAN) {
            throw new ControlException("Attempting to use a entity bean control with a bean that is not a entity bean");
        }
    }

    //
    // Override the onReset event handler that was already defined in EJBControlImpl to
    // add additional processing.
    //
    public void onReset() {
        super.onReset();
        _lastKey = null;
        _colIter = null;
    }

    private Serializable _lastKey;       // primary key of the selected instance
    private transient Iterator _colIter; // multi-finder result iterator
}
