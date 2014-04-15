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

package org.apache.beehive.controls.system.jdbc.units.dbconnection;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.system.jdbc.test.dbconnection.DriverManagerConnectionCtrl;
import org.apache.beehive.controls.system.jdbc.test.dbconnection.DriverManagerConnectionCtrlAuth;
import org.apache.beehive.controls.system.jdbc.test.dbconnection.DriverManagerConnectionCtrlProps;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Test gettting database connections.
 */
public class DBConnectionTest extends ControlTestCase {

    @Control
    public DriverManagerConnectionCtrl testCtrl;
    @Control
    public DriverManagerConnectionCtrlAuth testAuthCtrl;
    @Control
    public DriverManagerConnectionCtrlProps testPropsCtrl;

    /**
     * Get a connection.
     * @throws Exception
     */
    public void testDriverMgrConnection_simple() throws Exception {
        assertNotNull(testCtrl);
        testCtrl.getConnection();
    }

    /**
     * Get a connection which requires authentication.
     * @throws Exception
     */
    public void testDriverMgrConnection_auth() throws Exception {

        // add authentication and test
        Connection conn = DriverManager.getConnection("jdbc:derby:MyDBAuth;create=true");
        Statement s = conn.createStatement();

       // Setting and Confirming requireAuthentication
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
            "'derby.connection.requireAuthentication', 'true')");
        ResultSet rs = s.executeQuery(
            "VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(" +
            "'derby.connection.requireAuthentication')");
        rs.next();
        //System.out.println(rs.getString(1));

        // Setting authentication scheme to Derby
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
            "'derby.authentication.provider', 'BUILTIN')");

        // Creating some sample users
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
            "'derby.user.foo', 'bar')");
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
            "'derby.user.test', 'test')");

        // Setting default connection mode to no access
        // (user authorization)
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
            "'derby.database.defaultConnectionMode', 'noAccess')");
        // Confirming default connection mode
        rs = s.executeQuery (
            "VALUES SYSCS_UTIL.SYSCS_GET_DATABASE_PROPERTY(" +
            "'derby.database.defaultConnectionMode')");
        rs.next();
        assertEquals(rs.getString(1), "noAccess");

        // Defining read-write users
        s.executeUpdate("CALL SYSCS_UTIL.SYSCS_SET_DATABASE_PROPERTY(" +
            "'derby.database.fullAccessUsers', 'foo')");

        conn.close();

        assertNotNull(testAuthCtrl);
        testAuthCtrl.getConnection();
    }

    /**
     * Get a connection with properties.
     * @throws Exception
     */
    public void testDriverMgrConnection_props() throws Exception {
        assertNotNull(testPropsCtrl);
        testPropsCtrl.getConnection();
    }

    public static Test suite() { return new TestSuite(DBConnectionTest.class); }

    public static void main(String[] args) { junit.textui.TestRunner.run(suite()); }
}

