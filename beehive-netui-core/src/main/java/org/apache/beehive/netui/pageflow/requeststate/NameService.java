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
package org.apache.beehive.netui.pageflow.requeststate;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.beehive.netui.util.internal.ServletUtils;

/**
 * This class implements a service that will name and track objects which implement the
 * <code>INameable</code> interface.  The typical use of this class is in the XmlHttpRequest
 * request processing to lookup the object that should handle the request.
 */
final public class NameService implements java.io.Serializable
{
    private static final String NAME_SERVICE = "netui.nameService";
    public static final String NAME_SERVICE_MUTEX_ATTRIBUTE = NameService.class.getName() + ".MUTEX";

    // This is support for walking the _nameMap and reclaiming entries where
    // the weak reference object has been reclaimed by the garbage collector.
    // We will always reclaim at the reclaimPoint and after we run the reclaim we
    // set the next reclaimPoint to be the resulting size plus the reclaim increment.
    private final int _reclaimIncrement = 5;
    private int _reclaimPoint = _reclaimIncrement;

    // static value for situation where this is not stored in the session.
    private static NameService _nameService;

    private transient HashMap/*<String,WeakReference>*/ _nameMap = new HashMap();
    private int _nextValue;
    private ArrayList _listeners;

    /**
     * private constructor allowing for a factory method to access NameService objects.
     */
    private NameService() {
        _nextValue = 0;
    }

    /**
     * This will return the session specific instance of a NameService.  There
     * will only be a single NameService per session.
     * @param session the HttpSession that contains the NameService
     * @return the NameService associated with the session.
     */
    public static NameService instance(HttpSession session)
    {
        if (session == null)
            throw new IllegalArgumentException("Session must not be null");

        // Synchronize on a session scoped mutex to ensure that only a single
        // NameService object is created within a specific user session
        Object sessionMutex = ServletUtils.getSessionMutex(session, NAME_SERVICE_MUTEX_ATTRIBUTE);
        synchronized (sessionMutex) {
            NameService nameService = (NameService) session.getAttribute(NAME_SERVICE);
            if (nameService == null) {
                nameService = new NameService();
                session.setAttribute(NAME_SERVICE,nameService);
            }
            return nameService;
        }
    }

    /**
     * This will return a create a static name service.  This is used mainly to test the Name service
     * class.
     * @return The statically scoped <code>NameService</code>
     */
    public static synchronized NameService staticInstance()
    {
       if (_nameService == null)
            _nameService = new NameService();
        return _nameService;
    }

    /**
     * Deserialize an instance of this class.
     * @param in ObjectInputStream to deserialize from.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // the transient _nameMap is null after an instance of
        // NameService has been serialized.
        if (_nameMap == null) {
            _nameMap = new HashMap();
        }
    }

    /**
     * This method will add a <code>NamingObjectListener</code> to the set of listeners for the NamingObject event.
     * @param nol The <code>NamingObjectListener</code> to add as a listener.  This must not be null.
     * @throws IllegalArgumentException when nol is null.
     */
    public void addNamingObjectListener(NamingObjectListener nol) {
        if (nol == null)
            throw new IllegalArgumentException("The NameingObjectListener must not be null");

        ArrayList listener = getListener();

        synchronized(listener) {
            if (listener.contains(nol))
                return;
            listener.add(nol);
        }
    }

    /**
     * This method will remove a <code>NamingObjectListener</code> from the set of listeners.  If the
     * It is safe to call this if the NamingObjectListener hasn't been added to the listener list.
     * @param nol The <code>NamingObjectListener</code> to remove as a listener.  This must not be null.
     * @throws IllegalArgumentException when nol is null.
     */
    public void removeNamingObjectListener(NamingObjectListener nol) {
        if (nol == null)
            throw new IllegalArgumentException("The NameingObjectListener must not be null");

        ArrayList listener = getListener();
        synchronized(listener) {
            if (!listener.contains(nol))
                return;
            listener.remove(nol);
        }
    }

    /**
     * This method will create a unique name for an INameable object.  The name
     * will be unque within the session.  This will throw an IllegalStateException
     * if INameable.setObjectName has previously been called on object.
     * @param namePrefix The prefix of the generated name.
     * @param object the INameable object.
     * @throws IllegalStateException if this method is called more than once for an object
     */
    public synchronized void nameObject(String namePrefix, INameable object)
    {
        String name = namePrefix + Integer.toString(_nextValue++);
        object.setObjectName(name);
    }

    /**
     * This is a debug method that will set the next integer value.  This is used
     * so tests can force the name.
     * @param val the integer value that will be forced to be the next value.
     */
    public void debugSetNameIntValue(int val) {
        _nextValue = val;
    }

