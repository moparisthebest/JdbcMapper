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
import org.apache.beehive.controls.test.controls.generic.SimpleControlBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.util.Vector;

/**
 * A TestCase that tests Java generics on controls
 */
public class SimpleControlTest extends ControlTestCase
{
    /**
     * A control that contains a nested control
     */
    @Control
    private SimpleControlBean<String> stringBean;

    /**
     * Tests instantiating SimpleControlBean declaratively
     */
    public void testDeclarative() throws Exception
    {
        // Test invoking a bound operation, this should also result in the above event
        // handler being called in a bound fashion
        Vector<String> strings=new Vector<String>();
        strings.add("One");
        strings.add("Two");

        stringBean.setThem(strings);

        assertEquals("One", stringBean.getTheFirst());
    }

    /**
     * Tests instantiating SimpleControlBean programmatically
     */
    public void testProgrammatic() throws Exception
    {
        Vector<String> strings=new Vector<String>();
        strings.add("One");
        strings.add("Two");


        SimpleControlBean<String> sbean=(SimpleControlBean<String>)java.beans.Beans.instantiate(
            Thread.currentThread().getContextClassLoader() ,
            "org.apache.beehive.controls.test.controls.generic.SimpleControlBean");

        sbean.setThem(strings);
        assertEquals("One", sbean.getTheFirst());
    }

}
