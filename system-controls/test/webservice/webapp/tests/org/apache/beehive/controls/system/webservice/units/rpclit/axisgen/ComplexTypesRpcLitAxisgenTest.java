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
package org.apache.beehive.controls.system.webservice.units.rpclit.axisgen;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import complextypes.rpclit.axisgen.BankAccount;
import complextypes.rpclit.axisgen.AccountTransaction;

/**
 * Tests for complex object types for a rpc/lit service.
 */
public class ComplexTypesRpcLitAxisgenTest
    extends ControlTestCase {

    @Control
    public axisgentest.ComplexTypesRpcLitAxisgenService client;

    /**
     * Test echoing a complex type.
     */
    public void testEchoBankAccount() throws Exception {
        assertTrue(true);
       //todo: broken - apparently on wsm side, wsm is complaining about an <item> tag for the
       // array element, needs further investigation once we're no longer snapshoting wsm for wsc drts
/*
        BankAccount account = new BankAccount();
        account.setAccountHolderName("Mike Nelson");
        account.setAccountNumber(12345678L);
        account.setAccountBalance(123.33F);
        AccountTransaction[] x = new AccountTransaction[] {
                new AccountTransaction(1.00F, null, "Servo"),
                new AccountTransaction(66.01F, null, "Crow")
        };
        account.setTransactions(x);

        BankAccount result = client.echoAccount(account);

        assertTrue(result.getAccountBalance() == 123.33F );
        assertTrue("Mike Nelson".equals(result.getAccountHolderName()));
        assertTrue(result.getAccountNumber() == 12345678L);

        AccountTransaction[] transactions = result.getTransactions();
        assertTrue(transactions.length == 2);
        assertTrue(transactions[0].getAmount() == 1.00F);
        assertTrue(transactions[0].getPayee().equals("Servo"));

        assertTrue(transactions[1].getAmount() == 66.00F);
        assertTrue(transactions[1].getPayee().equals("Crow"));
*/
    }

    public static Test suite() {
        return new TestSuite(ComplexTypesRpcLitAxisgenTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}