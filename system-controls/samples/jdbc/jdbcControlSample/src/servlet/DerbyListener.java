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
package servlet;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import org.apache.beehive.netui.util.logging.Logger;

/**
 *
 */
public class DerbyListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getInstance(DerbyListener.class);
    private static final String _dbUrlStr = "jdbc:derby:JdbcControlSampleDB";

    public void contextInitialized(ServletContextEvent event) {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch(Exception e) {
            assert e instanceof ClassNotFoundException;
            e.printStackTrace();
            LOGGER.error("Exception occurred starting webapp context.  Cause: " + e.getMessage(), e);
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        try {
            DriverManager.getConnection(_dbUrlStr + ";shudown=true");
        } catch(SQLException sql) {
            sql.printStackTrace();
            LOGGER.error("Exception occurred stopping webapp context.  Cause: " + sql.getMessage(), sql);
        }
    }
}
