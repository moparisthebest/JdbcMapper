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
import org.apache.beehive.controls.system.jdbc.JdbcControl;
import org.apache.beehive.controls.system.jdbc.test.results.ResultsTestCtrl;
import org.apache.beehive.controls.test.junit.ControlTestCase;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Types;

/**
 * Unit tests for stored procedures
 */
public class StoredProcsDBResultsTest extends ControlTestCase {

    @Control
    public ResultsTestCtrl testCtrl;

    public void setUp() throws Exception {
        super.setUp();

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        Connection conn = testCtrl.getConnection();

        // setup the database
        Statement s = conn.createStatement();
        try {
            s.executeUpdate("DROP TABLE PRODUCTS");
            s.executeUpdate("DROP PROCEDURE getExpensiveProductsSP");
            s.executeUpdate("DROP PROCEDURE getExpensiveProductSP");
            s.executeUpdate("DROP PROCEDURE getProductsByColorSP");
            s.executeUpdate("DROP PROCEDURE getProductSP");
        } catch (Exception e) {}

        s.executeUpdate("CREATE TABLE PRODUCTS (SKU INT, PRODUCT_NAME VARCHAR(128), PRICE FLOAT, COLOR VARCHAR(64))");
        s.executeUpdate("INSERT INTO PRODUCTS VALUES (112233, 'Widget 1', 99.99, 'red')");
        s.executeUpdate("INSERT INTO PRODUCTS VALUES (445566, 'Widget 2', 66.66, 'green')");
        s.executeUpdate("INSERT INTO PRODUCTS VALUES (778899, 'Widget 3', 121.22, 'blue')");
        s.executeUpdate("INSERT INTO PRODUCTS VALUES (2357, 'Widget 4', 1099.99, 'red')");

        s.executeUpdate("CREATE PROCEDURE getExpensiveProductsSP(OUT productNames VARCHAR(128)) " +
                        "PARAMETER STYLE JAVA " +
                        "READS SQL DATA " +
                        "LANGUAGE JAVA " +
                        "EXTERNAL NAME 'org.apache.beehive.controls.system.jdbc.units.utils.StoredProcedures.getExpensiveProductsSP'");

        s.executeUpdate("CREATE PROCEDURE getExpensiveProductSP(OUT productName VARCHAR(128)) " +
                        "PARAMETER STYLE JAVA " +
                        "READS SQL DATA " +
                        "LANGUAGE JAVA " +
                        "EXTERNAL NAME 'org.apache.beehive.controls.system.jdbc.units.utils.StoredProcedures.getExpensiveProductSP'");

        s.executeUpdate("CREATE PROCEDURE getProductsByColorSP(IN color VARCHAR(64), OUT productNames VARCHAR(128)) " +
                        "PARAMETER STYLE JAVA " +
                        "READS SQL DATA " +
                        "LANGUAGE JAVA " +
                        "EXTERNAL NAME 'org.apache.beehive.controls.system.jdbc.units.utils.StoredProcedures.getProductsByColorSP'");
        s.close();
        conn = null;
    }

    /**
     * simple sp which does not do a database query in the sp
     * @throws Exception
     */
    public void testSimpleSP() throws Exception {
        assertNotNull(testCtrl);
        JdbcControl.SQLParameter[] params = new JdbcControl.SQLParameter[1];
        params[0] = new JdbcControl.SQLParameter(new String(), Types.VARCHAR, JdbcControl.SQLParameter.OUT);
        testCtrl.getExpensiveProduct(params);
        assertEquals(params[0].value, "foo");
    }

    /**
     * sp which does a database query
     * @throws Exception
     */
    public void testSP() throws Exception {
        assertNotNull(testCtrl);
        JdbcControl.SQLParameter[] params = new JdbcControl.SQLParameter[1];
        params[0] = new JdbcControl.SQLParameter(new String[8], Types.VARCHAR, JdbcControl.SQLParameter.OUT);
        testCtrl.getExpensiveProducts(params);
        assertEquals(params[0].value, "Widget 4");
    }

    /**
     * sp which does a database query and uses IN and OUT params
     * @throws Exception
     */
    public void testSPOutParams() throws Exception {
        assertNotNull(testCtrl);
        JdbcControl.SQLParameter[] params = new JdbcControl.SQLParameter[2];
        params[0] = new JdbcControl.SQLParameter("red", Types.VARCHAR, JdbcControl.SQLParameter.IN);
        params[1] = new JdbcControl.SQLParameter(new String[8], Types.VARCHAR, JdbcControl.SQLParameter.OUT);
        testCtrl.getProductsByColor(params);
        assertEquals(params[1].value, "Widget 1,Widget 4,");
    }

    /**
     * create a stored proc with the dbControl then run it -- only contains IN parameters
     * @throws Exception
     */
    public void testSPGeneration() throws Exception {
        assertNotNull(testCtrl);
        testCtrl.createStoredProc();
        testCtrl.getProduct("red", 1234);
    }

    public static Test suite() { return new TestSuite(StoredProcsDBResultsTest.class); }
    public static void main(String[] args) { junit.textui.TestRunner.run(suite()); }
}

