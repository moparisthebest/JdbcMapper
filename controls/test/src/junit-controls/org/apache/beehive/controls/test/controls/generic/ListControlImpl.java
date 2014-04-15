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

package org.apache.beehive.controls.test.controls.generic;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.context.Context;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.events.Client;
import org.apache.beehive.controls.api.events.EventHandler;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@ControlImplementation
public class ListControlImpl<E, T extends List<E>>
        implements ListControl<E, T>, Extensible, java.io.Serializable {
    @Context
    ControlBeanContext context;

    @EventHandler(field = "context", eventSet = ControlBeanContext.LifeCycle.class,
                  eventName = "onCreate")
    public void onCreate() {
    }

    @Client
    ListEvents<E> client;

    /**
     * Returns the list used to store control elements.  This is lazily instantiated, to
     * allow the list class to be set by either an annotation or by calling setListClass()
     * on the bean prior to manipulating the list.
     */
    private T getList() {
        if (_list == null) {
            Class<? extends List> listClass =
                    context.getControlPropertySet(ListProps.class).listClass();
            try {
                _list = (T) listClass.newInstance();
            }
            catch (Exception e) {
                throw new ControlException("Cannot create List", e);
            }
        }
        return _list;
    }

    public void add(E val) {
        getList().add(val);
        client.onAdd(val);
    }

    ;

    public <X extends E> boolean contains(X val) {
        return getList().contains(val);
    }

    public E get(int index) {
        return getList().get(index);
    }

    public T copy(Collection<? extends E> src) {
        T list = getList();
        for (E item : src)
            list.add(item);

        return list;
    }

    public <X extends E> E getLast(Collection<X> coll) {
        E last = null;
        Iterator<X> collIter = coll.iterator();
        while (collIter.hasNext())
            last = collIter.next();

        return last;
    }

    /**
     * TBD
     */
    public Object invoke(Method method, Object [] args) throws Throwable {
        // To Be Implemented
        //
        return null;
    }

    //
    // NEVER ACCESS DIRECTLY... ALWAYS CALL GETLIST()!!!!
    //
    T _list;
}
