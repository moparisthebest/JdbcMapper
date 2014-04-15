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

package org.apache.beehive.controls.test.junit.context;

import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.controls.context.BaseContextBean;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.util.Arrays;

/**
 * A TestCase that tests controls context services
 */
public class ContextTest extends ControlTestCase {

    @Control
    private BaseContextBean _contextBean;

    /**
     * Tests basic Context events for Controls
     */
    public void testContextEventSingle() throws Exception {

        _contextBean.hello("kyle");
        String [] expectedEvents = new String []{"BaseContextImpl.onCreate", "BaseContextImpl.onAcquire",
                "BaseContextImpl.hello kyle"};
        assertTrue(Arrays.equals(expectedEvents, _contextBean.getEventLog()));

    }

    /**
     * Tests basic Context events for Controls for multiple invocations in the same context
     */
    public void testContextEventMultiple() throws Exception {

        _contextBean.hello("ken");
        _contextBean.hello("mike");
        _contextBean.hello("lawrence");

        String[] expectedEvents = new String []{"BaseContextImpl.onCreate", "BaseContextImpl.onAcquire",
                "BaseContextImpl.hello ken", "BaseContextImpl.hello mike",
                "BaseContextImpl.hello lawrence"};
        assertTrue(Arrays.equals(expectedEvents, _contextBean.getEventLog()));
    }
}
