/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beehive.controls.system.jdbc.units;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.beehive.controls.test.container.ControlTestContainerContext;
import org.apache.beehive.controls.test.junit.ControlTestCase;
import org.apache.beehive.controls.system.jdbc.test.results.ResultsTestCtrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Serialization test for the WSC.  Serialize and deserialize a wsc and make sure
 * it still functions.
 */
public class JdbcControlSerializationTest
        extends ControlTestCase {

    @Control
    public ResultsTestCtrl _testCtrl;

    public void setUp() throws Exception {
        //
        // intentially not calling super()
        //
        Class.forName("org.apache.derby.jdbc.EmbeddedDriver");

        //
        // explicitly start the context
        //
        getControlContainerContextManager().beginContext();
        initializeControls();

        // setup the database
        Connection conn = _testCtrl.getConnection();
        Statement s = conn.createStatement();
        try {
            s.executeUpdate("DROP TABLE basic_types_ser");
        }
        catch (Exception e) {
        }

        StringBuilder sqlStr = new StringBuilder("CREATE TABLE basic_types_ser ( i INT )");
        s.executeUpdate(sqlStr.toString());

        sqlStr = new StringBuilder("INSERT INTO basic_types_ser VALUES( 2147483647 )");
        s.executeUpdate(sqlStr.toString());
        conn = null;
    }

    public void tearDown() {
        // intentially not calling super()
    }

    /**
     * Basic serialization test for the jdbc control.  Serialize the control, then deserialize and
     * verify that a control method call works as expected.
     *
     * @throws Exception
     */
    public void testSerialization() throws Exception {

        assertNotNull(_testCtrl);

        int i = _testCtrl.getIntValueSer();
        assertEquals(2147483647, i);

        ControlContainerContext ccc = getControlContainerContext();

        //
        // end the context
        //
        getControlContainerContextManager().endContext();

        //
        // serialize the wsc
        //
        File serFile = File.createTempFile("jdbcc", "ser");
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile));
        ControlTestContainerContext ctcc = (ControlTestContainerContext) ccc;
        oos.writeObject(ctcc);
        oos.close();

        //
        // deserialize the ctcc contents
        //
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile));
        ctcc = (ControlTestContainerContext)ois.readObject();
        ois.close();
        serFile.delete();

        //
        // find the deserialized instance in the ctcc
        //
        Object[] ctrls = ctcc.toArray();
        ResultsTestCtrl deserializedControl = null;
        for (Object c : ctrls) {
            if (c instanceof ResultsTestCtrl && !c.equals(_testCtrl)) {
                deserializedControl = (ResultsTestCtrl) c;
                break;
            }
        }

        //
        // start the context
        //
        getControlContainerContextManager().beginContext();

        assertNotNull(deserializedControl);
        assertFalse(deserializedControl == _testCtrl);
        int ii = _testCtrl.getIntValueSer();
        assertEquals(2147483647, ii);
        getControlContainerContextManager().endContext();
    }

    public static Test suite() {
        return new TestSuite(JdbcControlSerializationTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}