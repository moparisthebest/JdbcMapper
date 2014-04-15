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
package org.apache.beehive.controls.system.webservice.units.rpcenc.schematypes;

import javax.xml.rpc.holders.IntHolder;
import javax.xml.rpc.holders.StringHolder;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

/**
 * Header tests for rpc/enc services.
 */
public class HeaderRpcEncEndpointTest extends ControlTestCase {

    @Control
    public schematypestest.HeaderRpcEncEndpointService client;

    /**
     * Test echoing a header value as a result.
     * @throws Exception
     */
    public void testEchoReturnStringHeader() throws Exception {
        String headerVal = "Round tripd";
        String result = client.echoReturnStringHeader(headerVal);
        assertEquals(headerVal, result);
    }

    /**
     * Test modifing a header value.
     * @throws Exception
     */
    public void testModifyStringHeader() throws Exception {
        StringHolder gh = new StringHolder("foo");
        client.modifyStringHeader(gh);
        assertEquals("Header Set By Service!", gh.value);

    }

    /**
     * Test echoing a header value as a new header and result.
     * @throws Exception
     */
    public void testEchoStringHeaderAndResult() throws Exception {
        String header = "xxxx";
        String param = "Hello Service!";
        StringHolder gh = new StringHolder(header);

        String result = client.echoStringHeaderAndResult(gh, param);
        assertEquals(param, result);

        String headerResult = gh.value;
        assertEquals("Header Set By Service!", headerResult);
    }

    /**
     * Test swapping the values of two headers.
     * @throws Exception
     */
    public void testSwapHeaderStrings() throws Exception {
        String header1 = "foo";
        String header2 = "bar";
        StringHolder h1 = new StringHolder(header1);
        StringHolder h2 = new StringHolder(header2);

        client.swapHeaderStrings(h1, h2);
        assertEquals(header2, h1.value);
        assertEquals(header1, h2.value);
    }

    /**
     * Test setting header values.
     * @throws Exception
     */
    public void testSetHeaderStrings() throws Exception {
        String headerValue = "foo/bar";
        StringHolder h1 = new StringHolder("empty");
        StringHolder h2 = new StringHolder("empty");

        client.setHeaderStrings(headerValue, h1, h2);

        assertEquals(headerValue, h1.value);
        assertEquals(headerValue, h2.value);
    }

    /**
     * Test echoing an int header value.
     * @throws Exception
     */
    public void testEchointInHeader() throws Exception {
        final int value = 1234;
        IntHolder ih = new IntHolder(-1);
        int result = client.echointHeader(ih, value);

        assertEquals(value, result);
        assertEquals(value, ih.value);
    }

    public static Test suite() {
        return new TestSuite(HeaderRpcEncEndpointTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}