    /**
     * This method will store an INameable object into the <code>NameService</code>.   The name
     * is obtained from the INameable.  The object will be stored in the <code>NameService</code>
     * with a <code>WeakReference</code> so the <code>NameService</code> will not keep an object alive.
     * @param object The <code>INameable</code> to be stored in the name service.
     */
    public void put(INameable object) {
        if (object == null)
            throw new IllegalStateException("object must not be null");

        reclaimSpace();

        String name = object.getObjectName();
        if (name == null)
            throw new IllegalStateException("object has not been named");

        TrackingObject to = new TrackingObject();
        to.setINameable(new WeakReference(object));

        synchronized (_nameMap) { _nameMap.put(name,to); }

        // fire the fact that we just added a nameable to be tracked
        if (_listeners != null)
            fireNamingObjectEvent(to);
    }

    /**
     * Given the name, return the <code>INameable</code> object stored by the <code>NameService</code>.  Objects
     * are stored in the <code>NameService</code> using <code>WeakReference</code>s so this will not keep an object
     * alive.  If the object is not found or has been reclaimed, this method will return null.
     * @param name The name of the object to get from the <code>NameService</code>
     * @return INameable If the named object is stored by the name service, it will be returned otherwise
     * <code>null</code> is returned.
     */
    public INameable get(String name) {
        if (name == null)
            throw new IllegalStateException("name must not be null");

        if (_nameMap == null)
            return null;

        TrackingObject to = (TrackingObject) _nameMap.get(name);

        // The object wasn't found
        if (to == null)
            return null;

        // If the object has been reclaimed, then we remove the named object from the map.
        WeakReference wr = to.getWeakINameable();
        INameable o = (INameable) wr.get();
        if (o == null) {
            synchronized (_nameMap) { _nameMap.remove(name); }
            return null;
        }
        return o;
    }

    /**
     * This method will return the state map associated with the Nameable object if the
     * object has been stored in the <code>NameService</code> and something has been stored
     * into the <code>Map</code>.  Otherwise this will return null indicating that the map
     * is empty.  If the <code>create</code> parameter is true, we will always return the
     * <code>Map</code> object.
     * @param name The name of the object to return the named object.  This must not be null.
     * @param create This will create the map if necessary.
     * @return A Map Object for the named object.  This will return null if nothing has been stored in
     * the map and <code>create</code> is false.
     */
    public Map getMap(String name, boolean create) {
        if (name == null)
            throw new IllegalStateException("name must not be null");

        if (_nameMap == null)
            return null;

        TrackingObject to = (TrackingObject) _nameMap.get(name);

        // The object wasn't found
        if (to == null)
            return null;

        // If the object has been reclaimed, then we remove the named object from the map.
        WeakReference wr = to.getWeakINameable();
        INameable o = (INameable) wr.get();
        if (o == null) {
            synchronized (_nameMap) { _nameMap.remove(name); }
            return null;
        }
        if (create)
            return to;

        return to.isMapCreated() ? to : null;
    }

    /**
     * This mehtod will check the name map for entries where the
     * WeakReference object has been reclaimed.  When they are found it will
     * reclaim the entry in the map.
     */
    private void reclaimSpace()
    {
        if (_nameMap == null)
            return;

        if (_nameMap.size() > 0 && _nameMap.size() % _reclaimPoint == 0) {
            synchronized (_nameMap) {
                Set s = _nameMap.entrySet();
                Iterator it = s.iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();

                    TrackingObject to = (TrackingObject) e.getValue();

                   // If the object has been reclaimed, then we remove the named object from the map.
                    WeakReference wr = to.getWeakINameable();
                    INameable o = (INameable) wr.get();
                    if (o == null) {
                        it.remove();
                    }

                }
                _reclaimPoint = _nameMap.size() + _reclaimIncrement;
             }
        }
    }

    /**
     * This method will fire the NamingObject event.  This event is triggered when
     * we have added an element to be tracked.
     * @param to The <code>TrackingObject</code> that acts as the Map and also contains the INameable.
     */
    private void fireNamingObjectEvent(TrackingObject to)
    {
        Object[] copy;
        if (_listeners == null)
            return;

        // create a copy of the listeners so that there isn't any modifications while we
        // fire the events.
        synchronized (_listeners) { copy = _listeners.toArray(); }
        INameable o = (INameable) to.getWeakINameable().get();

        for (int i = 0; i < copy.length; i++) {
            ((NamingObjectListener)copy[i]).namingObject(o, to);
        }
    }

    /**
     * This method will return the event listener <code>ArrayList</code> when called.  The event listener is
     * lazily created.
     * @return The listener array list.
     */
    private ArrayList getListener()
    {
        if (_listeners != null)
            return _listeners;
        synchronized (this) {
            if (_listeners != null)
                return _listeners;
            _listeners = new ArrayList();
        }
        return _listeners;
    }

    final private class TrackingObject extends LazyMap
    {
        private WeakReference _nameable;

        public void setINameable(WeakReference nameable)
        {
            _nameable = nameable;
        }

        public WeakReference getWeakINameable() {
            return _nameable;
        }
    }
}
