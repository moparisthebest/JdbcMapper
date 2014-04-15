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

package org.apache.beehive.controls.test.controls.beancontext;

import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.beancontext.BeanContextMembershipListener;
import java.beans.beancontext.BeanContextMembershipEvent;
import java.util.LinkedList;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: cschoett
 * Date: Apr 13, 2006
 * Time: 9:25:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class MembershipListener implements BeanContextMembershipListener, Serializable {

    private LinkedList<BeanContextMembershipEvent> _recordedEvents;

    public MembershipListener() {
        _recordedEvents = new LinkedList<BeanContextMembershipEvent>();
    }


    public BeanContextMembershipEvent[] getEvents() {
        BeanContextMembershipEvent[] result = new BeanContextMembershipEvent[_recordedEvents.size()];
        return _recordedEvents.toArray(result);
    }

    public void reset() {
        _recordedEvents.clear();
    }

    /**
     * Called when a child or list of children is added to a
     * <code>BeanContext</code> that this listener is registered with.
     *
     * @param bcme The <code>BeanContextMembershipEvent</code>
     *             describing the change that occurred.
     */
    public void childrenAdded(BeanContextMembershipEvent bcme)
    {
        _recordedEvents.add(bcme);
    }

    /**
     * Called when a child or list of children is removed
     * from a <code>BeanContext</code> that this listener
     * is registered with.
     *
     * @param bcme The <code>BeanContextMembershipEvent</code>
     *             describing the change that occurred.
     */
    public void childrenRemoved(BeanContextMembershipEvent bcme)
    {
        _recordedEvents.add(bcme);
    }
}
