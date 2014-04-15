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

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Time;
import java.math.BigDecimal;

/**
 * Tests dbcontrol results for single row result sets.
 */
public class JdbcTypesTest extends ControlTestCase {

    @Control
    public ResultsTestCtrl testCtrl;

    public void setUp() throws Exception {
        super.setUp();

        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

        // setup the database
        Connection conn = testCtrl.getConnection();
        Statement s = conn.createStatement();
        try {
            s.executeUpdate("DROP TABLE blob_table");
            s.executeUpdate("DROP TABLE clob_table");
            s.executeUpdate("DROP TABLE basic_types");
        } catch (Exception e) {
        }

        s.executeUpdate("CREATE TABLE blob_table (id INT, BLB BLOB(4k))");
        s.executeUpdate("CREATE TABLE clob_table (id INT, CLB CLOB(8k))");
        s.executeUpdate("INSERT INTO clob_table VALUES (1234, 'thisisaclob1')");
        s.executeUpdate("INSERT INTO clob_table VALUES (5678, 'thisisaclob2')");

        StringBuilder sqlStr = new StringBuilder(
          "CREATE TABLE basic_types ("
          + "c CHAR, ca CHAR(10), vc VARCHAR(20), lvc LONG VARCHAR,"
          + "bin CHAR FOR BIT DATA,  varbin VARCHAR(1024) FOR BIT DATA, lvarbin LONG VARCHAR FOR BIT DATA,"
          + "sint SMALLINT, i INT, bint BIGINT, r REAL, dp DOUBLE PRECISION,"
          + "d DECIMAL, nu NUMERIC, dt DATE, t TIME, ts TIMESTAMP)");
        s.executeUpdate(sqlStr.toString());

        sqlStr = new StringBuilder(
           "INSERT INTO basic_types VALUES("
           + "'c', 'chararray', 'varcharvalue', 'longvarcharvalue',"
           + "X'DE', X'bcff', X'aacc',"
           + "32767, 2147483647, 9223372036854775807, 3.402E+38, 1.79769E+308,"
           + "123.4567, 123.4567, '2004-02-26', '09:45:13', TIMESTAMP('2005-02-23 09:46:17'))");
        s.executeUpdate(sqlStr.toString());
        conn = null;
    }

    /**
     * test Character, single char
     * @throws Exception
     */
    public void testCharacter() throws Exception {
        String chars = testCtrl.getChar();
        assertEquals(chars,"c");
    }

    /**
     * test Character - char string
     * @throws Exception
     */
    public void testCharacter2() throws Exception {
        String chars = testCtrl.getChar2();
        assertEquals(chars,"chararray ");
    }

    /**
     * test varchar
     * @throws Exception
     */
    public void testVarchar() throws Exception {
        String chars = testCtrl.getVarchar();
        assertEquals(chars,"varcharvalue");
    }

    /**
     * test long varchar
     * @throws Exception
     */
    public void testLongvarchar() throws Exception {
        String chars = testCtrl.getLongvarchar();
        assertEquals(chars,"longvarcharvalue");
    }

    /**
     * test fixed length binary
     * @throws Exception
     */
    public void testFixedLengthBinary() throws Exception {
        ResultsTestCtrl.Binary bin = testCtrl.getFixedLengthBinary();
        assertEquals(bin.getBin()[0], -34);
    }

    /**
     * test variable length binary
     * @throws Exception
     */
    public void testVariableLengthBinary() throws Exception {
        ResultsTestCtrl.Binary bin = testCtrl.getVarLengthBinary();
        assertEquals(bin.getVarbin()[0], -68);
    }

    /**
     * test long variable length binary
     * @throws Exception
     */
    public void testLongBinary() throws Exception {
        ResultsTestCtrl.Binary bin = testCtrl.getLongVarLengthBinary();
        assertEquals(bin.getLvarbin()[0], -86);
    }

    /**
     * test small int
     * @throws Exception
     */
    public void testSmallInt() throws Exception {
        short smallInt = testCtrl.getSmallIntValue();
        assertEquals(32767, smallInt);
    }

