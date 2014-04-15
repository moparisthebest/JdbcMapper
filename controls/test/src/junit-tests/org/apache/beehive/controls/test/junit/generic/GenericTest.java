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

package org.apache.beehive.controls.test.junit.generic;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.test.controls.generic.ListControl;
import org.apache.beehive.controls.test.controls.generic.ListControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A TestCase that tests control composition.
 * The outer control is instantiated declaratively, and the outer
 * control instantiates the nested control declaratively
 * <p/>
 * Instantiating controls declaratively is not supported currently.
 * All tests are deactivated until this is supported
 */
public class GenericTest extends ControlTestCase {
    /**
     * A control that contains a nested control
     */
    @Control
    @ListControl.ListProps(listClass = ArrayList.class)
    private ListControlBean<String, ArrayList<String>> stringBean;

    /**
     * Declare a specific bound event handler to process events.  Note that the parameter and
     * return type are bound to 'String', based upon the field declaration binding
     * (E -> String) above
     */
    @EventHandler(field = "stringBean", eventSet = ListControl.ListEvents.class, eventName = "onAdd")
    public String onAdd(String addString) {
        _addString = addString;
        return addString;
    }

    private String _addString;

    /**
     * Tests usage of a generic control type/event handler that are declaratively instantiated
     */
    public void testDeclarative() throws Exception {
        // Test invoking a bound operation, this should also result in the above event
        // handler being called in a bound fashion
        stringBean.add("foo");
        assertEquals("foo", _addString);
    }

    private int _newInt;

    /**
     * Tests usage of a generic control type/event handler that are programmatic instantiated
     */
    public void testProgrammatic() throws Exception {
        ListControlBean<Integer, LinkedList<Integer>> intBean = new ListControlBean<Integer, LinkedList<Integer>>();
        intBean.setListClass(LinkedList.class);
        intBean.addListEventsListener(
                new ListControl.ListEvents<Integer>() {
                    public Integer onAdd(Integer newInt) {
                        _newInt = newInt;
                        return newInt;
                    }
                }
        );
        intBean.add(3);
        assertEquals(3, _newInt);
    }
}
