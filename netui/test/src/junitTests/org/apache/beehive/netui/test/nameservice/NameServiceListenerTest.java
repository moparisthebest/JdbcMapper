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
package org.apache.beehive.netui.test.nameservice;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.netui.pageflow.requeststate.NameService;
import org.apache.beehive.netui.pageflow.requeststate.NamingObjectListener;
import org.apache.beehive.netui.pageflow.requeststate.INameable;

import java.util.Map;

/**
 */
public class NameServiceListenerTest extends TestCase
{
    public NameServiceListenerTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(NameServiceListenerTest.class);
    }

    public void testNamingListeners()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        // add the listener
        ListenerObject lo = new ListenerObject();
        ns.addNamingObjectListener(lo);

        // remove the listener
        ns.removeNamingObjectListener(lo);

        // the listener may be removed twice
        ns.removeNamingObjectListener(lo);
    }

    public void testNamingListenersEvents()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        ListenerObject lo1 = new ListenerObject();
        ns.addNamingObjectListener(lo1);

        // now add a nameable object and verify that the listener is called
        NameableObject no1 = new NameableObject();
        ns.nameObject("Nameable",no1);
        ns.put(no1);

        assertTrue("NameListener [1] = 1 (" + lo1.getEventCount() + ")", lo1.getEventCount() == 1);

        // now add a nameable object and verify that the listener is called
        NameableObject no2 = new NameableObject();
        ns.nameObject("Nameable",no2);
        ns.put(no2);

        assertTrue("NameListener [1] = 2 (" + lo1.getEventCount() + ")", lo1.getEventCount() == 2);

        // add another listener and verify that both are called
        ListenerObject lo2 = new ListenerObject();
        ns.addNamingObjectListener(lo2);

        // now add a nameable object and verify that the listener is called
        NameableObject no3 = new NameableObject();
        ns.nameObject("Nameable",no3);
        ns.put(no3);

        assertTrue("NameListener [1] = 3 (" + lo1.getEventCount() + ")", lo1.getEventCount() == 3);
        assertTrue("NameListener [2] = 1 (" + lo1.getEventCount() + ")", lo2.getEventCount() == 1);

        // remove the first listender and verify that things work correctly
        ns.removeNamingObjectListener(lo1);

        // now add a nameable object and verify that the listener is called
        NameableObject no4 = new NameableObject();
        ns.nameObject("Nameable",no4);
        ns.put(no4);

        assertTrue("NameListener [1] = 3 (" + lo1.getEventCount() + ")", lo1.getEventCount() == 3);
        assertTrue("NameListener [2] = 2 (" + lo1.getEventCount() + ")", lo2.getEventCount() == 2);

        // remove the second listender and verify that things work correctly
        ns.removeNamingObjectListener(lo2);

        // now add a nameable object and verify that the listener is called
        NameableObject no5 = new NameableObject();
        ns.nameObject("Nameable",no5);
        ns.put(no5);

        assertTrue("NameListener [1] = 3 (" + lo1.getEventCount() + ")", lo1.getEventCount() == 3);
        assertTrue("NameListener [2] = 2 (" + lo1.getEventCount() + ")", lo2.getEventCount() == 2);
    }

    public void testApplingStateToNamedObject()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        StatefulListenerObject listner = new StatefulListenerObject();
        ns.addNamingObjectListener(listner);

        try {
            NameableObject no = new NameableObject();
            ns.nameObject("Nameable",no);
            ns.put(no);

            Map map = ns.getMap(no.getObjectName(), false);
            assertNotNull("The returned Map was null", map);

            // verify that there was state stored in the map by the listener
            Object o = map.get(StatefulListenerObject.STATE_NAME);
            assertNotNull("The state was not found", o);
            assertTrue("The state was not the nameable's name", no.getObjectName().equals(o));
        }
        finally {
            ns.removeNamingObjectListener(listner);
        }
    }

    public void testApplingStateToNamedObject2()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        NameableObject no = new NameableObject();
        ns.nameObject("Nameable",no);
        ns.put(no);

        Map map = ns.getMap(no.getObjectName(), false);
        assertNull("The returned Map was not null", map);

        map = ns.getMap(no.getObjectName(), true);
        assertNotNull("The returned Map was null when create is true", map);

        // place something into the map
        map.put("x","x");

        // now that the map has something in it, it should return
        map = ns.getMap(no.getObjectName(), false);
        assertNotNull("The returned Map was null", map);

        Object o = map.get("x");
        assertNotNull("The state was not found", o);
        assertTrue("The state was not the set state", "x".equals(o));        
    }

    public void testNameServiceListenerErrors()
    {
        NameService ns = NameService.staticInstance();
        assertNotNull("The NameService was returned as null", ns);

        boolean error = true;

        // cannot add a null listener object
        try {
            ns.addNamingObjectListener(null);
        }
        catch (IllegalArgumentException e) {
            error = false;
        }
        assertFalse("Able to add a null listener object",error);

        // cannot remove a null listener object
        try {
            ns.removeNamingObjectListener(null);
        }
        catch (IllegalArgumentException e) {
            error = false;
        }
        assertFalse("Able to remove a null listener object",error);
    }

    static class ListenerObject implements NamingObjectListener
    {
        private int _cnt = 0;
        public void namingObject(INameable iNameable, Map map) {
            _cnt++;
        }

        public int getEventCount() {
            return _cnt;
        }
    }

    static class StatefulListenerObject implements NamingObjectListener
    {
        public static String STATE_NAME = "StatefulListenerObject.state";
        public void namingObject(INameable iNameable, Map map) {
            map.put(STATE_NAME, iNameable.getObjectName());
        }
    }
}
