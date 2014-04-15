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

package org.apache.beehive.controls.test.controls.beancontextchild;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cschoett
 * Date: Apr 13, 2006
 * Time: 9:25:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ChangeListener implements PropertyChangeListener, VetoableChangeListener, Serializable {

    private LinkedList<PropertyChangeEvent> _recordedEvents;

    public ChangeListener() {
        _recordedEvents = new LinkedList<PropertyChangeEvent>();
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */

    public void propertyChange(PropertyChangeEvent evt) {
        _recordedEvents.add(evt);
    }

    /**
     * This method gets called when a constrained property is changed.
     *
     * @param evt a <code>PropertyChangeEvent</code> object describing the
     *            event source and the property that has changed.
     * @throws java.beans.PropertyVetoException
     *          if the recipient wishes the property
     *          change to be rolled back.
     */
    public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
        _recordedEvents.add(evt);
    }

    public PropertyChangeEvent[] getEvents() {
        PropertyChangeEvent[] result = new PropertyChangeEvent[_recordedEvents.size()];
        return _recordedEvents.toArray(result);
    }

    public void reset() {
        _recordedEvents.clear();
    }
}
