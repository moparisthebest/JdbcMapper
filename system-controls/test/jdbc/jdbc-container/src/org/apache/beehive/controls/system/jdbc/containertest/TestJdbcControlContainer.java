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

package org.apache.beehive.controls.system.jdbc.containertest;

import junit.framework.Test;
import junit.framework.TestCase;
import org.apache.beehive.controls.api.bean.Control;
import org.apache.beehive.controls.api.context.ControlThreadContext;
import org.apache.beehive.controls.api.context.ControlContainerContext;
import org.apache.cactus.ServletTestSuite;
import org.apache.beehive.controls.system.jdbc.test.dbconnection.DataSourceConnectionCtrl;
import org.apache.beehive.controls.system.jdbc.units.errors.ErrorPathsTest;
import org.apache.beehive.controls.system.jdbc.units.results.DBMultiRowResultsTest;
import org.apache.beehive.controls.system.jdbc.units.results.DBSingleRowResultsTest;
import org.apache.beehive.controls.system.jdbc.units.results.JdbcTypesTest;
import org.apache.beehive.controls.system.jdbc.units.results.XmlBeanResultsTest;
import org.apache.beehive.controls.system.jdbc.units.sqlparser.SqlParserTest;

import java.sql.Connection;
import java.sql.SQLException;

/**
 */
public class TestJdbcControlContainer extends TestCase {

    private ControlContainerContext _controlContext = null;

    @Control
    private DataSourceConnectionCtrl ctrl;

    /**
     * Wrap the standalone Junit tests in Cactus.
     *
     * @return
     */
    public static Test suite() {
        ServletTestSuite suite = new ServletTestSuite();
        suite.addTestSuite(TestJdbcControlContainer.class);
        suite.addTestSuite(DBSingleRowResultsTest.class);
        suite.addTestSuite(DBMultiRowResultsTest.class);
        suite.addTestSuite(XmlBeanResultsTest.class);
        suite.addTestSuite(JdbcTypesTest.class);
        suite.addTestSuite(SqlParserTest.class);
        suite.addTestSuite(ErrorPathsTest.class);
        return suite;
    }

    public void setUp() throws Exception {
        super.setUp();
        _controlContext = ControlThreadContext.getContext();
        TestJdbcControlContainerClientInitializer.initialize(_controlContext, this);
    }

    public void testDataSourceConnection() throws SQLException {
        Connection conn = ctrl.getConnection();
        assertNotNull(conn);
    }
}
