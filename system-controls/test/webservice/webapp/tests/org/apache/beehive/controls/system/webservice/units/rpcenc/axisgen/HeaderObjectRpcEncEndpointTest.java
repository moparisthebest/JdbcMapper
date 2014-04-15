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
package org.apache.beehive.controls.system.webservice.units.rpcenc.axisgen;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import org.apache.beehive.controls.system.webservice.utils.GenericHolder;
import header.HeaderAddressAxisgen;

/**
 * Header tests for rpc/enc services.
 */
public class HeaderObjectRpcEncEndpointTest extends ControlTestCase {

    @Control
    public axisgentest.HeaderObjectRpcEncEndpointService client;

    /**
     * Test echoing an object header value.
     * @throws Exception
     */
    public void testEchoObject() throws Exception {
        header.HeaderAddressAxisgen h = new header.HeaderAddressAxisgen();
        h.setCity("Louisville");

        header.HeaderAddressAxisgen param = new header.HeaderAddressAxisgen();
        h.setCity("Denver");

        GenericHolder<header.HeaderAddressAxisgen> gh = new GenericHolder<HeaderAddressAxisgen>(h);
        header.HeaderAddressAxisgen result = client.echoObject(gh, param);

        assertEquals(param.getCity(), result.getCity());
        assertEquals("Boulder", gh.value.getCity());
    }

    public static Test suite() {
        return new TestSuite(HeaderObjectRpcEncEndpointTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}