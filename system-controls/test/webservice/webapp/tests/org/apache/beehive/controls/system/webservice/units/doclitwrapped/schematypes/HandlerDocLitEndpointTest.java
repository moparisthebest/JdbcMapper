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
package org.apache.beehive.controls.system.webservice.units.doclitwrapped.schematypes;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.HandlerInfo;
import javax.xml.rpc.holders.StringHolder;
import java.util.ArrayList;

/**
 * Handler tests for doc/lit service.
 */
public class HandlerDocLitEndpointTest
        extends ControlTestCase {

    @Control
    public schematypestest.HandlerDocLitEndpointService client;

    /**
     * Test with an IN/OUT mode header which gets modified by the handler on request and response.
     * @throws Exception
     */
    public void testEchoStringHeader() throws Exception {

        QName[] headers = new QName[]{new QName("http://handlers", "inputHeader")};
        HandlerInfo hinfo = new HandlerInfo(handler.TestHeaderHandler.class, null, headers);
        ArrayList<HandlerInfo> chain = new ArrayList<HandlerInfo>();
        chain.add(hinfo);
        client.setHandlers(chain);

        StringHolder sh = new StringHolder("** Client **");
        client.echoStringHeader(sh);
        assertNotNull(sh);
        assertEquals("** Client **** RequestHandler **** Server **** ResponseHandler **", sh.value);
    }

    public static Test suite() {
        return new TestSuite(HandlerDocLitEndpointTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}