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

package org.apache.beehive.controls.test.controls.beancontextservices;

import java.beans.beancontext.BeanContextServicesListener;
import java.beans.beancontext.BeanContextServiceAvailableEvent;
import java.beans.beancontext.BeanContextServiceRevokedEvent;
import java.util.EventObject;
import java.util.LinkedList;

/**
 */
public class NonSerializableServiceListener implements BeanContextServicesListener {

    private LinkedList<EventObject> _recordedEvents;

    public NonSerializableServiceListener() {
        _recordedEvents = new LinkedList<EventObject>();
    }

    public EventObject[] getEvents() {
        EventObject[] result = new EventObject[_recordedEvents.size()];
        return _recordedEvents.toArray(result);
    }

    public void reset() {
        _recordedEvents.clear();
    }

    public void serviceAvailable(BeanContextServiceAvailableEvent bcsae) {
        _recordedEvents.add(bcsae);
    }

    public void serviceRevoked(BeanContextServiceRevokedEvent bcsre) {
        _recordedEvents.add(bcsre);
    }
}
