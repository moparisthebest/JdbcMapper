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
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests dbcontrol results for single row result sets.
 */
public class DBSingleRowResultsTest extends ControlTestCase {

    @Control
    private ResultsTestCtrl testCtrl;

    public void setUp() throws Exception {
        super.setUp();

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection conn = testCtrl.getConnection();

        // setup the database
        Statement s = conn.createStatement();
        try {
            s.executeUpdate("DROP TABLE users");
            s.executeUpdate("DROP TABLE usergen");
        } catch (Exception e) {
        }

        s.executeUpdate("CREATE TABLE USERS (FNAME VARCHAR(32), USERID INT)");
        s.executeUpdate("INSERT INTO USERS VALUES ('tester1', 21)");
        s.executeUpdate("INSERT INTO USERS VALUES ('tester2', 22)");
        s.executeUpdate("INSERT INTO USERS VALUES ('tester3', 23)");
        s.executeUpdate("INSERT INTO USERS VALUES ('tester4', 24)");

        s.executeUpdate("CREATE TABLE usergen (user_id INT GENERATED ALWAYS AS IDENTITY (START WITH 1) CONSTRAINT people_pk PRIMARY KEY, person VARCHAR(128))");
        s.executeUpdate("INSERT INTO usergen (person) VALUES ('genmeakey')");

        s.close();
        conn = null;
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * test query return ResultSet
     * @throws Exception
     */
    public void testResultSetReturnType() throws Exception {
        assertNotNull(testCtrl);
        ResultSet rs = testCtrl.getAllUsers();
        assertNotNull(rs);
        rs.next();
        String name = rs.getString("FNAME");
        assertEquals(name, "tester1");
        rs.close();
    }

    /**
     * test query with sql: escape, returns ResultSet.
     * @throws Exception
     */
    public void testSqlEscapeResultSet() throws Exception {

        ResultSet rs = testCtrl.getSomeUser("tester4");
        assertNotNull(rs);
        rs.next();
        String name = rs.getString("FNAME");
        assertEquals(name, "tester4");
        rs.close();

        rs = testCtrl.getSomeUser(24);
        assertNotNull(rs);
        rs.next();
        name = rs.getString("FNAME");
        assertEquals(name, "tester4");
        rs.close();
    }

    /**
     * test query with sql: escape
     * @throws Exception
     */
    public void testSqlEscape() throws Exception {
        ResultSet rs = testCtrl.getJustOneUser("fname='tester4'");
        assertNotNull(rs);
        rs.next();
        String name = rs.getString("FNAME");
        assertEquals(name, "tester4");
        rs.close();
    }

    /**
     * test query which does reflection on a property which contains an ComplexSqlFragment type.
     * @throws Exception
     */
    public void testSqlEscapeComplexWhere() throws Exception {
        ResultSet rs = testCtrl.whereClauseSql(new ResultsTestCtrl.ComplexWhereClause());
        assertNotNull(rs);
        rs.next();
        String name = rs.getString("FNAME");
        assertEquals(name, "tester4");
        rs.close();
    }

    /**
     * test query which does reflection on a property which contains an ComplexSqlFragment type.
     * @throws Exception
     */
    public void testComplexWhere() throws Exception {
        ResultSet rs = testCtrl.whereClauseSub(new ResultsTestCtrl.ComplexWhereClause());
        assertNotNull(rs);
        rs.next();
        String name = rs.getString("FNAME");
        assertEquals(name, "tester4");
        rs.close();
    }

    /**
     * param sub from object getter method
     * @throws Exception
     */
    public void testGetUserWithObj() throws Exception {
        ResultsTestCtrl.CustomerInput1 ci = new ResultsTestCtrl.CustomerInput1();
        ci.setUserid(22);
        String c = testCtrl.getSomeUser(ci);
        assertEquals("tester2",c);
    }

    /**
     * param sub from object field
     * @throws Exception
     */
    public void testGetUserWithObj2() throws Exception {
        ResultsTestCtrl.CustomerInput2 ci = new ResultsTestCtrl.CustomerInput2();
        ci.userid = 23;
        String c = testCtrl.getSomeUser(ci);
        assertEquals("tester3",c);
    }

    /**
     * param sub from map
     * @throws Exception
     */
    public void testGetUserWithObj3() throws Exception {
        HashMap map = new HashMap();
        map.put("userid", 21);
        String c = testCtrl.getSomeUser(map);
        assertEquals("tester1",c);
    }

    /**
     * param sub from nested object
     * @throws Exception
     */
    public void testGetUserWithObj4() throws Exception {
        ResultsTestCtrl.CustomerWrapper cw = new ResultsTestCtrl.CustomerWrapper();
        cw.c = new ResultsTestCtrl.CustomerInput2();
        cw.c.userid = 22;
        String c = testCtrl.getSomeUser(cw);
        assertEquals("tester2", c);
    }

    /**
     * param sub from object getter method, inherited from base class
     * @throws Exception
     */
    public void testGetUserWithObj5() throws Exception {
        ResultsTestCtrl.CustomerInput3 ci = new ResultsTestCtrl.CustomerInput3();
        ci.setUserid(22);
        String c = testCtrl.getSomeUser(ci);
        assertEquals("tester2",c);
    }

    /**
     * param sub from object getter method, inherited from base class
     * @throws Exception
     */
    public void testGetUserWithObj6() throws Exception {
        ResultsTestCtrl.CustomerInput4 ci = new ResultsTestCtrl.CustomerInput4();
        ci.userid = 22;
        String c = testCtrl.getSomeUser(ci);
        assertEquals("tester2",c);
    }

    /**
     * test HashMap return type
     * @throws Exception
     */
    public void testHashMapReturnType() throws Exception {
        HashMap customerHashMap = testCtrl.getCustomerHashMap(23);
        assertNotNull(customerHashMap);
        assertEquals(customerHashMap.get("FNAME"), "tester3");
    }

    /**
     * test Map return type
     * @throws Exception
     */
    public void testMapReturnType() throws Exception {
        Map customerMap = testCtrl.getCustomerMap(22);
        assertNotNull(customerMap);
        assertEquals(customerMap.get("FNAME"), "tester2");
    }

    /**
     * test null / both Object and int return Types
     * @throws Exception
     */
    public void testNullReturnType() throws Exception {
        int prim = testCtrl.getNoUsers(111);
        assertEquals(prim, 0);
        ResultsTestCtrl.Customer customer = testCtrl.getACustomer(111);
        assertNull(customer);
    }

    /**
     * test Object return type
     * @throws Exception
     */
    public void testObjectReturnType() throws Exception {
        ResultsTestCtrl.Customer customer = testCtrl.getACustomer(23);
        assertNotNull(customer);
        assertEquals(customer.getFname(), "tester3");
        assertEquals(customer.userid, 23);
    }

    /**
     * add some new users to the table using batch update
     * @throws Exception
     */
    public void testBatchUpdate() throws Exception {
        int[] results = testCtrl.doABatchUpdate(new String[]{"tester44", "tester55", "tester66"}, new int[]{44, 55, 66});
        assertEquals(1, results[0]);
        assertEquals(1, results[1]);
        assertEquals(1, results[2]);
    }

    /**
     * get the generated keys from the SQL statement
     * @throws Exception
     */
    public void testGenKeys() throws Exception {
        ResultSet rs = testCtrl.getGenKeys("genmeanotherkey");
        assertNotNull(rs);
        rs.next();
        int generatedId = rs.getInt(1);
        assertEquals(generatedId, 2);
        rs.close();
    }

    /**
     * get the generated keys from the SQL statement -- with specified column names
     * @throws Exception
     */
    public void testGenKeysWithColumnNames() throws Exception {
        try {
            /* ResultSet rs = */testCtrl.getGenKeys2("genmeanotherkey2");
            fail("This feature has not been impelented in Derby yet (1/1/2005), need to add test case once it has.");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    /**
     * get the generated keys from the SQL statement -- with specified column names and return type mapping
     * @throws Exception
     */
    public void testGenKeysReturnTypeMapping() throws Exception {
        int result = testCtrl.getGenKeys3("genmeanotherkey3");
        assertEquals(result, 2);
    }

    /**
     * get the generated keys from the SQL statement -- with specified column names and return type mapping
     * @throws Exception
     */
    public void testGenKeysReturnTypeMapping2() throws Exception {
        String result = testCtrl.getGenKeys4("genmeanotherkey4");
        assertEquals(result, "2");
    }

    /**
     * get the generated keys from the SQL statement -- with specified column names and return type mapping
     * @throws Exception
     */
    public void testGenKeysReturnTypeMapping3() throws Exception {
        int[] result = testCtrl.getGenKeys5("genmeanotherkey5");
        assertEquals(result[0], 2);
    }

    /**
     * get the generated keys from the SQL statement -- with specified column indexes
     * @throws Exception
     */
    public void testGenKeysReturnTypeMapping4() throws Exception {
        try {
            /*ResultSet rs = */testCtrl.getGenKeys6("genmeanotherkey6");
            fail("This feature has not been impelented in Derby yet (1/1/2005), need to add test case once it has.");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    public static Test suite() {
        return new TestSuite(DBSingleRowResultsTest.class);
    }

    public static void main(String[] args) throws Exception {
        junit.textui.TestRunner.run(suite());
    }
}

