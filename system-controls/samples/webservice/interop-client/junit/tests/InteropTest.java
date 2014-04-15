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

package tests;

import mssoapinterop.Round4XSDTest;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import org.apache.beehive.controls.api.bean.Control;

/**
 * Junit test case for the interop-client web service system control sample.
 */
public class InteropTest extends ControlTestCase {

    @Control
    private Round4XSDTest _interopCtrl;

    /**
     * Test echoing a string.
     */
    public void testEchoString() throws Exception {
        assertNotNull(_interopCtrl);
        assertEquals("HelloWorld", _interopCtrl.echoString("HelloWorld"));
    }

    /**
     * Test echoing an Integer.
     */
    public void testEchoInteger() throws Exception {
        assertNotNull(_interopCtrl);
        assertEquals(6, _interopCtrl.echoInteger(6));
    }
}
