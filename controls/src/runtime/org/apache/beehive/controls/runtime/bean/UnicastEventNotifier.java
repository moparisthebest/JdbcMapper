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

import java.util.TooManyListenersException;

/**
 * The UnicastEventNotifier class provides basic callback listener management and event delivery
 * services for unicast EventSets on ControlBean instances. 
 */
public class UnicastEventNotifier implements java.io.Serializable
{
    /**
     * Adds a new callback event listener for this EventNotifier.  This method will also
     * perform a check to see if there is already a register listener, and throw a
     * <code>java.util.TooManyListenersException</code> if there is already a registered
     * listener.
     */
    synchronized public void addListener(Object listener) throws TooManyListenersException
    {
        if (_listener != null)
             throw new TooManyListenersException("Callback listener is already registered");
        _listener = listener;
    }

    /**
     * Remove an existing callback event listener for this EventNotifier
     */
    synchronized public void removeListener(Object listener)
    {
        if (_listener != listener)
        {
            throw new IllegalStateException("Invalid listener, not currently registered");
        }
        _listener = null;
    }
                                                                                                    
    /**
     * Returns the listener associated with this EventNotifier
     */
    public Object getListener()
    {
        return _listener;
    }

   /**
     * Returns the number of registered listeners
     */
    public int getListenerCount()
    {
        return (_listener != null) ? 1 : 0;
    }

    /**
     * Returns the listener list in array form
     */
    public void getListeners(Object [] listeners)
    {
        if (_listener != null)
            listeners[0] = _listener;
    }

    private Object _listener;
}
