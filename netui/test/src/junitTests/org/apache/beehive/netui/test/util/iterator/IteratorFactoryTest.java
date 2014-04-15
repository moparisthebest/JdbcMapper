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
package org.apache.beehive.netui.test.util.iterator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.sql.ResultSet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.beehive.netui.util.iterator.*;
import org.apache.beehive.netui.test.util.config.TestConfigUtil;

/**
 *
 */
public class IteratorFactoryTest
    extends TestCase {

    public void testMapIterator() {
        Map map = new HashMap();

        Iterator i = IteratorFactory.createIterator(map);
        assertTrue(i instanceof MapIterator);
        assertNotNull(i);
    }

    public void testAtomicObjectIterator() {
        String s = "atomic";

        Iterator i = IteratorFactory.createIterator(s);
        assertTrue(i instanceof AtomicObjectIterator);
        assertNotNull(i);

        i = IteratorFactory.createIterator(null);
        assertNull(i);
    }

    public void testResultSetIterator() {
        ResultSet rs = ResultSetTestSupport.getResultSet();
        Iterator iterator = IteratorFactory.createIterator(rs);

        assertNotNull(iterator);
        assertTrue(iterator.hasNext());

        Map map = (Map)iterator.next();
        assertNotNull(map);
        assertEquals("bob", map.get("name"));
        assertEquals(new Integer(1), map.get("id"));
        assertTrue(iterator.hasNext());

        int rowCount = 1;
        while(iterator.hasNext()) {
            map = (Map)iterator.next();
            rowCount++;
        }

        assertNotNull(map);
        assertEquals("harry", map.get("name"));
        assertEquals(new Integer(6), map.get("id"));
        assertFalse(iterator.hasNext());

        assertEquals(6, rowCount);        
    }

    public IteratorFactoryTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(IteratorFactoryTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        TestConfigUtil.testInit();
    }

    protected void tearDown() {
    }
}
