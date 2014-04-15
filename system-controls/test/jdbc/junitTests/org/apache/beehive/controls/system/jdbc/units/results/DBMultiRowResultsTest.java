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
import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.system.jdbc.test.results.ResultsTestCtrl;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import javax.sql.RowSet;

/**
 * Tests for result sets which contain multiple rows.
 */
public class DBMultiRowResultsTest extends ControlTestCase {

    @Control
    private ResultsTestCtrl testCtrl;

    public void setUp() throws Exception {
        super.setUp();

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection conn = testCtrl.getConnection();
        Statement s = conn.createStatement();
        try {
            s.executeUpdate("DROP TABLE USERS");
        } catch (Exception e) {
        }

        s.executeUpdate("CREATE TABLE USERS (FNAME VARCHAR(32), USERID INT)");
        s.executeUpdate("INSERT INTO USERS VALUES ('tester1', 21)");
        s.executeUpdate("INSERT INTO USERS VALUES ('tester2', 22)");
        s.executeUpdate("INSERT INTO USERS VALUES ('tester3', 23)");
        s.executeUpdate("INSERT INTO USERS VALUES ('tester4', 24)");
        conn = null;
    }

    /**
     * test array return type
     * @throws Exception
     */
    public void testArrayReturnType() throws Exception {
        ResultsTestCtrl.Customer[] customers = testCtrl.getCustomerArray();
        assertNotNull(customers);
        assertEquals(4, customers.length);
        assertEquals("tester1", customers[0].getFname());
        assertEquals(24, customers[3].userid);
    }

    /**
     * test array return type, restricted array size returned (2)
     * @throws Exception
     */
    public void testArrayReturnTypeMaxSize() throws Exception {
        ResultsTestCtrl.Customer[] customers = testCtrl.getCustomerArrayLimitedSize();
        assertNotNull(customers);
        assertEquals(2, customers.length);
    }

    /**
     * test array return type, restricted array size returned (2), restricted maxrows (1)
     * @throws Exception
     */
    public void testArrayReturnTypeMaxSize2() throws Exception {
        ResultsTestCtrl.Customer[] customers = testCtrl.getCustomerArrayLimitedSize2();
        assertNotNull(customers);
        assertEquals(1, customers.length);
    }

    /**
     * test array return type, restricted array size returned (2), restricted maxrows (4)
     * @throws Exception
     */
    public void testArrayReturnTypeMaxSize3() throws Exception {
        ResultsTestCtrl.Customer[] customers = testCtrl.getCustomerArrayLimitedSize3();
        assertNotNull(customers);
        assertEquals(2, customers.length);
    }

    /**
     * test array return type, restricted array size returned (ALL), restricted maxrows (ALL)
     * @throws Exception
     */
    public void testArrayReturnTypeMaxSize4() throws Exception {
        ResultsTestCtrl.Customer[] customers = testCtrl.getCustomerArrayLimitedSize5();
        assertNotNull(customers);
        assertEquals(4, customers.length);
    }

    /**
     * test array return type, restricted array size returned (ALL)
     * @throws Exception
     */
    public void testArrayReturnTypeMaxSize5() throws Exception {
        ResultsTestCtrl.Customer[] customers = testCtrl.getCustomerArrayLimitedSize6();
        assertNotNull(customers);
        assertEquals(4, customers.length);
    }

    /**
     * test max rows, value of 1
     * @throws Exception
     */
    public void testMaxRows1() throws Exception {
        ResultsTestCtrl.Customer[] customers = testCtrl.getCustomerArrayLimitedSize4();
        assertNotNull(customers);
        assertEquals(1, customers.length);
    }

    /**
    /**
     * test array of HashMap return type
     * @throws Exception
     */
    public void testArrayHashMapReturnType() throws Exception {
        HashMap[] customerHashMap = testCtrl.getCustomerHashMapArray(22);
        assertTrue(customerHashMap.length > 0);

        assertEquals("tester2", customerHashMap[0].get("FNAME"));
        assertEquals(22, customerHashMap[0].get("USERID"));

        assertEquals("tester2", customerHashMap[0].get("fname"));
        assertEquals(22, customerHashMap[0].get("userid"));
    }

    /**
     * test EMPTY array of HashMap return type
     * @throws Exception
     */
    public void testEmptyArrayHashMapReturnType() throws Exception {
        HashMap[] customerHashMap = testCtrl.getCustomerHashMapArray(1000);
        assertEquals(0, customerHashMap.length);
    }

    /**
     * test Iterator return type
     * @throws Exception
     */
    public void testIteratorReturnType() throws Exception {
        Iterator customersIterator = testCtrl.getCustomerIterator();
        assertNotNull(customersIterator);
        int idCheck = 21;
        assertTrue(customersIterator.hasNext());
        while (customersIterator.hasNext()) {
            ResultsTestCtrl.Customer c = (ResultsTestCtrl.Customer) customersIterator.next();
            assertEquals(idCheck, c.userid);
            idCheck++;
        }
        assertEquals(25, idCheck);
    }

    /**
     * test RowSet return type, defines its own rowset mapper
     * @throws Exception
     */
    public void testRowSetReturnType() throws Exception {
        RowSet customersRowSet = testCtrl.getAllUsersINRS();
        assertNotNull(customersRowSet);
        customersRowSet.beforeFirst();
        customersRowSet.next();
        assertEquals(21, customersRowSet.getInt("USERID"));
    }

    /**
     * test scrollable result set feature, sensitive / updateable
     * @throws Exception
     */
    public void testScrollableInsensitiveUpdateablepResultSet() throws Exception {
        try {
            ResultSet rs = testCtrl.getScrollableResultSet_IU();
            fail("This feature has not been impelented in Derby yet (1/19/2005), need to add test case once it has.");
        } catch (ControlException ce) {
            assertTrue(true);
        }
    }

    /**
     * test scrollable result set feature, forward only / updateable
     * @throws Exception
     */
    public void testScrollableSensitiveResultSet() throws Exception {
        try {
            ResultSet rs = testCtrl.getScrollableResultSet_SR();
            fail("This feature has not been impelented in Derby yet (1/19/2005), need to add test case once it has.");
        } catch (ControlException ce) {
            assertTrue(true);
        }
//        rs.afterLast();
//        rs.beforeFirst();
//        rs.next();
//        rs.next();
//        assertEquals(rs.getInt(2), 22);
//        rs.close();
    }

    /**
     * test holdable result set cursors
     * @throws Exception
     */
    public void testResultSetHoldability() throws Exception {
        ResultSet rs = testCtrl.getResultSetHoldablity();
    }

    /**
     * test for fetchSize / fetchDirection
     * @throws Exception
     */
    public void testFetchSizeDirection() throws Exception {
        ResultSet rs = testCtrl.getFetchOptmizedResultSet();
        rs.next();
        assertEquals(21, rs.getInt(2));
    }

    /**
     * test single column result set mapping
     * @throws Exception
     */
    public void testSingleColumnResultSetMapping() throws Exception {
        String fnames[] = testCtrl.getFnameColumn();
        assertEquals("tester2", fnames[1]);
        assertEquals("tester4", fnames[3]);
    }

    public static Test suite() { 
	    return new TestSuite(DBMultiRowResultsTest.class);
    }

    public static void main(String[] args) { 
	    junit.textui.TestRunner.run(suite());
    }
}

