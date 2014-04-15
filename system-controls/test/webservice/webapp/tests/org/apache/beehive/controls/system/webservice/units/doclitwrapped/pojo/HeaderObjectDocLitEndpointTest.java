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
package org.apache.beehive.controls.system.webservice.units.doclitwrapped.pojo;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import org.apache.beehive.controls.system.webservice.utils.GenericHolder;
import header.HeaderAddress;

/**
 * Header tests for doc/lit services.
 */
public class HeaderObjectDocLitEndpointTest
    extends ControlTestCase {

    @Control
    public pojotypetest.HeaderObjectDocLitEndpointService client;

    /**
     * Test echoing a object header value.
     */
    public void testEchoObject() throws Exception {
        header.HeaderAddress h = new header.HeaderAddress();
        h.setCity("Louisville");

        header.HeaderAddress param = new header.HeaderAddress();
        h.setCity("Denver");

        GenericHolder<HeaderAddress> gh = new GenericHolder<HeaderAddress>(h);
        header.HeaderAddress result = client.echoObject(gh, param);

        assertEquals(param.getCity(), result.getCity());
        assertEquals("Boulder", gh.value.getCity());
    }

    public static Test suite() {
        return new TestSuite(HeaderObjectDocLitEndpointTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}