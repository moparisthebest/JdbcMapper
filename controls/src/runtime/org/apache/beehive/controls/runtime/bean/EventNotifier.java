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

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The EventNotifier class provides basic callback listener management and event delivery
 * services for ControlBean instances. 
 */
public class EventNotifier implements java.io.Serializable
{
    /**
     * Adds a new callback event listener for this EventNotifier
     */
    synchronized public void addListener(Object listener)
    {
        _listeners.add(listener);
    }

    /**
     * Remove an existing callback event listener for this EventNotifier
     */
    synchronized public void removeListener(Object listener)
    {
        if (!_listeners.contains(listener))
            throw new IllegalStateException("Invalid listener, not currently registered");

        _listeners.remove(listener);
    }

    /**
     * Returns an iterator over the full set of listeners
     */
    public Iterator listenerIterator()
    {
        return _listeners.iterator();
    }

    /**
     * Returns the number of registered listeners
     */
    public int getListenerCount()
    {
        return _listeners.size();
    }

    /**
     * Returns the listener list in array form
     */
    public void getListeners(Object [] listeners)
    {
        _listeners.toArray(listeners);
    }

    private LinkedList _listeners = new LinkedList();
}
