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

package org.apache.beehive.controls.system.jdbc.units.results;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.system.jdbc.test.results.ResultsTestCtrl;
import org.apache.beehive.controls.system.jdbc.test.results.TxTestCtrl;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import test.customerDb.XStoogeRowDocument;
import test.customerDb.SMLXSizeType;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Transaction tests -- sanity check for db tx support
 */
public class TxResultsTest extends ControlTestCase {

    @Control
    public TxTestCtrl testCtrl;

    public void setUp() throws Exception {
        super.setUp();

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection conn = testCtrl.getConnection();
        Statement s = conn.createStatement();
        try {
            s.executeUpdate("drop table TX_USERS");
        } catch (Exception e) {
        }

        s.executeUpdate("create table TX_USERS (STOOGE_NAME varchar(32), STOOGE_PECKINGORDER int, STOOGE_PANTSIZE varchar(16))");
        conn = null;
    }

    /**
     * test transaction
     * @throws Exception
     */
    public void testTxSupport() throws Exception {
        Connection con = testCtrl.getConnection();
        con.setAutoCommit(false);
        testCtrl.insertUserRow("moe", 1, "small");
        con.commit();
    }

    /**
     * test test transaction commit
     * @throws Exception
     */
    public void testTxCommit() throws Exception {
        Connection con = testCtrl.getConnection();
        con.setAutoCommit(false);
        testCtrl.insertUserRow("larry", 1, "small");
        testCtrl.insertUserRow("shemp", 1, "small");
        con.commit();
    }

    /**
     * test transaction rollback
     * @throws Exception
     */
    public void testTxRollback() throws Exception {

        Connection con = testCtrl.getConnection();
        con.setAutoCommit(false);
        assertFalse(con.getAutoCommit());
        testCtrl.insertUserRow("curley", 1, "small");
        con.rollback();

        String nm = testCtrl.getAUser("curley");
        System.out.println("MN IS : x"+nm);
        assertNull(nm);
        con.commit();
    }

    public static Test suite() { return new TestSuite(TxResultsTest.class); }
    public static void main(String[] args) { junit.textui.TestRunner.run(suite()); }
}

