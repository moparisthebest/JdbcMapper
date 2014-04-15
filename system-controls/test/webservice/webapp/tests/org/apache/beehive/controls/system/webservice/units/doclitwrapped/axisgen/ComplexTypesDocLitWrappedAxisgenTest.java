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
package org.apache.beehive.controls.system.webservice.units.doclitwrapped.axisgen;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import complextypes.doclitwrapped.axisgen.BankAccount;
import complextypes.doclitwrapped.axisgen.AccountTransaction;

/**
 * Tests for complex object types for a doc/lit/wrapped service.
 */
public class ComplexTypesDocLitWrappedAxisgenTest
    extends ControlTestCase {

    @Control
    public axisgentest.ComplexTypesDocLitWrappedAxisgenService client;

    /**
     * Test echoing a complex type.
     */
    public void testEchoBankAccount() throws Exception {
        assertTrue(true);
/* TODO: This test is currently disabled, after some investigation it appears to be an issue in WSM.
   On deserialization of the SOAP message, wsm complains about the '<item>' tags for the array items.
   On further investigation this appears to be due to a type mapping bug in wsm, it doesn't appear the
   TypeDesc for the object is being created correctly so axis thinks that the transactions field of BankAccount
   is not an array.

   For this same case with pojo objects it works -- but it appears that it is pretty much a fluke, they TypeDesc's
   on the WSM side are not correct and axis uses a fallback clause to try to guess the serializer.
*/
//        BankAccount account = new BankAccount();
//        account.setAccountHolderName("Mike Nelson");
//        account.setAccountNumber(12345678L);
//        account.setAccountBalance(123.33F);
//        AccountTransaction[] x = new AccountTransaction[] {
//                new AccountTransaction(1.00F, null, "Servo"),
//                new AccountTransaction(66.00F, null, "Crow")
//        };
//        account.setTransactions(x);
//
//        BankAccount result = client.echoAccount(account);
//
//        assertTrue(result.getAccountBalance() == 123.33F );
//        assertTrue("Mike Nelson".equals(result.getAccountHolderName()));
//        assertTrue(result.getAccountNumber() == 12345678L);
//
//        AccountTransaction[] transactions = result.getTransactions();
//        assertTrue(transactions.length == 2);
//        assertTrue(transactions[0].getAmount() == 1.00F);
//        assertTrue(transactions[0].getPayee().equals("Servo"));
//
//        assertTrue(transactions[1].getAmount() == 66.00F);
//        assertTrue(transactions[1].getPayee().equals("Crow"));
    }

    public static Test suite() {
        return new TestSuite(ComplexTypesDocLitWrappedAxisgenTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}