    /**
     * test small int
     * @throws Exception
     */
    public void testSmallInt2() throws Exception {
        Short smallInt = testCtrl.getSmallIntValue2();
        assertEquals(32767, smallInt.shortValue());
    }

    /**
     * test int
     * @throws Exception
     */
    public void testInt() throws Exception {
        int i = testCtrl.getIntValue();
        assertEquals(2147483647, i);
    }

    /**
     * test int
     * @throws Exception
     */
    public void testInt2() throws Exception {
        Integer i = testCtrl.getIntValue2();
        assertEquals(2147483647, i.intValue());
    }

    /**
     * test big int
     * @throws Exception
     */
    public void testBigInt() throws Exception {
        long i = testCtrl.getBigIntValue();
        assertEquals(9223372036854775807L, i);
    }

    /**
     * test big int
     * @throws Exception
     */
    public void testBigInt2() throws Exception {
        Long i = testCtrl.getBigIntValue2();
        assertEquals(9223372036854775807L, i.longValue());
    }

    /**
     * test real
     * @throws Exception
     */
    public void testReal() throws Exception {
        float f = testCtrl.getRealValue();
        assertEquals(3.402E+38f, f);
    }

    /**
     * test real
     * @throws Exception
     */
    public void testReal2() throws Exception {
        Float f = testCtrl.getRealValue2();
        assertEquals(3.402E+38f, f.floatValue());
    }

    /**
     * test double precision
     * @throws Exception
     */
    public void testDoublePrecision() throws Exception {
        double d = testCtrl.getDoubleValue();
        assertEquals(1.79769E+308, d);
    }

    /**
     * test double precision
     * @throws Exception
     */
    public void testDoublePrecision2() throws Exception {
        Double d = testCtrl.getDoubleValue2();
        assertEquals(1.79769E+308, d.doubleValue());
    }

    /**
     * test decimal
     * @throws Exception
     */
    public void testDecimal() throws Exception {
        BigDecimal d = testCtrl.getDecimalValue();
        assertEquals(123.0, d.doubleValue());
    }

    /**
     * test numeric
     * @throws Exception
     */
    public void testNumeric() throws Exception {
        BigDecimal d = testCtrl.getNumericValue();
        assertEquals(123.0, d.doubleValue());
    }

    /**
     * test date
     * @throws Exception
     */
    public void testDate() throws Exception {
        java.sql.Date d = testCtrl.getDateValue();
        assertEquals("2004-02-26", d.toString());
    }

    /**
     * test time
     * @throws Exception
     */
    public void testTime() throws Exception {
        Time t = testCtrl.getTimeValue();
        assertEquals("09:45:13", t.toString());
    }

    /**
     * test timestamp
     * @throws Exception
     */
    public void testTimestamp() throws Exception {
        java.sql.Timestamp ts = testCtrl.getTimestampValue();
        assertEquals("2005-02-23 09:46:17.0", ts.toString());
    }

    /**
     * Test blob.
     * @throws Exception
     */
    public void testBlob() throws Exception {
        assertNotNull(testCtrl);

        //
        // attempt to insert a blob value
        //
        Blob myBlob = new SerialBlob(new byte[] {1,2,3});
        int result = testCtrl.insertABlob(123, myBlob);
        assertEquals(result,1);

        //
        // retrieve the blob value just inserted and validate
        //
        ResultsTestCtrl.BlobInfo b = testCtrl.getABlob(123);
        assertNotNull(b);
        byte[] check = b.getBlb().getBytes(1,3);
        assertEquals(check[0], 1);
    }

    /**
     * Test clob.
     * @throws Exception
     */
    public void testClob() throws Exception {
        assertNotNull(testCtrl);

        //
        // get a clob and validate
        //
        Clob c = testCtrl.getAClob(1234);
        assertNotNull(c);
        assertEquals(c.getSubString(1,3), "thi");
    }

    public static Test suite() { return new TestSuite(JdbcTypesTest.class); }

    public static void main(String[] args) { junit.textui.TestRunner.run(suite()); }
}

