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
package org.apache.beehive.controls.system.webservice.units.rpclit.xmlbeangen;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import org.apache.beehive.controls.system.webservice.units.doclitwrapped.xmlbeangen.FaultsDocLitWrappedXmlbeangenTest;
import org.apache.axis.AxisFault;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * Tests for faults from a rpc/lit service.
 */
public class FaultsRpcLitXmlbeangenTest
    extends ControlTestCase {

    @Control
    public xmlbeangentest.ComplexTypesRpcLitXmlbeangenService client;

    /**
     * Test fault handling.
     * @throws Exception
     */
    public void testThrowAccountException() throws Exception {

        try {
            client.throwAccountException(66);
        } catch (Exception e) {
            assertTrue(e instanceof AxisFault);
            assertTrue("org.apache.beehive.complextypes.AccountException: AccountException; input value=\"66\"".equals(e.getMessage()));
            return;
        }
        fail("Expected AccountException to be thrown!");
    }

    /**
     * Test complex exception/fault handling.
     * @throws Exception
     */
    public void testThrowComplexAccountException() throws Exception {
        try {
            client.throwComplexAccountException();
        } catch (Exception e) {
            assertTrue(e instanceof AxisFault);
            Element[] el = ((AxisFault)e).getFaultDetails();
            assertTrue(el != null);

            NodeList children = el[0].getChildNodes();
            assertTrue(children.getLength() == 2);

            Node child = children.item(0);
            assertTrue("msg1".equals(child.getLocalName()));
            assertTrue("FirstMessage".equals(child.getFirstChild().getNodeValue()));

            child = children.item(1);
            assertTrue("msg2".equals(child.getLocalName()));
            assertTrue("SecondMessage".equals(child.getFirstChild().getNodeValue()));
             return;
        }
        fail("Expected ComplexAccountException to be thrown!");
    }

    public static Test suite() {
        return new TestSuite(FaultsRpcLitXmlbeangenTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}